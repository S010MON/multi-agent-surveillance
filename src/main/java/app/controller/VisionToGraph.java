package app.controller;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class VisionToGraph
{
    private Graph<Vertice, DefaultWeightedEdge> world;
    //Current assumption is 360 degree visual field

    private Beam[] currentVisualField;

    public VisionToGraph(Graph world)
    {
        this.world = world;
    }

    //TODO Once direction function is complete
    public void updateWorld(Beam[] visualField) {}
}
