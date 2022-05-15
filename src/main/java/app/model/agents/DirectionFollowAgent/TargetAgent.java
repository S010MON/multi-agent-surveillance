package app.model.agents.DirectionFollowAgent;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Angle;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.agents.World;

public class TargetAgent extends AgentImp
{
    private Ray targetRay;
    private boolean guardDetected;
    private final boolean DEBUG = false;

    public TargetAgent(Vector position, Vector direction, double radius, Type type, Ray targetRay, World world)
    {
        super(position, direction, radius, type);
        this.targetRay = targetRay;
        this.world = world;
        guardDetected = false;
    }

    public Move move()
    {
        return goToTarget();
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


    @Override
    public Agent nextState()
    {
        if(isTypeSeen(Type.GUARD))
        {
            //TODO: make this return a new EvasionAgent,
            //      once that agent exists in this branch
            //return new EvasionAgent();
        }

        return this;
    }
}
