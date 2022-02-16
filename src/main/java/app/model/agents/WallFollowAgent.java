package app.model.agents;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;

import java.util.ArrayList;
import java.util.Arrays;

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
    private double moveLength = 1;  // equal to obstacleDetectionDistance as only care about walls within move range
    private boolean DEBUG = false;

    public WallFollowAgent(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
    }

    @Override

    public Vector move()
    {
        // pseudocode from: https://blogs.ntu.edu.sg/scemdp-201718s1-g14/exploration-algorithm/
        // pseudocode for simple (left) wall following algorithm:
//        if (turned left previously and forward no wall)
//            go forward;
//        else if (no wall at left)
//            turn 90 deg left;
//        else if (no wall forward)
//            go forward;
//        else
//            turn 90 deg right;

        // agent always faces in the direction of the last move, i.e. last move was left, agent direction is left
        // so "going forward" means going in the direction where agent is facing
        Vector newMove = new Vector(0,0);
        if (lastTurn == TurnType.LEFT && noWallDetected(direction.getAngle()))
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 1"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            movedForwardLast = true;
            lastTurn = TurnType.NO_TURN;
        }
        else if (noWallDetected(getAngleOfLeftRay()))
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 2"); }
            direction = rotateAgentLeft(true);
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
            direction = rotateAgentLeft(false);
            movedForwardLast = false;
        }
        Vector newPosition = position.add(newMove);
        return newPosition;
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

    public Vector rotateAgentLeft(boolean rotateLeft)
    {
        // if rotateLeft is true, rotate agent 90 degrees left
        // if rotateLeft id false, rotate agent 90 degrees right
        ArrayList<Vector> directions = new ArrayList<>(Arrays.asList(new Vector(0,1),
                new Vector(1,0), new Vector(0,-1), new Vector(-1,0)));
        for (int i=0; i<=directions.size(); i++)
        {
            if (direction.equals(directions.get(i)) && !rotateLeft)
            {
                lastTurn = TurnType.RIGHT;
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
                lastTurn = TurnType.LEFT;
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

}
