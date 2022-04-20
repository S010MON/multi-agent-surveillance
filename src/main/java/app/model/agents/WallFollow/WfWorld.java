package app.model.agents.WallFollow;

import app.model.agents.MemoryGraph;

public class WfWorld<Object, DefaultEdge> extends MemoryGraph
{
    public WfWorld(int distance)
    {
        super(distance);
    }
}
