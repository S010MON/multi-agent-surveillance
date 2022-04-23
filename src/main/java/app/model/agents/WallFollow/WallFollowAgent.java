package app.model.agents.WallFollow;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.Move;
import app.model.agents.AgentImp;
import app.model.agents.Cells.GraphCell;
import app.model.agents.Team;
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
    @Getter @Setter private double moveLength = 20;
    @Getter @Setter private boolean wallEncountered = false;
    //private int maxViewingDistance = 10;
    public static Map map;
    //@Getter private final app.model.agents.WallFollow.BooleanCellGraph<BooleanCell, DefaultEdge> cellGraph;
    private boolean initialVertexFound = false;  // pheromone 1
    private GraphCell tempAgentCell;
    //private boolean agentNotInitialized = true;
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

    public WallFollowAgent(Vector position, Vector direction, double radius, Team team)
    {
        super(position, direction, radius, team);
        /*
        cellGraph = new app.model.agents.WallFollow.BooleanCellGraph<>();
        cellGraph.setEdge((int)moveLength);
        BooleanCell agentInitialVertex = new BooleanCell(0,0);
        cellGraph.addVertex(agentInitialVertex);
        cellGraph.getVertices().put(agentInitialVertex.toString(),agentInitialVertex);
        updateNeighbouringVertices(agentInitialVertex);
        agentNotInitialized = false;
         */
        initializeWorld();
    }

    public WallFollowAgent(Vector position, Vector direction, double radius, Team team, double moveLen)
    {
        super(position, direction, radius, team);
        this.moveLength = moveLen;
        /* cellGraph = new app.model.agents.WallFollow.BooleanCellGraph<>();
        cellGraph.setEdge((int)moveLength);
        BooleanCell agentInitialVertex = new BooleanCell(0,0);
        tempAgentCell = agentInitialVertex;
        cellGraph.addVertex(agentInitialVertex);
        updateNeighbouringVertices(agentInitialVertex);
         */
        initializeWorld();
    }

    private void initializeWorld()
    {
        if(world == null)
        {
            world = new WfWorld<>((int)moveLength);
        }
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
        //System.out.println("Doing move: " + deltaPos);
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
                currentPathToNextVertex = DijkstraShortestPath.findPathBetween(world,
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
        List<GraphCell> shortestPath = DijkstraShortestPath.findPathBetween(world, world.getVertexAt(position), vertex).getVertexList();
        if (shortestPath != null)
        {
            shortestPathLength = shortestPath.size();
        }
        else
        {
            return 100000;
        }
        int neighboursOnUnexploredFrontier = 0;
        List<GraphCell> neighbours = Graphs.neighborListOf(world,vertex);
        for (GraphCell neighbour : neighbours)
        {
            if (!neighbour.getObstacle() && world.edgesOf(vertex).size() < 4)
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
        Vector nextDir = world.getNeighbourDir(world.getVertexAt(position), nextVertex);
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
        //System.out.println("Current agent vertex: " + world.getVertexAt(position));
        //System.out.println("Previous agent vertex: " + prevAgentVertex);
        world.leaveVertex(prevAgentVertex.getPosition());
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
                    //System.out.println("Last positions are: " + lastPositions);
                    return true;
                }
            }
            /*for (GraphCell vertex : lastPositions)
            {
                for (GraphCell other: lastPositions)
                {
                    if (!diffVertices.contains(other))
                    {
                        diffVertices.add(other);
                        if (diffVertices.indexOf(other) == lastPositions.size()-1 && diffVertices.size() < 5)
                        {
                            System.out.println("Last positions are: " + lastPositions);
                            return true;
                        }
                    }
                    if (!vertex.equals(other))
                    {
                        if (Math.abs(vertex.getX() - other.getX()) > moveLength
                                || Math.abs(vertex.getY() - other.getY()) > moveLength)
                        {
                            return false;
                        }
                    }
                }
            }
            //System.out.println("Stuck movement: problem with distance between vertices");
            //System.out.println("The last vertices were: " + lastPositions);
            //return true;*/
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

    /*
    public void updateGraph()
    {
        int agentX = world.getVertexAt(position).getX();
        int agentY = world.getVertexAt(position).getY();

        if (getLastTurn() != TurnType.NO_TURN && !movedForwardLast)
        {
            world.updateLastPositions();
            GraphCell cellToUpdate = null;
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
            GraphCell newAgentCell = world.getVertexFromCurrent(world.getVertexAt(position),
                    world.getDirectionStr(direction.getAngle()));;
            updateNeighbouringVertices(newAgentCell);
        }
    }
     */

    /*
    public GraphCell getNeighbourVertex(int neighbourX,
                                          int neighbourY,
                                          double rayAngle,
                                          boolean exploring,
                                          int distFromAgentVertex)
    {
        GraphCell neighbour;
        if (!world.getVertices().containsKey(neighbourX + " " + neighbourY))
        {
            neighbour = new GraphCell(neighbourX, neighbourY);  // TODO need to fix this or memorygraph already has this?
            // TODO differentiate between obstacle and occupied cell?
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
            neighbour = world.getVertices().get(neighbourX + " " + neighbourY);
            if (world.getInitialWallFollowPos() != null &&
                    rayAngle == direction.getAngle() &&
                    !tempAgentCell.equals(world.getVertexAt(position)) &&
                    neighbour.equals(world.getInitialWallFollowPos()))
            {
                initialVertexFound = true;
            }
        }
        return neighbour;
    }
     */

    /*
    public void updateNeighbouringVertices(GraphCell agentCell)
    {
        tempAgentCell = agentCell;
        GraphCell forwardCell = getNeighbourVertex((agentCell.getX() + direction.getX() * moveLength),
                                                     (agentCell.getY() + direction.getY() * moveLength),
                                                     direction.getAngle(),
                                                    false,
                                                    1);

        GraphCell leftCell = getNeighbourVertex((agentCell.getX() + rotateAgentLeft().getX() * moveLength),
                                                  (agentCell.getY() + rotateAgentLeft().getY() * moveLength),
                                                  rotateAgentLeft().getAngle(),
                                                  false,
                                                  1);

        GraphCell rightCell = getNeighbourVertex((agentCell.getX() + rotateAgentRight().getX() * moveLength),
                                                   (agentCell.getY() + rotateAgentRight().getY() * moveLength),
                                                    rotateAgentRight().getAngle(),
                                                   false,
                                                   1);

        //cellGraph.updateAgentPos(agentCell,forwardCell,leftCell,rightCell);
        world.add_or_adjust_Vertex(position);
        // TODO not exploring further than direct neighbours in shared graph
        if (!agentNotInitialized)
        {
            exploreVerticesUntilObstacle(forwardCell, direction, 1);
            exploreVerticesUntilObstacle(leftCell, rotateAgentLeft(), 1);
            exploreVerticesUntilObstacle(rightCell, rotateAgentRight(), 1);
        }
    }*/

    // TODO we don't explore further than one vertex in the shared graph?
    /**
     * Explore all vertices in a direction until an object is encountered.
     * Explore here means agent sees the vertices (space in room) but does not actually move to them.
     * @param initVertex check the neighbouring vertex of this vertex
     * @param dir direction in which vertices are checked
     * @param distFromAgentVertex n-th vertex from agent's position vertex (e.g. neighbour of agent has dist = 1)
     */
    /*
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
     */

    /**
     * Overloads {@code getNeighbourVertex()} with double values cast to int
     */
    /*
    public  GraphCell getNeighbourVertex(double neighbourX,
                                           double neighbourY,
                                           double rayAngle,
                                           boolean exploring,
                                           int distFromAgentVertex)
    {
        return getNeighbourVertex((int) neighbourX, (int) neighbourY, rayAngle, exploring, distFromAgentVertex);
    }
     */



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
            if ((r.angle() <= rayAngle + 1.0 && r.angle() >= rayAngle - 1.0) && r.length() <= moveLength)
            {
                if (DEBUG)
                    System.out.println("WALL DETECTED! Ray Angle: " + rayAngle);
                return false;
            }
        }
        if (DEBUG)
            System.out.println("No wall detected with ray of angle: " + rayAngle);
        return true;
    }

    /*
    /**
     * Method for checking for walls/obstacles in a certain direction when exploring by "seeing",
     * i.e agent is not on next to that vertex but further away.
     * Walls/obstacles are checked in the direction of the given rayAngle by checking if that ray detects
     * an obstacle within the distance range of moveLength * distFromAgentVertex.
     * @param rayAngle angle of the direction to be checked.
     * @param distFromAgentVertex n-th vertex from agent's position vertex (e.g. neighbour of agent has dist = 1)
     * @return true if no obstacle detected; false if obstacle detected

    public boolean noObstacleDetectedFromPos(double rayAngle, int distFromAgentVertex)
    {
        for (Ray r : view)
        {
            if (r.angle() <= rayAngle + 1.0 && r.angle() >= rayAngle - 1.0)
            {
                if (r.length() <= (moveLength) * distFromAgentVertex)
                {
                    return false;
                }
            }
        }
        return true;
    }*/

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

    /*
    public GraphCell getAgentNeighbourBasedOnAngle(double rayAngle)
    {
        GraphCell agentCell = world.getVertexAt(position);

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
     */

    /**
     * Give vertex a direction score for heuristic's algorithm.
     * If vertex is towards the direction agent is currently facing at, give highest score.
     * If vertex is to the left or right of agent, give middle score.
     * If vertex is bbehing agent, give lowest score.
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

