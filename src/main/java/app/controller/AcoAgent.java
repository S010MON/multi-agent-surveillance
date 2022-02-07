package app.controller;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class AcoAgent extends AgentImp
{
    private Graph<Vertice, DefaultWeightedEdge> world = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    private double visualDistance = 50.0;

    public AcoAgent(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
    }

    public Graph accessAgentWorld()
    {
        return world;
    }

    public double accessAgentVisualDistance()
    {
        return visualDistance;
    }
}
