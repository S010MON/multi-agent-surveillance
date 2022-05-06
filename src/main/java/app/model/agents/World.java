package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;
import org.jgrapht.graph.DefaultWeightedEdge;

public abstract class World
{
    protected MemoryGraph<GraphCell, DefaultWeightedEdge> G;

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

    public void setInitialWallFollowPos


}
