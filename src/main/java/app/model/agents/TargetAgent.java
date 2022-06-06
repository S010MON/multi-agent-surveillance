package app.model.agents;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Evasion.EvasionAgent;
import app.model.agents.WallFollow.WallFollowAgent;

public class TargetAgent extends AgentImp
{
    private Ray targetRay;
    private boolean targetLost = false;

    public TargetAgent(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    public TargetAgent(Agent other)
    {
        this(other.getPosition(), other.getDirection(), other.getRadius(), other.getType());
        copyOver(other);
    }

    public Move move()
    {
        boolean tgtVisible = updateTargetRay();

        // If the target has been lost, don't move and change state
        if(!tgtVisible)
        {
            targetLost = true;
            return new Move(direction, new Vector());
        }

        // Otherwise move towards the target
        if(!direction.equals(targetRay.direction()))
        {
            return new Move(targetRay.direction(), new Vector());
        }
        else
        {
            return new Move(targetRay.direction(), targetRay.direction().scale(moveLength));
        }
    }

    /**
     * Update the target ray to see the new location of the target
     * @return true if target found, false if lost
     */
    private boolean updateTargetRay()
    {
        Ray tempRay = null;

        for(Ray ray: view)
        {
            if(ray.getType() == Type.TARGET)
                tempRay = ray;
        }

        if(tempRay != null)
        {
            targetRay = tempRay;
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public Agent nextState()
    {
        if(isTypeSeen(Type.GUARD))
            return new EvasionAgent(this);
        if(targetLost || moveFailed)
            return new WallFollowAgent(this);

        return this;
    }
}