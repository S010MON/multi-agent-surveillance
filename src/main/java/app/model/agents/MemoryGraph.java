package app.model.agents;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;

import lombok.Getter;
import org.jgrapht.Graphs;
import org.jgrapht.graph.SimpleGraph;

import java.util.HashMap;

public class MemoryGraph<Object, DefaultEdge> extends SimpleGraph
{
    @Getter private HashMap<String, GraphCell> vertices = new HashMap<>();

    public MemoryGraph()
    {
        super(org.jgrapht.graph.DefaultEdge.class);
    }

    public void addVertex(Vector position, boolean obstacle, boolean occupied, double pheromone)
    {
        if(!adjustVertex(position, obstacle, occupied, pheromone));
        {
            addNewVertex(position, obstacle, occupied, pheromone);
        }
    }

    public void addNewVertex(Vector position, boolean obstacle, boolean occupied, double pheromone)
    {
        GraphCell vertice = new GraphCell(position, obstacle, occupied, pheromone);
        this.addVertex(vertice);
        vertices.put(vertice.getXY(), vertice);
    }

    /*
     * If a vertex exists, it will be adjusted to new value & return true
     * If it does not exist, it will return false.
     */
    public boolean adjustVertex(Vector position, boolean obstacle, boolean occupied, double pheromone)
    {
        GraphCell cell = getVertexAt(position);
        if(cell != null)
        {
            cell.setOccupied(occupied);
            cell.setObstacle(obstacle);
            cell.updatePheromone(pheromone);
            return true;
        }
        return false;
    }

    public void setEdge(double weight)
    {
        this.setEdge((int)weight);
    }

    public GraphCell getVertexAt(Vector position)
    {
        return vertices.get(keyGenerator(position));
    }

    public String keyGenerator(Vector position)
    {
        return position.getX() + " " + position.getY();
    }
}
