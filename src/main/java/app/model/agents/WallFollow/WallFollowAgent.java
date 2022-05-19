package app.model.agents.WallFollow;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Angle;
import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.agents.Cells.GraphCell;
import app.model.agents.Universe;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.GraphPath;
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
    @Setter private boolean DEBUG = false;
    @Getter @Setter private boolean movedForwardLast = false;
    @Getter @Setter private TurnType lastTurn = TurnType.NO_TURN;
    @Getter @Setter private boolean wallEncountered = false;
    public static Map map;
    private boolean initialVertexFound = false;
    private boolean noMovesDone = true;
    private boolean explorationDone = false;
    private GraphCell currentTargetVertex = null;
    private List<GraphCell> currentPathToNextVertex = null;
    private ArrayList<GraphCell> inaccessibleCells = new ArrayList<>();
    private final List<Vector> directions = Arrays.asList(new Vector(0,1),
            new Vector(1,0),
            new Vector(0,-1),
            new Vector(-1,0));
    private ArrayList<GraphCell> lastPositions = new ArrayList<>();
    @Getter private GraphCell prevAgentVertex = null;
    private boolean hasLeftInitialWallFollowPos = false;
    private GraphCell initialWallFollowPos = null;
    protected double directionHeuristicWeight = 1;
    private final double epsilon = 0.5;

    /** Original WallFollow agent that switches between following a wall and doing heuristics exploration,
     * depending on if it finds an unexplored wall to follow.
     * Compared to other variants of WF, gives lowest weight to direction in heuristics part. */
    public WallFollowAgent(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
        this.direction = closestCardinalDirection(direction.getAngle());
        initializeWorld();
    }

    public WallFollowAgent(Vector position, Vector direction, double radius, Type type, double moveLen)
    {
        super(position, direction, radius, type);
        this.direction = closestCardinalDirection(direction.getAngle());
        this.moveLength = moveLen;
        initializeWorld();
    }

    public WallFollowAgent(Agent other)
    {
        super(other.getPosition(), other.getDirection(), other.getRadius(), other.getType());
        this.direction = closestCardinalDirection(direction.getAngle());
        this.moveLength = other.getMoveLength();
        copyOver(other);

        world = new WfWorld(world.getG());
        world.add_or_adjust_Vertex(position);
        lastPositions.add(world.getVertexAt(position));
        prevAgentVertex = world.getVertexAt(position);
        world.add_or_adjust_Vertex(position);
    }

    public void initializeWorld()
    {
        Universe.init(type, (int)moveLength);
        world = new WfWorld(Universe.getMemoryGraph(type));
        world.add_or_adjust_Vertex(position);
        lastPositions.add(world.getVertexAt(position));
        prevAgentVertex = world.getVertexAt(position);
        world.add_or_adjust_Vertex(position);
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
        if (!hasLeftInitialWallFollowPos && initialWallFollowPos != null &&
                !initialWallFollowPos.equals(world.getVertexAt(position)))
        {
            hasLeftInitialWallFollowPos = true;
        }
        if (!initialVertexFound && hasLeftInitialWallFollowPos && initialWallFollowPos != null &&
                initialWallFollowPos.equals(world.getVertexAt(position)))
        {
            initialVertexFound = true;
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
            hasLeftInitialWallFollowPos = false;
        }
        else if (currentPathToNextVertex != null && !foundUnexploredWallToFollow())
        {
            Move pathMove = getMoveBasedOnPath();
            deltaPos = pathMove.getDeltaPos();
            newDirection = pathMove.getEndDir();
        }
        else if ((currentPathToNextVertex != null || initialVertexFound) && foundUnexploredWallToFollow())
        {
            currentPathToNextVertex = null;
            currentTargetVertex = null;
            Move wallFollowMove = runWallFollowAlgorithm();
            deltaPos = wallFollowMove.getDeltaPos();
            newDirection = wallFollowMove.getEndDir();
            wallEncountered = true;
            initialVertexFound = false;
            hasLeftInitialWallFollowPos = false;
            initialWallFollowPos = world.getVertexAt(position);
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
            hasLeftInitialWallFollowPos = false;
        }
        else if (!wallEncountered) {
            GraphCell forwardCell = world.getVertexFromCurrent(world.getVertexAt(position),
                    world.getDirectionStr(direction.getAngle()));
            GraphCell leftCell = world.getVertexFromCurrent(world.getVertexAt(position),
                    world.getDirectionStr(getAngleOfLeftRay()));
            if (!noWallDetected(direction.getAngle()) /*&& forwardCell != null*/ && forwardCell.getObstacle())
            {
                if (DEBUG) {
                    System.out.println("ALGORITHM CASE 0: wall encountered in front!");
                }
                // TODO: check if the wall encountered is already being covered by someone else?
                newDirection = rotateAgentRight();
                lastTurn = TurnType.RIGHT;
                movedForwardLast = false;
                wallEncountered = true;
                initialWallFollowPos = world.getVertexAt(position);
            }
            else if (!noWallDetected(getAngleOfLeftRay()) /*&& leftCell != null*/ && leftCell.getObstacle())
            {
                if (DEBUG) {
                    System.out.println("ALGORITHM CASE 0: wall encountered on left!");
                }
                wallEncountered = true;
                initialWallFollowPos = world.getVertexAt(position);
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
        else if (no wall at left and already following a wall)
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
        else if (noWallDetected(getAngleOfLeftRay()) && !leftCell.getObstacle() && wallEncountered)
        {
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
            GraphCell cell = world.getVertexFromCurrent(world.getVertexAt(position),
                    world.getDirectionStr(direction.getAngle()));
            world.markWallAsCovered(cell,position);
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
            double bestScore = 0;
            GraphCell bestScoreVertex = null;
            for (GraphCell vertex : unexploredVertices)
            {
                if (!inaccessibleCells.contains(vertex))
                {
                    double score = getVertexScore(vertex);
                    if (bestScore == 0 || score < bestScore)
                    {
                        bestScore = score;
                        bestScoreVertex = vertex;
                    }
                }
            }
            if (bestScoreVertex != null)
            {
                currentTargetVertex = bestScoreVertex;
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
        return new Move(newDirection, deltaPos);
    }

    /** Takes into account shortest path length, direction relative to agent's
     * and how many neighbours also have unexplored cells.
     * Original WF agent gives low weight to keeping in current direction, WFDirHeuristicMedWeight gives more weight
     * and WFDirHeuristicHighWeight gives most weight to that.
     */
    public double getVertexScore(GraphCell vertex)
    {
        double score;
        double shortestPathLength;
        GraphPath dijkstrasPath = DijkstraShortestPath.findPathBetween(world.G, world.getVertexAt(position), vertex);
        if (dijkstrasPath != null)
        {
            shortestPathLength = dijkstrasPath.getVertexList().size();
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
            boolean removed = currentPathToNextVertex.remove(nextVertex);
            if(removed && !(currentPathToNextVertex instanceof ArrayList<?>))
                System.out.println(removed);
            if(!removed)
            {
                System.out.println("not removed");
            }
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
        if (prevAgentVertex != null)
        {
            world.G.leaveVertex(prevAgentVertex.getPosition());
        }
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
                /*
                if(neighbour == null)
                    return;
                 */
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
        ArrayList<GraphCell> diffVertices = new ArrayList<>();
        if (lastPositions.size() >= 24)
        {
            for (int i=0; i<lastPositions.size(); i++)
            {
                if (lastPositions.get(i) != null)
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
        }
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
        double anglePrecision = 2;
        for (Ray r : view)
        {
            if (Angle.angleInRange(r.angle(),rayAngle+anglePrecision, rayAngle-anglePrecision))
            {
                if (r.length() <= moveLength)
                {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean foundUnexploredWallToFollow()
    {
        GraphCell forwardCell = world.getVertexFromCurrent(world.getVertexAt(position),
                world.getDirectionStr(direction.getAngle()));
        return ((!noWallDetected(direction.getAngle()) || forwardCell.getObstacle())
                && ((!world.getHorizontalWallsCovered().contains(forwardCell.getX()) && direction.getX() == 0)
                || (!world.getVerticalWallsCovered().contains(forwardCell.getY()) && direction.getY() == 0)));
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
    public double getDirectionScore(GraphCell targetVertex)
    {
        Vector targetVector = new Vector(targetVertex.getX(),targetVertex.getY());
        double angle = targetVector.sub(position).getAngle();
        double agentAngle = direction.getAngle();
        if (agentAngle == 0)
        {
            if (angle >= 315 || angle <= 45)
            {
                return 3 * directionHeuristicWeight;
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
            return 3 * directionHeuristicWeight;
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

    private Vector closestCardinalDirection(double angle)
    {
        Vector direction = directions.get(0);
        double smallestDiff = 360;
        for(Vector dir: directions)
        {
            double diff = Math.abs(dir.getAngle() - angle);
            if(diff < smallestDiff)
            {
                smallestDiff = diff;
                direction = dir;
            }
        }
        return direction;
    }
}
