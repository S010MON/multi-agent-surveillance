package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;

import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.HashMap;
import java.util.Set;

public class MemoryGraph<Object, DefaultWeightedEdge> extends SimpleWeightedGraph
{
    @Getter private HashMap<String, GraphCell> vertices = new HashMap<>();
    @Getter private HashMap<String, Vector> cardinalDirections = new HashMap<>();
    private int travelDistance;
    @Getter private double obstaclePheromoneValue = 1000.0;

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
            addNewVertex(position);
        }
    }

    private GraphCell addNewVertex(Vector position)
    {
        Vector vertexCentre = determineVertexCentre(position);
        GraphCell cell = new GraphCell(vertexCentre);
        addVertex(cell);

        vertices.put(keyGenerator(position), cell);
        connectNeighbouringVertices(cell);
        return cell;
    }

    public void setVertexAsObstacle(Vector currentPosition, Vector movement)
    {
        Vector obstaclePosition = currentPosition.add(movement);
        GraphCell obstacleVertex = getVertexAt(obstaclePosition);
        if(obstacleVertex != null)
        {
            obstacleVertex.setObstacle(true);
        }
        else
        {
            obstacleVertex = addNewVertex(obstaclePosition);
            obstacleVertex.setObstacle(true);
        }
        obstacleVertex.setPheromone(obstaclePheromoneValue);
    }

    private void modifyVertex(GraphCell cell)
    {
        cell.setOccupied(true);
    }

    private void connectNeighbouringVertices(GraphCell currentCell)
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

    //Possibly scale for aggregate values
    public double aggregateCardinalPheromones(Vector currentPosition, Vector cardinalMovement)
    {
        GraphCell neighbour = getVertexAt(currentPosition.add(cardinalMovement));
        GraphCell[] aggregateCells = {neighbour};
        return aggregatePheromoneValues(aggregateCells);
    }

    //Depreciated, however still necessary in case of calculating aggregate pheromone values
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

    private String keyGenerator(Vector position)
    {
        Vector centrePosition = determineVertexCentre(position);
        return centrePosition.getX() + " " + centrePosition.getY();
    }

    private Vector determineVertexCentre(Vector position)
    {
        int x_centre = calculateDimensionCentre(position.getX());
        int y_centre = calculateDimensionCentre(position.getY());

        return new Vector(x_centre, y_centre);
    }

    private int calculateDimensionCentre(double axisPosition)
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

    private void populateCardinalVectors()
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
