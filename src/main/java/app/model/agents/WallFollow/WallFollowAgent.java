package app.model.agents.WallFollow;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.AgentImp;
import app.model.agents.Cells.BooleanCell;
import app.model.Move;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.graph.DefaultEdge;

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
    @Getter @Setter private double moveLength = 10;
    @Setter private boolean DEBUG = false;
    @Getter @Setter private boolean wallEncountered = false;
    public static Map map;
    private app.model.agents.WallFollow.BooleanCellGraph<BooleanCell, DefaultEdge> cellGraph;
    private boolean initialVertexFound = false;
    private BooleanCell tempAgentCell;
    private boolean noMovesDone = true;

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
    }

    /**
     * pseudocode for simple wall following algorithm:

        if !wallEncountered
            if (no wall forward)
                go forward
            else
                turn 90 deg right
                set wallEcountered = true
        else if (turned left previously and forward no wall)
            go forward;
        else if (no wall at left)
            turn 90 deg left;
        else if (no wall forward)
            go forward;
        else
            turn 90 deg right;

     * based on: https://blogs.ntu.edu.sg/scemdp-201718s1-g14/exploration-algorithm/
     */
    @Override
    public Move move()
    {
        Vector newMove = new Vector(0,0);
        Vector newDirection = direction;

        if (initialVertexFound)
        {
            /*
            TODO: run a combination of Wall Following and Heuristic algorithm from here on:
             1. get list of vertices with less than 4 edges to them (the unexplored frontier)
             2. use heuristics to pick one to move to
             3. move there with Dijkstra's algorithm
             4. reset wallEncountered to false and initialWallFollowPos to null
             */
            if (DEBUG) {
                System.out.println("agent final vertex " + cellGraph.getAgentPos());
                System.out.println(cellGraph.getVerticesWithUnexploredNeighbours());
            }
            return new Move(newDirection, newMove);  // currently agent stops if found initial vertex
        }
        else if (!wallEncountered)
        {
            if (noWallDetected(direction.getAngle()))
            {
                if (DEBUG) { System.out.println("ALGORITHM CASE 0: no wall encountered"); }
                newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
                movedForwardLast = true;
                lastTurn = TurnType.NO_TURN;
            }
            else
            {
                if (DEBUG) { System.out.println("ALGORITHM CASE 0: wall encountered!"); }
                newDirection = rotateAgentLeft(false);
                lastTurn = TurnType.RIGHT;
                movedForwardLast = false;
                wallEncountered = true;
                cellGraph.setInitialWallFollowPos(cellGraph.getAgentPos());
            }
        }
        else if (lastTurn == TurnType.LEFT && noWallDetected(direction.getAngle()))
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 1"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            movedForwardLast = true;
            lastTurn = TurnType.NO_TURN;
        }
        else if (noWallDetected(getAngleOfLeftRay()))
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 2"); }
            newDirection = rotateAgentLeft(true);
            lastTurn = TurnType.LEFT;
            movedForwardLast = false;
        }
        else if (noWallDetected(direction.getAngle()))
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 3"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            movedForwardLast = true;
            lastTurn = TurnType.NO_TURN;
        }
        else
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 4"); }
            newDirection = rotateAgentLeft(false);
            lastTurn = TurnType.RIGHT;
            movedForwardLast = false;
        }
        direction = newDirection;  // need to update direction in GameEngine instead?
        Move move = new Move(newDirection, newMove);
        updateGraph();
        if (DEBUG)
        {
            System.out.println("Did move: " + move.getDeltaPos().getX() + " " + move.getDeltaPos().getY());
            System.out.println(cellGraph.getVertices());
        }
        return move;
    }

    public void updateNeighbouringVertices(BooleanCell agentCell)
    {
        tempAgentCell = agentCell;
        BooleanCell forwardCell = getNeighbourVertex((int) (agentCell.getX() + direction.getX()*moveLength),
                (int) (agentCell.getY() + direction.getY()*moveLength), direction.getAngle(), false, 1);
        BooleanCell leftCell = getNeighbourVertex((int) (agentCell.getX() + rotateAgentLeft(true).getX()*moveLength),
                (int) (agentCell.getY() + rotateAgentLeft(true).getY()*moveLength),
                rotateAgentLeft(true).getAngle(), false, 1);
        BooleanCell rightCell = getNeighbourVertex((int) (agentCell.getX() + rotateAgentLeft(false).getX()*moveLength),
                (int) (agentCell.getY() + rotateAgentLeft(false).getY()*moveLength),
                rotateAgentLeft(false).getAngle(), false, 1);
        cellGraph.updateAgentPos(agentCell,forwardCell,leftCell,rightCell);
        if (!noMovesDone)
        {
            exploreVerticesUntilObstacle(forwardCell, direction, 1);
            exploreVerticesUntilObstacle(leftCell, rotateAgentLeft(true), 1);
            exploreVerticesUntilObstacle(rightCell, rotateAgentLeft(false), 1);
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
        BooleanCell nextCell = getNeighbourVertex((int) (initVertex.getX() + dir.getX()*moveLength),
                (int) (initVertex.getY() + dir.getY()*moveLength), dir.getAngle(), true, distFromAgentVertex);
        cellGraph.addExploredVertex(nextCell);
        if (!nextCell.getObstacle())
        {
            exploreVerticesUntilObstacle(nextCell, dir, distFromAgentVertex+1);
        }
        return;
    }

    public BooleanCell getNeighbourVertex(int neighbourX, int neighbourY, double rayAngle, boolean exploring,
                                          int distFromAgentVertex)
    {
        BooleanCell neighbour;
        if (!cellGraph.getVertices().containsKey(neighbourX + " " + neighbourY))
        {
            neighbour = new BooleanCell(neighbourX, neighbourY);
            // TODO differentiate between obstacle and occupied cell
            // TODO write a new method for checking intersection with boundaries
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
                    rayAngle == direction.getAngle() && !tempAgentCell.equals(cellGraph.getAgentPos())
                    && neighbour.equals(cellGraph.getInitialWallFollowPos()))
            {
                initialVertexFound = true;
            }
        }
        return neighbour;
    }

    public void updateGraph()
    {
        if (getLastTurn() != TurnType.NO_TURN && !movedForwardLast)
        {
            BooleanCell cellToUpdate = null;
            if (lastTurn == TurnType.LEFT)
            {
                cellToUpdate = getNeighbourVertex((int) (cellGraph.getAgentPos().getX()
                                + rotateAgentLeft(true).getX()*moveLength), (int) (cellGraph.getAgentPos().getY()
                                + rotateAgentLeft(true).getY()*moveLength),
                        rotateAgentLeft(true).getAngle(), false, 1);
            }
            else if (lastTurn == TurnType.RIGHT)
            {
                cellToUpdate = getNeighbourVertex((int) (cellGraph.getAgentPos().getX()
                                + rotateAgentLeft(false).getX()*moveLength), (int) (cellGraph.getAgentPos().getY()
                                + rotateAgentLeft(false).getY()*moveLength),
                        rotateAgentLeft(false).getAngle(), false, 1);
            }
            if (cellToUpdate != null)
            {
                cellGraph.updateVertex(cellToUpdate);
            }
        }
        else if (movedForwardLast)
        {
            BooleanCell newAgentCell = getNeighbourVertex((int) (cellGraph.getAgentPos().getX() + direction.getX()*moveLength),
                    (int) (cellGraph.getAgentPos().getY() + direction.getY()*moveLength), direction.getAngle(), false, 1);
            updateNeighbouringVertices(newAgentCell);
        }
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
            // TODO need to deal with edge case for 0 = 360 degrees and area around that
            if ((r.angle() <= rayAngle + 1.0 && r.angle() >= rayAngle - 1.0) && r.rayLength() <= moveLength)
            {
                if (DEBUG)
                {
                    System.out.print("WALL DETECTED! Angle of detecting ray: " + rayAngle + " and ray length: ");
                    System.out.println(r.rayLength());
                }
                return false;
            }
        }
        if (DEBUG)
        {
            System.out.println("No wall detected with ray of angle: " + rayAngle);
        }
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
                if (r.rayLength() <= moveLength*distFromAgentVertex)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean obstacleDetected(Vector direction)
    {
        // TODO use BoundaryImp methods to check if ray finds obstacle
        return false;
    }

    public Vector rotateAgentLeft(boolean rotateLeft)
    {
        // if rotateLeft is true, rotate agent 90 degrees left
        // if rotateLeft id false, rotate agent 90 degrees right
        List<Vector> directions = Arrays.asList(new Vector(0,1),
                new Vector(1,0),
                new Vector(0,-1),
                new Vector(-1,0));
        for (int i=0; i<=directions.size(); i++)
        {
            if (direction.equals(directions.get(i)) && !rotateLeft)
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
            else if (direction.equals(directions.get(i)) && rotateLeft)
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
}
