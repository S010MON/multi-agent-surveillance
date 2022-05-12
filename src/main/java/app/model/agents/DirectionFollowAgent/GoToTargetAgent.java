package app.model.agents.DirectionFollowAgent;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;
import app.model.agents.AgentImp;

public class GoToTargetAgent extends AgentImp
{
    private Ray targetRay;

    public GoToTargetAgent(Vector position, Vector direction, double radius, Type type, Ray targetRay)
    {
        super(position, direction, radius, type);
        this.targetRay = targetRay;
    }

    public Move move()
    {
        Vector guardPosition = guardPosition();
        if(guardPosition==null)
        {
            return goToTarget();
        }

    }

    private Move goToTarget()
    {
        if(!direction.equals(targetRay.direction()))
        {
            return new Move(targetRay.direction(), new Vector(0, 0));
        } else
        {
            return new Move(targetRay.direction(), targetRay.direction().scale(moveLength));
        }
    }

    private Vector guardPosition()
    {

    }


    //TODO: implement this
    @Override
    public Agent nextState()
    {
        return this;
    }
}
