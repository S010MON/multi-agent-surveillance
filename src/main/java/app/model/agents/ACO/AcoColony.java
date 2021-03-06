package app.model.agents.ACO;

import app.controller.linAlg.Vector;
import app.model.Type;
import app.model.agents.Agent;

public class AcoColony extends AcoMomentum
{
    public AcoColony(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
        if((getAcoAgentCount() % 2) == 0)
        {
            // Set heuristic value to that of Aco Mid
            momentumHeuristic = 0.5;
        }
        else
        {
            // Set heuristic value to that of Aco Straight
            momentumHeuristic = 0.97;
        }
    }

    public AcoColony(Agent other)
    {
        this(other.getPosition(), other.getDirection(), other.getRadius(), other.getType());
        copyOver(other);
    }
}
