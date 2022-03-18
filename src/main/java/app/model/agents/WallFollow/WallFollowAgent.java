package app.model.agents.WallFollow;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.Move;
import app.model.agents.AgentImp;
import app.model.agents.Cells.BooleanCell;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WallFollowAgent extends AgentImp
{
    public enum TurnType
    {
        LEFT,
        RIGHT,
        NO_TURN,
    }
    @Getter @Setter private boolean movedForwardLast = false;
    @Getter @Setter private TurnType lastTurn = TurnType.NO_TURN;
    @Getter @Setter private double moveLength = 20;
    private double maxViewingDistance = 100;
    @Setter private boolean DEBUG = false;
    @Getter @Setter private boolean wallEncountered = false;
    public static Map map;
    private final app.model.agents.WallFollow.BooleanCellGraph<BooleanCell, DefaultEdge> cellGraph;
    private boolean initialVertexFound = false;
    private BooleanCell tempAgentCell;
    private boolean noMovesDone = true;
    private final List<Vector> directions = Arrays.asList(new Vector(0,1),
                                                    new Vector(1,0),
                                                    new Vector(0,-1),
                                                    new Vector(-1,0));
    private List<BooleanCell> currentPathToNextVertex = null;
    private BooleanCell currentTargetVertex = null;
    private ArrayList<BooleanCell> inaccessibleCells = new ArrayList<>();
    private boolean explorationDone = false;

    public WallFollowAgent(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
        cellGraph = new app.model.agents.WallFollow.BooleanCellGraph<>();
        cellGraph.setEdge((int)moveLength);
        BooleanCell agentInitialVertex = new BooleanCell(0,0);
        cellGraph.addVertex(agentInitialVertex);
        cellGraph.getVertices().put(agentInitialVertex.toString(),agentInitialVertex);
        updateNeighbouringVertices(agentInitialVertex);
        noMovesDone = false;
    }

    public WallFollowAgent(Vector position, Vector direction, double radius, double moveLen)
    {
        super(position, direction, radius);
        this.moveLength = moveLen;
        cellGraph = new app.model.agents.WallFollow.BooleanCellGraph<>();
        cellGraph.setEdge((int)moveLength);
        BooleanCell agentInitialVertex = new BooleanCell(0,0);
        tempAgentCell = agentInitialVertex;
        cellGraph.addVertex(agentInitialVertex);
        updateNeighbouringVertices(agentInitialVertex);
        noMovesDone = false;
    }

    /**
     * general algorithm:
        if last move failed
            handle failed move
        else if following path
            get move based on path
        else if found initial wall following vertex or stuck in weird movement
            run heuristics algorithm
        else if no wall encountered yet
            if (no wall forward)
                go forward
            else
                turn 90 deg right
                set wallEcountered = true
        else
            run wall following algorithm
     */
    @Override
    public Move move()
    {
        Vector deltaPos = new Vector(0,0);
        Vector newDirection = direction;

        if (explorationDone)
        {
            return new Move(direction, deltaPos);
        }
        if (isMoveFailed())
        {
            if (DEBUG) {
                System.out.println("Move failed.");
            }
            Move failedMoveHandle = handleFailedMove();
            deltaPos = failedMoveHandle.getDeltaPos();
            newDirection = failedMoveHandle.getEndDir();
            currentTargetVertex = null;
            currentPathToNextVertex = null;
        }
        else if (cellGraph.agentStuckInVertex())
        {
            if (DEBUG) {
                System.out.println("Agent stuck in vertex.");
            }
            BooleanCell forwardCell = getAgentNeighbourBasedOnAngle(direction.getAngle());
            if (noWallDetected(direction.getAngle()) && !forwardCell.getObstacle())
            {
                deltaPos = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
                movedForwardLast = true;
                lastTurn = TurnType.NO_TURN;
            }
            else
            {
                newDirection = rotateAgentRight();
                lastTurn = TurnType.RIGHT;
                movedForwardLast = false;
            }
            wallEncountered = false;
            currentTargetVertex = null;
            currentPathToNextVertex = null;
        }
        else if (currentPathToNextVertex != null)
        {
            if (DEBUG) {
                System.out.println("Following path.");
            }
            Move pathMove = getMoveBasedOnPath();
            deltaPos = pathMove.getDeltaPos();
            newDirection = pathMove.getEndDir();
        }
        else if (initialVertexFound || cellGraph.agentInStuckMovement())
        {
            if (DEBUG) {
                System.out.println("Found initial vertex or got stuck, following heuristics now.");
            }
            Move heuristicsMove = runHeuristicsAlgorithm();
            deltaPos = heuristicsMove.getDeltaPos();
            newDirection = heuristicsMove.getEndDir();
        }
        else if (!wallEncountered) {
            BooleanCell forwardCell = getAgentNeighbourBasedOnAngle(direction.getAngle());
            if (noWallDetected(direction.getAngle()) && !forwardCell.getObstacle()) {
                if (DEBUG) {
                    System.out.println("ALGORITHM CASE 0: no wall encountered");
                }
                deltaPos = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
                movedForwardLast = true;
                lastTurn = TurnType.NO_TURN;
            } else {
                if (DEBUG) {
                    System.out.println("ALGORITHM CASE 0: wall encountered!");
                }
                newDirection = rotateAgentRight();
                lastTurn = TurnType.RIGHT;
                movedForwardLast = false;
                wallEncountered = true;
                cellGraph.setInitialWallFollowPos(cellGraph.getAgentPos());
            }
        }
        else
        {
            Move wallFollowMove = runWallFollowAlgorithm();
            deltaPos = wallFollowMove.getDeltaPos();
            newDirection = wallFollowMove.getEndDir();
        }
        Move nextMove = new Move(newDirection, deltaPos);
        direction = nextMove.getEndDir();
        updateGraph();
        return nextMove;
    }

    /** Pseudocode for simple wall following algorithm:
        if (turned left previously and forward no wall)
            go forward;
        else if (no wall at left)
            turn 90 deg left;
        else if (no wall forward)
            go forward;
        else
            turn 90 deg right;

        * based on: https://blogs.ntu.edu.sg/scemdp-201718s1-g14/exploration-algorithm/
     */
    public Move runWallFollowAlgorithm()
    {
        Vector newMove = new Vector(0,0);
        Vector newDirection = direction;
        BooleanCell forwardCell = getAgentNeighbourBasedOnAngle(direction.getAngle());
        BooleanCell leftCell = getAgentNeighbourBasedOnAngle(rotateAgentLeft().getAngle());
        if (lastTurn == TurnType.LEFT && noWallDetected(direction.getAngle()) && !forwardCell.getObstacle())
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 1"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            movedForwardLast = true;
            lastTurn = TurnType.NO_TURN;
        }
        else if (noWallDetected(getAngleOfLeftRay()) && !leftCell.getObstacle())
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 2"); }
            newDirection = rotateAgentLeft();
            lastTurn = TurnType.LEFT;
            movedForwardLast = false;
        }
        else if (noWallDetected(direction.getAngle()) && !forwardCell.getObstacle())
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 3"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            movedForwardLast = true;
            lastTurn = TurnType.NO_TURN;
        }
        else
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 4"); }
            newDirection = rotateAgentRight();
            lastTurn = TurnType.RIGHT;
            movedForwardLast = false;
        }
        return new Move(newDirection,newMove);
    }

    /**
     1. Get list of vertices with less than 4 edges to them (the unexplored frontier)
     2. Use heuristics to pick best vertex to move to
     3. Move there following Dijkstra's shortest path
     4. If vertex reached, start doing wall following again
     */
    public Move runHeuristicsAlgorithm()
    {
        Vector newMove = new Vector(0,0);
        Vector newDirection = direction;
        ArrayList<BooleanCell> unexploredVertices = cellGraph.getVerticesWithUnexploredNeighbours();
        if (unexploredVertices.size() == 0)
        {
            explorationDone = true;
        }
        else
        {
            double minScore = 0;
            BooleanCell minScoreVertex = null;
            for (BooleanCell vertex : unexploredVertices)
            {
                if (!inaccessibleCells.contains(vertex))
                {
                    double score = getVertexScore(vertex);
                    if (minScore == 0 || score < minScore)
                    {
                        minScore = score;
                        minScoreVertex = vertex;
                    }
                }
            }
            if (minScoreVertex != null)
            {
                currentTargetVertex = minScoreVertex;
                currentPathToNextVertex = DijkstraShortestPath.findPathBetween(cellGraph,
                        cellGraph.getAgentPos(), currentTargetVertex).getVertexList();
                return getMoveBasedOnPath();
            }
        }
        return new Move(newDirection, newMove);
    }

    public Move handleFailedMove()
    {
        Vector deltaPos = new Vector(0,0);
        Vector newDirection = direction;
        if (movedForwardLast)
        {
            if (currentTargetVertex != null)
            {
                inaccessibleCells.add(currentTargetVertex);
            }
            cellGraph.moveAgentBack();
            newDirection = rotateAgentRight();
            lastTurn = TurnType.RIGHT;
            movedForwardLast = false;
        }
        else {
            throw new RuntimeException("Move failed but last move was not position change!");
        }
        return new Move(newDirection, deltaPos);
    }

    public double getVertexScore(BooleanCell vertex)
    {
        // currently takes into account shortest path length and how many neighbours also have unexplored cells
        // TODO add score for if agent is facing that direction already
        // TODO add weights to score components
        double score;
        int shortestPathLength = DijkstraShortestPath.findPathBetween(cellGraph, cellGraph.getAgentPos(), vertex).getLength();
        int neighboursOnUnexploredFrontier = 0;
        List<BooleanCell> neighbours = Graphs.neighborListOf(cellGraph,vertex);
        for (BooleanCell neighbour : neighbours)
        {
            if (!neighbour.getObstacle() && cellGraph.edgesOf(vertex).size() < 4)
            {
                neighboursOnUnexploredFrontier++;
            }
        }
        score = shortestPathLength;
        if (neighboursOnUnexploredFrontier != 0)
        {
            score = score / neighboursOnUnexploredFrontier;
        }
        return score;
    }

    public Move getMoveBasedOnPath()
    {
        BooleanCell nextVertex = currentPathToNextVertex.get(0);
        if (nextVertex.equals(cellGraph.getAgentPos()))
        {
            currentPathToNextVertex.remove(nextVertex);
            nextVertex = currentPathToNextVertex.get(0);
        }
        Vector nextDir = cellGraph.getNeighbourDir(nextVertex);
        if (nextDir.equals(direction))
        {
            double deltaX = nextVertex.getX() - cellGraph.getAgentPos().getX();
            double deltaY = nextVertex.getY() - cellGraph.getAgentPos().getY();
            currentPathToNextVertex.remove(nextVertex);
            if (currentPathToNextVertex.isEmpty())
            {
                currentPathToNextVertex = null;
                inaccessibleCells.clear();
            }
            lastTurn = TurnType.NO_TURN;
            movedForwardLast = true;
            return new Move(direction, new Vector(deltaX,deltaY));
        }
        else
        {
            if (rotateAgentLeft().equals(nextDir))
            {
                lastTurn = TurnType.LEFT;
                movedForwardLast = false;
                return new Move(rotateAgentLeft(),new Vector(0,0));
            }
            else if (rotateAgentRight().equals(nextDir))
            {
                lastTurn = TurnType.RIGHT;
                movedForwardLast = false;
                return new Move(rotateAgentRight(),new Vector(0,0));
            }
            else
            {
                lastTurn = TurnType.LEFT;
                movedForwardLast = false;
                return new Move(rotateAgentLeft(),new Vector(0,0));
            }
        }
    }

    public void updateGraph()
    {
        int agentX = cellGraph.getAgentPos().getX();
        int agentY = cellGraph.getAgentPos().getY();

        if (getLastTurn() != TurnType.NO_TURN && !movedForwardLast)
        {
            cellGraph.updateLastPositions();
            BooleanCell cellToUpdate = null;
            if (lastTurn == TurnType.LEFT)
            {
                cellToUpdate = getNeighbourVertex((agentX + rotateAgentLeft().getX() * moveLength),
                        (agentY + rotateAgentLeft().getY() * moveLength),
                        rotateAgentLeft().getAngle(),
                        false,
                        1);
            }
            else if (lastTurn == TurnType.RIGHT)
            {
                cellToUpdate = getNeighbourVertex((agentX + rotateAgentRight().getX() * moveLength),
                        (agentY + rotateAgentRight().getY() * moveLength),
                        rotateAgentRight().getAngle(),
                        false,
                        1);
            }
            if (cellToUpdate != null)
            {
                cellGraph.updateVertex(cellToUpdate);
            }
        }
        else if (movedForwardLast)
        {
            BooleanCell newAgentCell = getAgentNeighbourBasedOnAngle(direction.getAngle());
            updateNeighbouringVertices(newAgentCell);
        }
    }

    public BooleanCell getNeighbourVertex(int neighbourX,
                                          int neighbourY,
                                          double rayAngle,
                                          boolean exploring,
                                          int distFromAgentVertex)
    {
        BooleanCell neighbour;
        if (!cellGraph.getVertices().containsKey(neighbourX + " " + neighbourY))
        {
            neighbour = new BooleanCell(neighbourX, neighbourY);
            // TODO differentiate between obstacle and occupied cell (use moveFailed)
            if (!exploring && !noWallDetected(rayAngle))
            {
                neighbour.setObstacle(true);
            }
            if (exploring && !noObstacleDetectedFromPos(rayAngle, distFromAgentVertex))
            {
                neighbour.setObstacle(true);
            }
        }
        else
        {
            neighbour = cellGraph.getVertices().get(neighbourX + " " + neighbourY);
            if (cellGraph.getInitialWallFollowPos() != null &&
                    rayAngle == direction.getAngle() &&
                    !tempAgentCell.equals(cellGraph.getAgentPos()) &&
                    neighbour.equals(cellGraph.getInitialWallFollowPos()))
            {
                initialVertexFound = true;
            }
        }
        return neighbour;
    }

    public void updateNeighbouringVertices(BooleanCell agentCell)
    {
        tempAgentCell = agentCell;
        BooleanCell forwardCell = getNeighbourVertex((agentCell.getX() + direction.getX() * moveLength),
                                                     (agentCell.getY() + direction.getY() * moveLength),
                                                     direction.getAngle(),
                                                    false,
                                                    1);

        BooleanCell leftCell = getNeighbourVertex((agentCell.getX() + rotateAgentLeft().getX() * moveLength),
                                                  (agentCell.getY() + rotateAgentLeft().getY() * moveLength),
                                                  rotateAgentLeft().getAngle(),
                                                  false,
                                                  1);

        BooleanCell rightCell = getNeighbourVertex((agentCell.getX() + rotateAgentRight().getX() * moveLength),
                                                   (agentCell.getY() + rotateAgentRight().getY() * moveLength),
                                                    rotateAgentRight().getAngle(),
                                                   false,
                                                   1);

        cellGraph.updateAgentPos(agentCell,forwardCell,leftCell,rightCell);
        if (!noMovesDone)
        {
            exploreVerticesUntilObstacle(forwardCell, direction, 1);
            exploreVerticesUntilObstacle(leftCell, rotateAgentLeft(), 1);
            exploreVerticesUntilObstacle(rightCell, rotateAgentRight(), 1);
        }
    }

    /**
     * Explore all vertices in a direction until an object is encountered.
     * Explore here means agent sees the vertices (space in room) but does not actually move to them.
     * @param initVertex check the neighbouring vertex of this vertex
     * @param dir direction in which vertices are checked
     * @param distFromAgentVertex n-th vertex from agent's position vertex (e.g. neighbour of agent has dist = 1)
     */
    public void exploreVerticesUntilObstacle(BooleanCell initVertex, Vector dir, int distFromAgentVertex)
    {
        BooleanCell nextCell = getNeighbourVertex((initVertex.getX() + dir.getX() * moveLength),
                                                  (initVertex.getY() + dir.getY() * moveLength),
                                                  dir.getAngle(),
                                                  true,
                                                  distFromAgentVertex);
        cellGraph.addExploredVertex(nextCell);
        if (!nextCell.getObstacle() && distFromAgentVertex < maxViewingDistance)
        {
            exploreVerticesUntilObstacle(nextCell, dir, distFromAgentVertex+1);
        }
    }

    /**
     * Overloads {@code getNeighbourVertex()} with double values cast to int
     */
    public  BooleanCell getNeighbourVertex(double neighbourX,
                                           double neighbourY,
                                           double rayAngle,
                                           boolean exploring,
                                           int distFromAgentVertex)
    {
        return getNeighbourVertex((int) neighbourX, (int) neighbourY, rayAngle, exploring, distFromAgentVertex);
    }



    /**
     * Method for checking for walls/obstacles for getting next move in the wall following algorithm.
     * Walls/obstacles are checked in the direction of the given rayAngle by checking if that ray detects
     * an obstacle within the moveLength distance range.
     * @param rayAngle angle of the direction to be checked.
     * @return true if no obstacle detected; false if obstacle detected
     */
    public boolean noWallDetected(double rayAngle)
    {
        for (Ray r : view)
        {
            if ((r.angle() <= rayAngle + 1.0 && r.angle() >= rayAngle - 1.0) && r.rayLength() <= moveLength)
            {
                if (DEBUG)
                    System.out.print("WALL DETECTED! Ray Angle: " + rayAngle);
                return false;
            }
        }
        if (DEBUG)
            System.out.println("No wall detected with ray of angle: " + rayAngle);
        return true;
    }

    /**
     * Method for checking for walls/obstacles in a certain direction when exploring by "seeing",
     * i.e agent is not on next to that vertex but further away.
     * Walls/obstacles are checked in the direction of the given rayAngle by checking if that ray detects
     * an obstacle within the distance range of moveLength * distFromAgentVertex.
     * @param rayAngle angle of the direction to be checked.
     * @param distFromAgentVertex n-th vertex from agent's position vertex (e.g. neighbour of agent has dist = 1)
     * @return true if no obstacle detected; false if obstacle detected
     */
    public boolean noObstacleDetectedFromPos(double rayAngle, int distFromAgentVertex)
    {
        for (Ray r : view)
        {
            if (r.angle() <= rayAngle + 1.0 && r.angle() >= rayAngle - 1.0)
            {
                if (r.rayLength() <= (moveLength) * distFromAgentVertex)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public Vector rotateAgentLeft()
    {
        for (int i = 0; i <= directions.size(); i++)
        {
            if (direction.equals(directions.get(i)))
            {
                if (i == directions.size()-1)
                {
                    return directions.get(0);
                }
                else
                {
                    return directions.get(i+1);
                }
            }
        }
        throw new RuntimeException("None of the 4 cardinal directions were reached when rotating agent.");
    }

    public Vector rotateAgentRight()
    {
        for (int i = 0; i <= directions.size(); i++)
        {
            if (direction.equals(directions.get(i)))
            {
                if (i == 0)
                {
                    return directions.get(3);
                }
                else
                {
                    return directions.get(i-1);
                }
            }
        }
        throw new RuntimeException("None of the 4 cardinal directions were reached when rotating agent.");
    }

    public double getAngleOfLeftRay()
    {
        double currentAngle = direction.getAngle();
        if (currentAngle < 270)
        {
            return currentAngle + 90;
        }
        else
        {
            return currentAngle + 90 - 360;
        }
    }

    public BooleanCell getAgentNeighbourBasedOnAngle(double rayAngle)
    {
        BooleanCell agentCell = cellGraph.getAgentPos();
        if (rayAngle == direction.getAngle())
        {
            return getNeighbourVertex((agentCell.getX() + direction.getX() * moveLength),
                    (agentCell.getY() + direction.getY() * moveLength),
                    direction.getAngle(),
                    false,
                    1);
        }
        else if (rayAngle == rotateAgentLeft().getAngle())
        {
            return getNeighbourVertex((agentCell.getX() + rotateAgentLeft().getX() * moveLength),
                    (agentCell.getY() + rotateAgentLeft().getY() * moveLength),
                    rotateAgentLeft().getAngle(),
                    false,
                    1);
        }
        else if (rayAngle == rotateAgentRight().getAngle())
        {
            return getNeighbourVertex((agentCell.getX() + rotateAgentRight().getX() * moveLength),
                    (agentCell.getY() + rotateAgentRight().getY() * moveLength),
                    rotateAgentRight().getAngle(),
                    false,
                    1);
        }
        throw new RuntimeException("Checking vertex that is behind agent but cannot actually see behind!");
    }
}

