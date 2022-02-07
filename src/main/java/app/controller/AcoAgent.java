package app.controller;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class AcoAgent extends AgentImp
{
    private Graph<Vertice, DefaultWeightedEdge> world = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

    public AcoAgent(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
    }

    private Graph accessAgentWorld()
    {
        return world;
    }
}
