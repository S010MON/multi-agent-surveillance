package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;

import lombok.Getter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGraph<Object, DefaultWeightedEdge> extends SimpleWeightedGraph
{
    @Getter private HashMap<String, GraphCell> vertices = new HashMap<>();
    private int travelDistance;
    @Getter private ArrayList<Vector> cardinalVectors = new ArrayList<>();


    public MemoryGraph(int distance)
    {
        super(org.jgrapht.graph.DefaultWeightedEdge.class);
        this.travelDistance = distance;
        populateCardinalVectors();
    }

    //Modify to create edges between two cells (Do intrinisically without agent input)
    // Check existance of neighbouring vertex, if they exist, add edge
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
            connectNeighbouringVertices(cell);
        }
    }

    public void modifyVertex(GraphCell cell, boolean obstacle, boolean occupied, double pheromone)
    {
        cell.setObstacle(obstacle);
        cell.setOccupied(occupied);
        cell.updatePheromone(pheromone);
    }

    public void connectNeighbouringVertices(GraphCell currentCell)
    {
        Vector currentPosition = currentCell.getPosition();
        for(Vector cardinal: cardinalVectors)
        {
            Vector resultingPosition = currentPosition.add(cardinal);
            GraphCell neighbouringCell = vertices.get(keyGenerator(resultingPosition));
            if(neighbouringCell != null && !containsEdge(currentCell, neighbouringCell))
            {
                DefaultWeightedEdge edge = (DefaultWeightedEdge) this.addEdge(currentCell, neighbouringCell);
                this.setEdgeWeight(edge, travelDistance);
            }
        }
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
        if(axisPosition >=0)
        {
            return axis_start + centreDistance;
        }
        else
        {
            return axis_start - centreDistance;
        }
    }

    public void populateCardinalVectors()
    {
        cardinalVectors.add(new Vector(travelDistance, 0));
        cardinalVectors.add(new Vector(0, travelDistance));
        cardinalVectors.add(new Vector(-travelDistance, 0));
        cardinalVectors.add(new Vector(0, -travelDistance));
    }
}
