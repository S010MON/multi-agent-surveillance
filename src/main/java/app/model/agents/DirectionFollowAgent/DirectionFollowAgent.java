package app.model.agents.DirectionFollowAgent;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.agents.AgentImp;
import app.model.agents.Team;
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

    private Ray targetRay;
    private InternalState internalState;
    private boolean DEBUG = false;
    @Getter @Setter private double moveLength;

    public DirectionFollowAgent(Vector position, Vector direction, double radius, Team team, Vector targetDirection)
    {
        super(position, direction, radius, team);
        if(targetDirection == null)
        {
            throw new RuntimeException("no targetDirection provided for intruder");
        }
        targetRay = new Ray(position, targetDirection);
        internalState = InternalState.followRay;
    }

    public DirectionFollowAgent(Vector position, Vector direction, double radius, Team team, Vector targetDirection, double moveLen)
    {
        super(position, direction, radius, team);
        if(targetDirection == null)
        {
            throw new RuntimeException("no targetDirection provided for intruder");
        }
        targetRay = new Ray(position, targetDirection);
        internalState = InternalState.followRay;
        this.moveLength = moveLen;
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


}
