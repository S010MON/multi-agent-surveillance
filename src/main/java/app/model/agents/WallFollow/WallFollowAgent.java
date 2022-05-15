package app.model.agents.WallFollow;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.Move;
import app.model.TypeInformation;
import app.model.agents.AgentImp;
import app.model.agents.Cells.GraphCell;
import app.model.agents.Universe;
import app.model.Type;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

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
    @Setter private boolean DEBUG = true;
    @Getter @Setter private boolean movedForwardLast = false;
    @Getter @Setter private TurnType lastTurn = TurnType.NO_TURN;
    @Getter @Setter private boolean wallEncountered = false;
    public static Map map;
    private boolean initialVertexFound = false;  // pheromone 1
    private boolean noMovesDone = true;
    private boolean explorationDone = false;
    private GraphCell currentTargetVertex = null;
    private List<GraphCell> currentPathToNextVertex = null;
    private ArrayList<GraphCell> inaccessibleCells = new ArrayList<>();
    private ArrayList<Integer> horizontalWallsCovered = new ArrayList<>();  // pheromone 2
    private ArrayList<Integer> verticalWallsCovered = new ArrayList<>();  // pheromone 2
    private final List<Vector> directions = Arrays.asList(new Vector(0,1),
            new Vector(1,0),
            new Vector(0,-1),
            new Vector(-1,0));
    private ArrayList<GraphCell> lastPositions = new ArrayList<>();
    @Getter private GraphCell prevAgentVertex = null;

    public WallFollowAgent(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
        initializeWorld();
    }

    public WallFollowAgent(Vector position, Vector direction, double radius, Type type, double moveLen)
    {
        super(position, direction, radius, type);
        this.moveLength = moveLen;
        initializeWorld();
    }

    public void initializeWorld()
    {
        Universe.init(type, (int)moveLength);
        world = new WfWorld(Universe.getMemoryGraph(type));

        world.add_or_adjust_Vertex(position);
        lastPositions.add(world.getVertexAt(position));
        prevAgentVertex = world.getVertexAt(position);
        checkIfNeighboursAreObstacles();
    }

    /**
     * general algorithm:
        if last move failed
            handle failed move
        else if following path and no unexplored wall found
            get move based on path
        else if found unexplored wall while following path
            start wall following again
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
        if (!noMovesDone && !isMoveFailed())
        {
            updateGraphAfterSuccessfulMove();
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
        else if (wallEncountered && agentStuckInVertex(position))
        {
            if (DEBUG) {
                System.out.println("Agent stuck in vertex.");
            }
            //GraphCell forwardCell = getAgentNeighbourBasedOnAngle(direction.getAngle());
            GraphCell forwardCell = world.getVertexFromCurrent(world.getVertexAt(position),
                    world.getDirectionStr(direction.getAngle()));
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
        else if (currentPathToNextVertex != null && !foundUnexploredWallToFollow())
        {
            if (DEBUG) {
                System.out.println("Following path.");
            }
            Move pathMove = getMoveBasedOnPath();
            deltaPos = pathMove.getDeltaPos();
            newDirection = pathMove.getEndDir();
        }
        else if (currentPathToNextVertex != null && foundUnexploredWallToFollow())
        {
            currentPathToNextVertex = null;
            currentTargetVertex = null;
            wallEncountered = true;
            initialVertexFound = false;
            Move wallFollowMove = runWallFollowAlgorithm();
            deltaPos = wallFollowMove.getDeltaPos();
            newDirection = wallFollowMove.getEndDir();
        }
        else if (initialVertexFound || agentInStuckMovement())
        {
            if (DEBUG) {
                if (initialVertexFound) System.out.println("Found initial vertex");
                if (agentInStuckMovement()) System.out.println("Agent stuck in movement");
            }
            Move heuristicsMove = runHeuristicsAlgorithm();
            deltaPos = heuristicsMove.getDeltaPos();
            newDirection = heuristicsMove.getEndDir();
            wallEncountered = false;
        }
        else if (!wallEncountered) {
            GraphCell forwardCell = world.getVertexFromCurrent(world.getVertexAt(position),
                    world.getDirectionStr(direction.getAngle()));
            GraphCell leftCell = world.getVertexFromCurrent(world.getVertexAt(position),
                    world.getDirectionStr(getAngleOfLeftRay()));
            if (!noWallDetected(direction.getAngle()) && forwardCell.getObstacle())
            {
                if (DEBUG) {
                    System.out.println("ALGORITHM CASE 0: wall encountered in front!");
                }
                newDirection = rotateAgentRight();
                lastTurn = TurnType.RIGHT;
                movedForwardLast = false;
                wallEncountered = true;
                world.setInitialWallFollowPos(world.getVertexAt(position));
            }
            else if (!noWallDetected(getAngleOfLeftRay()) && leftCell.getObstacle())
            {
                if (DEBUG) {
                    System.out.println("ALGORITHM CASE 0: wall encountered on left!");
                }
                wallEncountered = true;
                world.setInitialWallFollowPos(world.getVertexAt(position));
            }
            else
            {
                if (DEBUG) {
                    System.out.println("ALGORITHM CASE 0: no wall encountered");
                }
                deltaPos = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
                movedForwardLast = true;
                lastTurn = TurnType.NO_TURN;
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
        noMovesDone = false;
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
        GraphCell forwardCell = world.getVertexFromCurrent(world.getVertexAt(position),
                world.getDirectionStr(direction.getAngle()));
        GraphCell leftCell = world.getVertexFromCurrent(world.getVertexAt(position),
                world.getDirectionStr(getAngleOfLeftRay()));
        if (lastTurn == TurnType.LEFT && noWallDetected(direction.getAngle()) && !forwardCell.getObstacle())
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 1"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            movedForwardLast = true;
            lastTurn = TurnType.NO_TURN;
        }
        else if (noWallDetected(getAngleOfLeftRay()) && !leftCell.getObstacle())
        {
            // TODO sth weird happens when having moved forward after making a turn
            if (DEBUG) { System.out.println("Obstacle on left: " + leftCell.getObstacle()); ; }
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
            markWallAsCovered();
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
        ArrayList<GraphCell> unexploredVertices = world.getVerticesWithUnexploredNeighbours();
        if (unexploredVertices.size() == 0)
        {
            explorationDone = true;
        }
        else
        {
            double minScore = 0;
            GraphCell minScoreVertex = null;
            for (GraphCell vertex : unexploredVertices)
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
                currentPathToNextVertex = DijkstraShortestPath.findPathBetween(world.G,
                        world.getVertexAt(position), currentTargetVertex).getVertexList();
                return getMoveBasedOnPath();
            }
            else
            {
                inaccessibleCells.clear();
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
            newDirection = rotateAgentRight();
            lastTurn = TurnType.RIGHT;
            movedForwardLast = false;
            wallEncountered = false;
            initialVertexFound = false;
        }
        else {
            throw new RuntimeException("Move failed but last move was not position change!");
        }
        return new Move(newDirection, deltaPos);
    }

    public double getVertexScore(GraphCell vertex)
    {
        // currently takes into account shortest path length, direction relative to agent's and
        // how many neighbours also have unexplored cells
        // TODO add weights to score components?
        double score;
        int shortestPathLength;
        List<GraphCell> shortestPath = DijkstraShortestPath.findPathBetween(world.G, world.getVertexAt(position), vertex).getVertexList();
        if (shortestPath != null)
        {
            shortestPathLength = shortestPath.size();
        }
        else
        {
            return 100000;
        }
        int neighboursOnUnexploredFrontier = 0;
        List<GraphCell> neighbours = Graphs.neighborListOf(world.G,vertex);
        for (GraphCell neighbour : neighbours)
        {
            if (!neighbour.getObstacle() && world.G.edgesOf(vertex).size() < 4)
            {
                neighboursOnUnexploredFrontier++;
            }
        }
        score = shortestPathLength;
        score = score / getDirectionScore(vertex);
        if (neighboursOnUnexploredFrontier != 0)
        {
            score = score / neighboursOnUnexploredFrontier;
        }

        return score;
    }

    public Move getMoveBasedOnPath()
    {
        GraphCell nextVertex = currentPathToNextVertex.get(0);
        if (nextVertex.equals(world.getVertexAt(position)))
        {
            currentPathToNextVertex.remove(nextVertex);
            nextVertex = currentPathToNextVertex.get(0);
        }
        Vector nextDir = world.G.getNeighbourDir(world.getVertexAt(position), nextVertex);
        if (nextDir.equals(direction))
        {
            double deltaX = nextVertex.getX() - world.getVertexAt(position).getX();
            double deltaY = nextVertex.getY() - world.getVertexAt(position).getY();
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

    public void updateGraphAfterSuccessfulMove()
    {
        updateLastPositions(world.getVertexAt(position));
        world.G.leaveVertex(prevAgentVertex.getPosition());
        world.add_or_adjust_Vertex(position);
        checkIfNeighboursAreObstacles();
    }

    public void checkIfNeighboursAreObstacles()
    {
        GraphCell agentCell = world.getVertexAt(position);
        for (String dir : world.getCardinalDirections().keySet())
        {
            GraphCell neighbour = world.getVertexFromCurrent(agentCell,dir);
            if (!noWallDetected(world.getCardinalDirections().get(dir).getAngle()))
            {
                neighbour.setObstacle(true);
            }
        }
    }

    public boolean agentStuckInVertex(Vector currentPos)
    {
        if (lastPositions.size() < 8)
        {
            return false;
        }
        for (GraphCell vertex : lastPositions)
        {
            if (vertex != world.getVertexAt(currentPos))
            {
                return false;
            }
        }
        return true;
    }

    public boolean agentInStuckMovement()
    {
        System.out.println("Last positions before checking stuck movement: " + lastPositions);
        ArrayList<GraphCell> diffVertices = new ArrayList<>();
        if (lastPositions.size() >= 24)
        {
            for (int i=0; i<lastPositions.size(); i++)
            {
                if(!diffVertices.contains(lastPositions.get(i)))
                {
                    diffVertices.add(lastPositions.get(i));
                }
                if(i == lastPositions.size() - 1 && diffVertices.size() < 5)
                {
                    return true;
                }
            }
        }
        System.out.println("Different vertices: " + diffVertices);
        return false;
    }

    public void updateLastPositions(GraphCell currentAgentCell)
    {
        if (lastPositions.size() >= 24) {
            lastPositions.remove(0);
        }
        if (lastPositions.size() > 0)
        {
            prevAgentVertex = lastPositions.get(lastPositions.size()-1);
        }
        lastPositions.add(currentAgentCell);
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
            if (TypeInformation.isSolid(r.getType()) && (r.angle() <= rayAngle + 1.0 && r.angle() >= rayAngle - 1.0) && r.length() <= moveLength)
            {
                if(r.getType()==Type.BORDER || r.getType()==Type.WALL)
                {
                    if(DEBUG)
                        System.out.println("WALL DETECTED! Ray Angle: " + rayAngle);
                    return false;
                }
            }
        }
        if (DEBUG)
            System.out.println("No wall detected with ray of angle: " + rayAngle);
        return true;
    }

    public void markWallAsCovered()
    {
        // TODO implement pheromone smell here?
        GraphCell forwardCell = world.getVertexFromCurrent(world.getVertexAt(position),
                world.getDirectionStr(direction.getAngle()));
        if (forwardCell.getX() == world.getVertexAt(position).getX())
        {
            horizontalWallsCovered.add(forwardCell.getX());
        }
        if (forwardCell.getY() == world.getVertexAt(position).getY())
        {
            verticalWallsCovered.add(forwardCell.getY());
        }
    }

    private boolean foundUnexploredWallToFollow()
    {
        GraphCell forwardCell = world.getVertexFromCurrent(world.getVertexAt(position),
                world.getDirectionStr(direction.getAngle()));
        return (!noWallDetected(direction.getAngle()) && forwardCell.getObstacle()
                && ((!horizontalWallsCovered.contains(forwardCell.getX()) && direction.getX() == 0)
                || (!verticalWallsCovered.contains(forwardCell.getY()) && direction.getY() == 0)));
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

    /**
     * Give vertex a direction score for heuristic's algorithm.
     * If vertex is towards the direction agent is currently facing at, give highest score.
     * If vertex is to the left or right of agent, give middle score.
     * If vertex is behind agent, give lowest score.
     * @param targetVertex vertex to give the score to.
     * @return the direction score (1 or 2 or 3)
     */
    public int getDirectionScore(GraphCell targetVertex)
    {
        Vector targetVector = new Vector(targetVertex.getX(),targetVertex.getY());
        double angle = targetVector.sub(position).getAngle();
        double agentAngle = direction.getAngle();
        if (agentAngle == 0)
        {
            if (angle >= 315 || angle <= 45)
            {
                return 3;
            }
            else if (angle >= 225 || angle <= 135)
            {
                return 2;
            }
            else
            {
                return 1;
            }
        }
        else if (angle >= agentAngle-45 && angle <= agentAngle+45)
        {
            return 3;
        }
        else if (angle >= agentAngle-135 && angle <= agentAngle+135)
        {
            return 2;
        }
        else
        {
            return 1;
        }
    }
}
