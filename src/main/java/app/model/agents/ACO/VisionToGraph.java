package app.model.agents.ACO;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class VisionToGraph
{
    private Graph<Vertice, DefaultWeightedEdge> world;
    private double obstacleDetectionDistance = 1;
    private Vertice currentPosition;

    //Current assumption is 360 degree visual field
    private Ray[] currentVisualField;

    public VisionToGraph(Graph world)
    {
        this.world = world;
        Vertice startingPosition = new Vertice(new Vector());
        currentPosition = startingPosition;
        world.addVertex(startingPosition);
    }

    public void updateWorld(Ray[] visualField)
    {
        for(Ray r: visualField)
        {
            Vector movement;
            //TODO Implement ray angle function here
            double rayAngle = 0;

            if((movement = determineMove(rayAngle, r)) != null)
            {
                Vector nextPosition = nextVerticePosition(movement);
                addNextVerticeToAgentWorld(nextPosition);
            }
        }
    }

    public void addNextVerticeToAgentWorld(Vector nextPosition)
    {
        Vertice connectingPosition = new Vertice(nextPosition);
        world.addVertex(connectingPosition);

        world.addEdge(currentPosition, connectingPosition);
    }

    public Vector determineMove(double rayAngle, Ray currentRay)
    {
        Vector movement;
        if((movement = visionToDirectionLink(rayAngle)) != null && !obstacleDetectedWithinVision(currentRay))
        {
            return movement;
        }
        return null;
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

    public Vector nextVerticePosition(Vector link)
    {
        Vector nextVerticePosition = currentPosition.accessCellPosition().add(link);
        return nextVerticePosition;
    }

    public boolean obstacleDetectedWithinVision(Ray currentRay)
    {
        if(currentRay.rayLength() >  obstacleDetectionDistance)
        {
            return false;
        }
        return true;
    }
}