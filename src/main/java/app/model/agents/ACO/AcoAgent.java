package app.model.agents.ACO;

import app.model.agents.AgentImp;
import org.jgrapht.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import app.controller.linAlg.Vector;

public class AcoAgent extends AgentImp
{
    private Graph<Vertice, DefaultWeightedEdge> world = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    private double obstacleDetectionDistance = 50.0;
    private Vertice currentPosition;

    /**
     * Root node is initialized to origin.
     */
    public AcoAgent(Vector position, Vector direction, double radius)
    {
        super(position, direction, radius);
        currentPosition = new Vertice();
    }

    public Graph accessAgentWorld()
    {
        return world;
    }

    public Vector visionToDirectionLink(double angle)
    {
        switch((int)angle)
        {
            case 0:
                return new Vector(1, 0);
            case 90:
                return new Vector(0, 1);
            case 180:
                return new Vector(-1, 0);
            case 270:
                return new Vector(0, -1);
            default:
                return null;
        }
    }

    //TODO Method to detect obstacle within specified distance
    public boolean detectObstacleWithinVision()
    {
        return false;
    }

    public Vector nextVerticePosition(Vector link)
    {
        Vector nextVerticePosition = currentPosition.accessCellPosition().add(link);
        return nextVerticePosition;
    }
}
