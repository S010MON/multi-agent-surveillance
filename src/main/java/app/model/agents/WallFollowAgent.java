package app.model.agents;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class WallFollowAgent extends AgentImp
{

    private enum TurnType
    {
        LEFT,
        RIGHT,
        NO_TURN,
    }
    private TurnType lastTurn = TurnType.NO_TURN;
    private final double MOVE_LENGTH = 1;  // equal to obstacleDetectionDistance as only care about walls within move range

    public WallFollowAgent(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
    }

    @Override
    public void move()
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
            newMove = new Vector(MOVE_LENGTH * direction.getX(), MOVE_LENGTH * direction.getY());
        }
        else if (noWallDetected(getAngleOfLeftRay()))
        {
            direction = rotateAgentLeft(true);
        }
        else if (noWallDetected(direction.getAngle()))
        {
            newMove = new Vector(MOVE_LENGTH * direction.getX(), MOVE_LENGTH * direction.getY());
        }
        else
        {
            rotateAgentLeft(false);
        }
        position = position.add(newMove);
    }

    private boolean noWallDetected(double rayAngle)
    {
        // if the ray with given rayAngle detects an obstacle -> return false
        for (Ray r : view)
        {
            if (r.angle() == rayAngle && r.rayLength() < MOVE_LENGTH) {
                return false;
            }
        }
        return true;
    }

    private Vector rotateAgentLeft(boolean rotateLeft)
    {
        // if rotateLeft is true, rotate agent 90 degrees left
        // if rotateLeft id false, rotate agent 90 degrees right
        ArrayList<Vector> directions = new ArrayList<>(Arrays.asList(new Vector(0,1),
                new Vector(1,0), new Vector(0,-1), new Vector(-1,0)));
        for (int i=0; i<=directions.size(); i++)
        {
            if (direction.equals(directions.get(i)) && rotateLeft)
            {
                lastTurn = TurnType.LEFT;
                if (i == 0)
                {
                    return directions.get(3);
                }
                else
                {
                    return directions.get(i-1);
                }
            }
            else if (direction.equals(directions.get(i)) && !rotateLeft)
            {
                lastTurn = TurnType.RIGHT;
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

    private double getAngleOfLeftRay()
    {
        double currentAngle = direction.getAngle();
        if (currentAngle >= 90)
        {
            return currentAngle - 90;
        }
        else
        {
            return 360 + (currentAngle - 90);
        }
    }

}
