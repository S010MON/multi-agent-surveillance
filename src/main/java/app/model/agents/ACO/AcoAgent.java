package app.model.agents.ACO;

import app.model.agents.AgentImp;
import org.jgrapht.*;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import app.controller.linAlg.Vector;

public class AcoAgent extends AgentImp
{
    private Graph<Vertice, DefaultEdge> world = new SimpleGraph<>(DefaultEdge.class);

    private VisionToGraph worldConversion = new VisionToGraph(world);

    public AcoAgent(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
    }

    public Graph accessAgentWorld()
    {
        return world;
    }





}
