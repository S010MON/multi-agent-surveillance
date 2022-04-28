package app.controller.linAlg;

import app.model.agents.Team;
import lombok.Getter;

public class AgentIntersection
{
    @Getter private Vector intersection;
    @Getter private Team agentTeam;

    public AgentIntersection(Vector intersection, Team agentTeam)
    {
        this.intersection = intersection;
        this.agentTeam = agentTeam;
    }
}
