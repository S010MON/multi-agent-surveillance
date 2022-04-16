package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;

import lombok.Getter;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGraph<Object, DefaultWeightedEdge> extends SimpleWeightedGraph
{
    @Getter private HashMap<String, GraphCell> vertices = new HashMap<>();
    @Getter private HashMap<String, Vector> cardinalDirections = new HashMap<>();
    private int travelDistance;


    public MemoryGraph(int distance)
    {
        super(org.jgrapht.graph.DefaultWeightedEdge.class);
        this.travelDistance = distance;
        populateCardinalVectors();
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
        for(Vector cardinal: cardinalDirections.values())
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

    /*
    Aggregate pattern as follows
              c5--c6
               |  |
    |c0|--c1--c2--c3--c4
               |  |
              c7--c8
     */
    public double aggregateCardinalPheromones(Vector currentPosition, Vector cardinalMovement)
    {
        GraphCell currentCell = getVertexAt(currentPosition);
        if(cardinalDirections.get("North").equals(cardinalMovement))
        {
            return northSouthAggregate(currentCell, "North");
        }
        else if(cardinalDirections.get("South").equals(cardinalMovement))
        {
            return northSouthAggregate(currentCell, "South");
        }
        else if(cardinalDirections.get("East").equals(cardinalMovement))
        {
            return northSouthAggregate(currentCell, "East");
        }
        else if(cardinalDirections.get("West").equals(cardinalMovement))
        {
            return northSouthAggregate(currentCell, "West");
        }
        else
        {
            throw new RuntimeException("No cell aggregate could be calculated");
        }
    }

    //Refactor into sustainable way
    public double northSouthAggregate(GraphCell currentCell, String direction)
    {
        GraphCell c1 = getVertexFromCurrent(currentCell, direction);

        GraphCell c2 = getVertexFromCurrent(c1, direction);
        GraphCell c5 = getVertexFromCurrent(c2, "East");
        GraphCell c7 = getVertexFromCurrent(c2, "West");


        GraphCell c3 = getVertexFromCurrent(c2, direction);
        GraphCell c6 = getVertexFromCurrent(c3, "East");
        GraphCell c8 = getVertexFromCurrent(c2, "West");

        GraphCell c4 = getVertexFromCurrent(c3, direction);
        GraphCell[] aggregateVertices =  {c1, c2, c3, c4, c5, c6, c7, c8};
        return aggregatePheromoneValues(aggregateVertices);
    }

    public double eastWestAggregate(GraphCell currentCell, String direction)
    {
        GraphCell c1 = getVertexFromCurrent(currentCell, direction);

        GraphCell c2 = getVertexFromCurrent(c1, direction);
        GraphCell c5 = getVertexFromCurrent(c2, "North");
        GraphCell c7 = getVertexFromCurrent(c2, "South");


        GraphCell c3 = getVertexFromCurrent(c2, direction);
        GraphCell c6 = getVertexFromCurrent(c3, "North");
        GraphCell c8 = getVertexFromCurrent(c2, "South");

        GraphCell c4 = getVertexFromCurrent(c3, direction);
        GraphCell[] aggregateVertices =  {c1, c2, c3, c4, c5, c6, c7, c8};
        return aggregatePheromoneValues(aggregateVertices);
    }


    public double aggregatePheromoneValues(GraphCell[] aggregateVertices)
    {
        double pheromoneSum = 0;
        for(GraphCell vertex: aggregateVertices)
        {
            if(vertex != null)
            {
                pheromoneSum += vertex.getPheromone();
            }
        }
        return pheromoneSum;
    }

    public GraphCell getVertexFromCurrent(GraphCell currentCell, String direction)
    {
        Vector currentPosition = currentCell.getPosition();
        Vector neighbouringCellDirection = cardinalDirections.get(direction);
        return getVertexAt(currentPosition.add(neighbouringCellDirection));
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
        cardinalDirections.put("North", new Vector(0, travelDistance));
        cardinalDirections.put("East", new Vector(travelDistance, 0));
        cardinalDirections.put("South", new Vector(0, -travelDistance));
        cardinalDirections.put("West", new Vector(-travelDistance, 0));
    }
}
