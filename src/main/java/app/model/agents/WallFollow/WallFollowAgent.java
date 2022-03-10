package app.model.agents.WallFollow;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.AgentImp;
import app.model.agents.Cells.BooleanCell;
import app.model.map.Move;
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
    private boolean movedForwardLast = false; // false if position didn't change and/or if only direction changed
    private TurnType lastTurn = TurnType.NO_TURN;
    private double moveLength = 5;  // equal to obstacleDetectionDistance as only care about walls within move range
    private boolean DEBUG = false;
    private boolean wallEncountered = false;
    public static Map map;
    //private WallFollowCellMap cellMap;
    private BooleanCellGraph<BooleanCell, DefaultEdge> cellGraph;
    private boolean alreadyExploredVertexFound = false;
    private BooleanCell tempAgentCell;

    public WallFollowAgent(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
        cellGraph = new BooleanCellGraph<>();
        BooleanCell agentInitialVertex = new BooleanCell(0,0);
        cellGraph.addVertex(agentInitialVertex);
        cellGraph.getVertices().put(agentInitialVertex.getXY(),agentInitialVertex);
        cellGraph.setInitialPos(agentInitialVertex);  // not sure if will need it in the end
        updateNeighbouringVertices(agentInitialVertex);
    }

    public WallFollowAgent(Vector position, Vector direction, double radius, Map map)
    {
        super(position, direction, radius);
        WallFollowAgent.map = map;
        //cellMap = new WallFollowCellMap(map.getSettings().getHeight(),map.getSettings().getWidth());
        //cellMap.updateAgentPos(this.position);

        cellGraph = new BooleanCellGraph<>();
        BooleanCell agentInitialVertex = new BooleanCell(0,0);
        tempAgentCell = agentInitialVertex;
        cellGraph.addVertex(agentInitialVertex);
        cellGraph.setInitialPos(agentInitialVertex);  // not sure if will need it in the end
        updateNeighbouringVertices(agentInitialVertex);
    }

    @Override
    public Move move()
    {
        /*
        pseudocode from: https://blogs.ntu.edu.sg/scemdp-201718s1-g14/exploration-algorithm/
        pseudocode for simple (left) wall following algorithm:

        CASE 0: if !wallEncountered
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

        NB! Agent currently only does wall following, but does not cover unexplored areas in middle!
        */

        // agent always faces in the direction of the last move, i.e. last move was left, agent direction is left
        // so "going forward" means going in the direction where agent is facing
        Vector newMove = new Vector(0,0);
        Vector newDirection = direction;

        // check if found already initially explored vertex where agent should move next
        // TODO this step is not working properly yet, as it cannot just check what would be the next forward cell
        if (alreadyExploredVertexFound)
        {
            if (DEBUG) { System.out.println("found already explored vertex"); }
            // TODO run Heuristic algorithm from here on
            return new Move(newDirection, newMove);  // stop if found already explored vertex
        }
        else if (!wallEncountered)
        {
            if (noWallDetected(direction.getAngle()))
            {
                //if (!cellMap.getAgentLoc().getNextToMapEdge()) System.out.println("not on map edge");
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
        // Vector newPosition = position.add(newMove);
        direction = newDirection;
        // TODO need to to update of direction somewhere else (in GameEngine tick)
        Move move = new Move(newDirection, newMove);
        updateGraph(move);
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
        BooleanCell forwardCell = getNeighbourVertex((int) (agentCell.getX() + direction.getX()),
                (int) (agentCell.getY() + direction.getY()), direction.getAngle());
        BooleanCell leftCell = getNeighbourVertex((int) (agentCell.getX() + rotateAgentLeft(true).getX()),
                (int) (agentCell.getY() + rotateAgentLeft(true).getY()),
                rotateAgentLeft(true).getAngle());
        BooleanCell rightCell = getNeighbourVertex((int) (agentCell.getX() + rotateAgentLeft(false).getX()),
                (int) (agentCell.getY() + rotateAgentLeft(false).getY()),
                rotateAgentLeft(false).getAngle());
        cellGraph.updateAgentPos(agentCell,forwardCell,leftCell,rightCell);
    }

    public BooleanCell getNeighbourVertex(int neighbourX, int neighbourY, double rayAngle)
    {
        if (!cellGraph.getVertices().containsKey(neighbourX + " " + neighbourY))
        {
            BooleanCell neighbour = new BooleanCell(neighbourX, neighbourY);
            // TODO differentiate between obstacle and occupied cell
            // TODO write a new method for checking intersection with boundaries
            if (!noWallDetected(rayAngle))
            {
                neighbour.setObstacle(true);
            }
            return neighbour;
        }
        else
        {
            // check if the next supposed move (forward) cell is already explored
            // TODO this step is not working properly yet, as it cannot just check what would be the next forward cell
            if (rayAngle == direction.getAngle() && !tempAgentCell.equals(cellGraph.getAgentPos()))
            {
                alreadyExploredVertexFound = true;
            }
            return cellGraph.getVertices().get(neighbourX + " " + neighbourY);
        }
    }

    public void updateGraph(Move move)
    {
        // if move is rotation
        if (move.getEndDir() != direction && move.getDeltaPos().length() == 0)
        {
            BooleanCell cellToUpdate = null;
            // if rotate left
            if (move.getEndDir().getX() == rotateAgentLeft(true).getX()
                    && move.getEndDir().getY() == rotateAgentLeft(true).getY())
            {
                cellToUpdate = getNeighbourVertex((int) (cellGraph.getAgentPos().getX()
                        + rotateAgentLeft(true).getX()), (int) (cellGraph.getAgentPos().getY()
                        + rotateAgentLeft(true).getY()), rotateAgentLeft(true).getAngle());
            }
            // if rotate right
            else if (move.getEndDir().getX() == rotateAgentLeft(false).getX()
                    && move.getEndDir().getY() == rotateAgentLeft(false).getY())
            {
                cellToUpdate = getNeighbourVertex((int) (cellGraph.getAgentPos().getX()
                        + rotateAgentLeft(false).getX()), (int) (cellGraph.getAgentPos().getY()
                        + rotateAgentLeft(false).getY()), rotateAgentLeft(false).getAngle());
            }
            if (cellToUpdate != null)
            {
                cellGraph.updateVertex(cellToUpdate);
            }
        }
        // if move is change in position
        else if (move.getDeltaPos().length() != 0)
        {
            // agent's new cell/vertex will be the vertex ahead (forward cell)
            BooleanCell newAgentCell = getNeighbourVertex((int) (cellGraph.getAgentPos().getX() + direction.getX()),
                    (int) (cellGraph.getAgentPos().getY() + direction.getY()), direction.getAngle());
            updateNeighbouringVertices(newAgentCell);
        }
    }

    public boolean noWallDetected(double rayAngle)
    {
        // if the ray with given rayAngle (+- 1.0 degrees) detects an obstacle in the moveLength distance range -> return false
        for (Ray r : view)
        {
            // TODO need to deal with edge case for 0 = 360 degrees and area around that
            if ((r.angle() <= rayAngle + 1.0 && r.angle() >= rayAngle - 1.0) && r.rayLength() < moveLength) {
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
        // should never reach here when only 4 directions are possible
        return direction;
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

    public void setMoveLength(double moveLength)
    {
        this.moveLength = moveLength;
    }

    public void setLastTurn(TurnType turn)
    {
        lastTurn = turn;
    }

    public void setMovedForwardLast(boolean movedForward)
    {
        movedForwardLast = movedForward;
    }

    public TurnType getLastTurn()
    {
        return lastTurn;
    }

    public boolean isMovedForwardLast()
    {
        return movedForwardLast;
    }

    public void setDEBUG(boolean debug) {
        this.DEBUG = debug;
    }

    public void setDirection(Vector dir)
    {
        direction = dir;
    }

    public void setWallEncountered(boolean wallEncountered)
    {
        this.wallEncountered = wallEncountered;
    }

    public boolean isWallEncountered() {
        return wallEncountered;
    }
}
