package app.model.agents;

import org.jgrapht.graph.SimpleGraph;

public class MemoryGraph<Object, DefaultEdge> extends SimpleGraph
{
    public MemoryGraph()
    {
        super(org.jgrapht.graph.DefaultEdge.class);
    }

    public void addVertex(int x, int y)
    {

    }
}
