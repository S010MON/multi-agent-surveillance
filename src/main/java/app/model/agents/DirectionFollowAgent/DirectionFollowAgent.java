package app.model.agents.DirectionFollowAgent;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.agents.AgentImp;
import app.model.agents.Team;


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
        // followRay assumes the start point is on the ray itself
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

}
