package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.agents.ACO.AcoAgentLimitedVision;
import app.model.agents.WallFollow.WallFollowAgent;

public abstract class AgentFactory
{
    public static Agent make(AgentType type, Vector position, Vector direction, double radius, Team team)
    {
        switch(type)
        {
            case ACO -> {return new AcoAgentLimitedVision(position, direction, radius, team);}
            case WALL_FOLLOW -> {return new WallFollowAgent(position, direction, radius, team);}
            case RANDOM -> {return new AgentImp(position, direction, radius, team);}
        }
        return null;
    }
}
