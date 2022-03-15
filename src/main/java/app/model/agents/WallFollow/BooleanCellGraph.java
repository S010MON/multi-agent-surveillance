package app.model.agents.WallFollow;

import app.model.agents.Cells.BooleanCell;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.HashMap;

public class BooleanCellGraph<Object,DefaultEdge> extends SimpleGraph
{
    @Getter @Setter private BooleanCell initialWallFollowPos;
    @Getter private BooleanCell agentPos;
    @Getter private HashMap<String,BooleanCell> vertices = new HashMap<>();
    @Setter int edge;  // edge length between vertices (moveLength)
    boolean DEBUG = false;

    public BooleanCellGraph()
    {
        super(org.jgrapht.graph.DefaultEdge.class);
        // TODO: maybe need to add weights to edges for heuristics later?
    }

    public void updateAgentPos(BooleanCell agentCell, BooleanCell forwardCell,
                               BooleanCell leftCell, BooleanCell rightCell)
    {
        agentPos = agentCell;
        addVertex(forwardCell);
        addVertex(leftCell);
        addVertex(rightCell);
        vertices.put(forwardCell.toString(), forwardCell);
        vertices.put(leftCell.toString(), leftCell);
        vertices.put(rightCell.toString(), rightCell);
        updateVertex(forwardCell);
        updateVertex(leftCell);
        updateVertex(rightCell);
    }

    public void updateVertex(BooleanCell vertex)
    {
        int vertexX = vertex.getX();
        int vertexY = vertex.getY();
        if (vertices.containsKey(vertexX + " " + (vertexY-edge)))  // north neighbour
        {
            this.addEdge(vertex,vertices.get(vertexX + " " + (vertexY-edge)));
        }
        if (vertices.containsKey(vertexX-edge + " " + (vertexY)))  // east neighbour
        {
            this.addEdge(vertex,vertices.get(vertexX-edge + " " + (vertexY)));
        }
        if (vertices.containsKey(vertexX + " " + (vertexY+edge)))  // south neighbour
        {
            this.addEdge(vertex,vertices.get(vertexX + " " + (vertexY+edge)));
        }
        if (vertices.containsKey(vertexX+edge + " " + (vertexY)))  // west neighbour
        {
            this.addEdge(vertex,vertices.get(vertexX+edge + " " + (vertexY)));
        }
    }

    public void addExploredVertex(BooleanCell vertex)
    {
        addVertex(vertex);
        vertices.put(vertex.toString(), vertex);
        updateVertex(vertex);
    }

    public ArrayList<BooleanCell> getVerticesWithUnexploredNeighbours()
    {
        ArrayList<BooleanCell> unexploredFrontier = new ArrayList<>();
        for (String v : vertices.keySet())
        {
            BooleanCell vertex = vertices.get(v);
            if (!vertex.getObstacle() && edgesOf(vertex).size() < 4)
            {
                unexploredFrontier.add(vertex);
            }
        }
        if (DEBUG)
        {
            System.out.println("Total nr of vertices explored: " + vertexSet().size());
            System.out.println("Nr of vertices with unexplored neighbours: " + unexploredFrontier.size());
        }
        return unexploredFrontier;
    }
}
