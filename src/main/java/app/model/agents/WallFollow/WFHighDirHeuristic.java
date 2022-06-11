package app.model.agents.WallFollow;

import app.controller.linAlg.Vector;
import app.model.Type;
import app.model.agents.Agent;

public class WFHighDirHeuristic extends WallFollowAgent
{
    /** WallFollow agent that puts high weight on keeping in current direction while doing heuristics. */
    public WFHighDirHeuristic(Vector position, Vector direction, double radius, Type type, double moveLen)
    {
        super(position, direction, radius, type, moveLen);
        directionHeuristicWeight = 4;
    }

    public WFHighDirHeuristic(Agent other)
    {
        super(other);
        directionHeuristicWeight = 4;
    }
}
