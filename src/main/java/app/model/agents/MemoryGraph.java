package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;

import lombok.Getter;
import org.jgrapht.graph.SimpleGraph;

import java.util.HashMap;

public class MemoryGraph<Object, DefaultEdge> extends SimpleGraph
{
    @Getter private HashMap<String, GraphCell> vertices = new HashMap<>();
    @Getter private int travelDistance;

    public MemoryGraph(int distance)
    {
        super(org.jgrapht.graph.DefaultEdge.class);
        this.travelDistance = distance;
    }

    public void add_or_adjust_Vertex(Vector position, boolean obstacle, boolean occupied, double pheromone)
    {
        GraphCell cell = getVertexAt(position);

        if(cell != null)
        {
            modifyVertex(cell, obstacle, occupied, pheromone);
        }
        else
        {
            Vector vertexCentre = determineVertexCentre(position);
            cell = new GraphCell(vertexCentre, obstacle, occupied, pheromone);
            addVertex(cell);

            vertices.put(keyGenerator(position), cell);
        }
    }

    public void modifyVertex(GraphCell cell, boolean obstacle, boolean occupied, double pheromone)
    {
        cell.setObstacle(obstacle);
        cell.setOccupied(occupied);
        cell.updatePheromone(pheromone);
    }


    public GraphCell getVertexAt(Vector position)
    {
        return vertices.get(keyGenerator(position));
    }

    public String keyGenerator(Vector position)
    {
        Vector centrePosition = determineVertexCentre(position);
        return centrePosition.getX() + " " + centrePosition.getY();
    }

    public Vector determineVertexCentre(Vector position)
    {
        int x_centre = calculateDimensionCentre(position.getX());
        int y_centre = calculateDimensionCentre(position.getY());

        return new Vector(x_centre, y_centre);
    }

    public int calculateDimensionCentre(double axisPosition)
    {
        int centreDistance = travelDistance / 2;
        int axis_start = (int)(axisPosition / travelDistance) * travelDistance;
        int axis_centre = axis_start + centreDistance;
        return (int)axis_centre;
    }
}
