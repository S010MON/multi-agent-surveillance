package app.model.agents.Evasion;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Agent;
import app.model.agents.AgentImp;

public class RunAwayAgent extends AgentImp
{
    public RunAwayAgent(Vector position, Vector direction, double radius, Type type)
    {
        super(position, direction, radius, type);
    }

    public RunAwayAgent(Agent other)
    {
        super(other);
    }

    @Override
    public Move move()
    {
        Ray longest = null;
        double max = 0;
        for(Ray r: view)
        {
            if(r.length() > max)
            {
                longest = r;
                max = longest.length();
            }
        }

        Vector deltaPos = longest.getV()
                                 .sub(position)
                                 .normalise()
                                 .scale(maxSprint);
        Vector endDir = deltaPos;
        return new Move(endDir, deltaPos);
    }
}
