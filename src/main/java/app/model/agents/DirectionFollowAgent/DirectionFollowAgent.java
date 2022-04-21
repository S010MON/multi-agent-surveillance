package app.model.agents.DirectionFollowAgent;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.agents.AgentImp;
import app.model.agents.Cells.BooleanCell;
import app.model.agents.Team;
import app.model.agents.WallFollow.WallFollowAgent;
import lombok.Getter;
import lombok.Setter;


public class DirectionFollowAgent extends AgentImp
{
    public enum InternalState
    {
        followWall,
        followRay,
        goToTarget,
    }

    public enum TurnType
    {
        LEFT,
        RIGHT,
        NO_TURN,
    }

    private Ray targetRay;
    private InternalState internalState;
    private boolean DEBUG = false;
    @Getter @Setter private double moveLength = 20;
    private TurnType lastTurn;
    private TurnType wallTurn;

    public DirectionFollowAgent(Vector position, Vector direction, double radius, Team team, Vector targetDirection)
    {
        super(position, direction, radius, team);
        if(targetDirection == null)
        {
            throw new RuntimeException("no targetDirection provided for intruder");
        }
        targetRay = new Ray(position, targetDirection);

        internalState = InternalState.followRay;
        lastTurn = TurnType.NO_TURN;
    }

    public DirectionFollowAgent(Vector position, Vector direction, double radius, Team team, Vector targetDirection, double moveLen)
    {
        super(position, direction, radius, team);
        if(targetDirection == null)
        {
            throw new RuntimeException("no targetDirection provided for intruder");
        }
        targetRay = new Ray(position, targetDirection);
        moveLength = moveLen;

        internalState = InternalState.followRay;
        lastTurn = TurnType.NO_TURN;
    }


    @Override
    public Move move()
    {
        switch(internalState)
        {
            case followRay -> { return followRay(); }
            case followWall -> { return followWall(); }
            case goToTarget -> { return goToTarget(); }
        }

        return null;
    }

    private Move followRay()
    {
        return null;
    }

    private Move followWall()
    {
        return null;
    }

    private Move goToTarget()
    {
        return null;
    }

    /**
     * Method for checking for walls/obstacles for getting next move in the wall following algorithm.
     * Walls/obstacles are checked in the direction of the given rayAngle by checking if that ray detects
     * an obstacle within the moveLength distance range.
     * @param rayAngle angle of the direction to be checked.
     * @return true if no obstacle detected; false if obstacle detected
     */
    private boolean noWallDetected(double rayAngle)
    {
        for (Ray r : view)
        {
            if ((r.angle() <= rayAngle + 1.0 && r.angle() >= rayAngle - 1.0) && r.rayLength() <= moveLength)
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

    /** Pseudocode for simple wall following algorithm:
         if (turned wallTurn previously and forward no wall)
            go forward;
         else if (no wall at wallTurn)
            turn 90 deg wallTurn;
         else if (no wall forward)
            go forward;
         else
            turn 90 deg opposite of wallTurn;

     * based on: https://blogs.ntu.edu.sg/scemdp-201718s1-g14/exploration-algorithm/
     */

    public Move runWallFollowAlgorithm()
    {
        Vector newMove = new Vector(0,0);
        Vector newDirection = direction;
        if (lastTurn == wallTurn && noWallDetected(direction.getAngle()))
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 1"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            lastTurn = TurnType.NO_TURN;
        }
        else if (noWallDetected(getAngleOfLeftRay()) && !leftCell.getObstacle())
        {
            if (DEBUG) { System.out.println("Angle of left ray: " + getAngleOfLeftRay()); ; }
            if (DEBUG) { System.out.println("ALGORITHM CASE 2"); }
            // TODO: make rotateAgent(TurnType turn) method and use here
            newDirection = rotateAgentLeft();
            lastTurn = WallFollowAgent.TurnType.LEFT;
            movedForwardLast = false;
        }
        else if (noWallDetected(direction.getAngle()) && !forwardCell.getObstacle())
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 3"); }
            newMove = new Vector(moveLength * direction.getX(), moveLength * direction.getY());
            movedForwardLast = true;
            lastTurn = WallFollowAgent.TurnType.NO_TURN;
            markWallAsCovered();
        }
        else
        {
            if (DEBUG) { System.out.println("ALGORITHM CASE 4"); }
            newDirection = rotateAgentRight();
            lastTurn = WallFollowAgent.TurnType.RIGHT;
            movedForwardLast = false;
        }
        return new Move(newDirection,newMove);
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
