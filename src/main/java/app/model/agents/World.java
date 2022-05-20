package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;
import lombok.Getter;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class World
{
    @Getter private ArrayList<Integer> horizontalWallsCovered = new ArrayList<>();
    @Getter private ArrayList<Integer> verticalWallsCovered = new ArrayList<>();

    @Getter public MemoryGraph<GraphCell, DefaultWeightedEdge> G;

    public World(MemoryGraph G)
    {
        this.G = G;
    }

    public void connectNeighbouringVertices(GraphCell currentCell) {}

    public void add_or_adjust_Vertex(Vector position){}

    protected GraphCell addNewVertex(Vector position)
    {
        Vector vertexCentre = G.determineVertexCentre(position);
        GraphCell cell = new GraphCell(vertexCentre);
        G.addVertex(cell);
        G.vertices.put(G.keyGenerator(position), cell);
        return cell;
    }

    protected void modifyVertex(GraphCell cell)
    {
        cell.setOccupied(true);
    }

    public GraphCell getVertexAt(Vector position)
    {
        return G.getVertexAt(position);
    }

    public GraphCell getVertexFromCurrent(GraphCell currentCell, String direction)
    {
        return G.getVertexFromCurrent(currentCell, direction);
    }

    public String getDirectionStr(double directionAngle)
    {
        return G.getDirectionStr(directionAngle);
    }

    public ArrayList<GraphCell> getVerticesWithUnexploredNeighbours()
    {
        return G.getVerticesWithUnexploredNeighbours();
    }

    public HashMap<String, Vector> getCardinalDirections()
    {
        return G.cardinalDirections;
    }

    public void leaveVertex(Vector position, double pheromoneValue)
    {
        G.leaveVertex(position, pheromoneValue);
    }

    public double aggregateCardinalPheromones(Vector currentPosition, Vector cardinalMovement)
    {
        return G.aggregateCardinalPheromones(currentPosition, cardinalMovement);
    }

    public void setVertexAsObstacle(Vector currentPosition, Vector movement)
    {
        G.setVertexAsObstacle(currentPosition, movement);
    }

    public void evaporateWorld()
    {
        G.evaporateWorld();
    }

    public void markWallAsCovered(GraphCell cell, Vector position) {}
}
