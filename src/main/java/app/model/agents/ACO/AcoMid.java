package app.model.agents.ACO;

import app.controller.linAlg.Vector;
import app.model.Type;
import app.model.agents.Agent;

public class AcoMid extends AcoMomentum
{
    public AcoMid(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
        momentumHeuristic = 0.5;
    }

    public AcoMid(Agent other)
    {
        this(other.getPosition(), other.getDirection(), other.getRadius(), other.getType());
        copyOver(other);
    }
}
