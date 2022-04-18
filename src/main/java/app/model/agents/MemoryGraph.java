package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;

import lombok.Getter;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.HashMap;
import java.util.Set;

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

    public void add_or_adjust_Vertex(Vector position)
    {
        GraphCell cell = getVertexAt(position);

        if(cell != null)
        {
            modifyVertex(cell);
        }
        else
        {
            Vector vertexCentre = determineVertexCentre(position);
            cell = new GraphCell(vertexCentre);
            addVertex(cell);

            vertices.put(keyGenerator(position), cell);
            connectNeighbouringVertices(cell);
        }
    }

    public void modifyVertex(GraphCell cell)
    {
        cell.setOccupied(true);
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
              c5
               |
    |c0|--c1--c2
               |
              c7
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
            return eastWestAggregate(currentCell, "East");
        }
        else if(cardinalDirections.get("West").equals(cardinalMovement))
        {
            return eastWestAggregate(currentCell, "West");
        }
        else
        {
            throw new RuntimeException("No cell aggregate could be calculated");
        }
    }

    //Refactor into sustainable way
    public double northSouthAggregate(GraphCell currentCell, String direction)
    {
        Vector currentPosition = currentCell.getPosition();
        Vector aggregateDirection = cardinalDirections.get(direction);

        GraphCell c1 = getVertexAt(currentPosition.add(aggregateDirection));
        Vector TPosition = currentPosition.add(aggregateDirection.scale(2));
        GraphCell c2 = getVertexAt(TPosition);
        GraphCell c3 = getVertexAt(currentPosition.add(aggregateDirection.scale(3)));
        //GraphCell c3 = getVertexAt(TPosition.add(cardinalDirections.get("East")));
        //GraphCell c4 = getVertexAt(TPosition.add(cardinalDirections.get("West")));

        GraphCell[] aggregateVertices =  {c1, c2};
        return aggregatePheromoneValues(aggregateVertices);
    }

    public double eastWestAggregate(GraphCell currentCell, String direction)
    {
        Vector currentPosition = currentCell.getPosition();
        Vector aggregateDirection = cardinalDirections.get(direction);

        GraphCell c1 = getVertexAt(currentPosition.add(aggregateDirection));
        Vector TPosition = currentPosition.add(aggregateDirection.scale(2));
        GraphCell c2 = getVertexAt(TPosition);
        GraphCell c3 = getVertexAt(currentPosition.add(aggregateDirection.scale(3)));
        //GraphCell c3 = getVertexAt(TPosition.add(cardinalDirections.get("North")));
        //GraphCell c4 = getVertexAt(TPosition.add(cardinalDirections.get("South")));

        GraphCell[] aggregateVertices =  {c1, c2};
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

    public void leaveVertex(Vector position, double pheromoneValue)
    {
        GraphCell cell = getVertexAt(position);
        cell.setOccupied(false);
        cell.updatePheromone(pheromoneValue);
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
        cardinalDirections.put("North", new Vector(0, -travelDistance));
        cardinalDirections.put("East", new Vector(travelDistance, 0));
        cardinalDirections.put("South", new Vector(0, travelDistance));
        cardinalDirections.put("West", new Vector(-travelDistance, 0));
    }

    public void evaporateWorld()
    {
        Set<GraphCell> vertexSet = this.vertexSet();
        for(GraphCell cell: vertexSet)
        {
            cell.evaporate();
        }
    }
}
