package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class World
{
    public MemoryGraph<GraphCell, DefaultWeightedEdge> G;

    public World(MemoryGraph G)
    {
        this.G = G;
    }

    protected GraphCell addNewVertex(Vector position)
    {
        return null;
    }

    public void add_or_adjust_Vertex(Vector position) {}

    public void connectNeighbouringVertices(GraphCell currentCell) {}

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

    public void setInitialWallFollowPos(GraphCell graphCell)
    {
        G.setInitialWallFollowPos(graphCell);
    }

    public GraphCell getInitialWallFollowPos()
    {
        return G.getInitialWallFollowPos();
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

    public void leaveVertex(Vector position)
    {
        G.leaveVertex(position);
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


}
