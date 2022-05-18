package app.model.agents.WallFollow;

import app.controller.linAlg.Vector;
import app.model.Type;

public class WFMedDirHeuristic extends WallFollowAgent
{
    /** WallFollow agent that puts medium weight on keeping in current direction while doing heuristics. */
    public WFMedDirHeuristic(Vector position, Vector direction, double radius, Type type, double moveLen)
    {
        super(position, direction, radius, type, moveLen);
        directionHeuristicWeight = 2;
    }

}
