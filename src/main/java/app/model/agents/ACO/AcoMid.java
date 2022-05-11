package app.model.agents.ACO;

import app.controller.linAlg.Vector;
import app.model.Type;

public class AcoMid extends AcoStraight
{
    public AcoMid(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
        momentumHeuristic = 0.5;
    }
}
