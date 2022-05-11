package app.model.agents.ACO;

import app.controller.linAlg.Vector;
import app.model.Type;

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
}
