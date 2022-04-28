package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;


public abstract class World
{
    protected MemoryGraph G;

    public World(MemoryGraph G)
    {
        this.G = G;
    }

    protected GraphCell addNewVertex(Vector position)
    {
        return null;
    }

    public void add_or_adjust_Vertex(Vector position) {}

    protected void connectNeighbouringVertices(GraphCell currentCell) {}

    protected void modifyVertex(GraphCell cell)
    {
        cell.setOccupied(true);
    }
}
