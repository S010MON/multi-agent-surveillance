package app.model.agents.WallFollow;

import app.model.agents.MemoryGraph;

public class WfWorld<Object, DefaultWeightedEdge> extends MemoryGraph
{
    public WfWorld(int distance)
    {
        super(distance);
    }
}
