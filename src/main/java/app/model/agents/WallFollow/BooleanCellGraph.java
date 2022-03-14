package app.model.agents.WallFollow;

import app.model.agents.Cells.BooleanCell;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.graph.SimpleGraph;

import java.util.HashMap;

public class BooleanCellGraph<Object,DefaultEdge> extends SimpleGraph
{
    @Getter @Setter private BooleanCell initialWallFollowPos;
    @Getter private BooleanCell agentPos;
    @Getter private HashMap<String,BooleanCell> vertices = new HashMap<>();
    @Setter int edge;  // edge length between vertices (moveLength)

    public BooleanCellGraph()
    {
        super(org.jgrapht.graph.DefaultEdge.class);
        // TODO: maybe need to add weights to edges for heuristics later?
    }

    public void updateAgentPos(BooleanCell agentCell, BooleanCell forwardCell,
                               BooleanCell leftCell, BooleanCell rightCell)
    {
        // adds three new vertices to graph (forward, left, right) and updates agent's position vertex
        agentPos = agentCell;
        boolean forwardNotPresent = this.addVertex(forwardCell);
        boolean leftNotPresent = this.addVertex(leftCell);
        boolean rightNotPresent = this.addVertex(rightCell);
        vertices.put(forwardCell.toString(), forwardCell);
        vertices.put(leftCell.toString(), leftCell);
        vertices.put(rightCell.toString(), rightCell);
        System.out.println("VertexSet is: " + this.vertexSet());
        if (forwardNotPresent)  // TODO adding edges here duplicates updateVertex code?
            this.addEdge(agentPos,forwardCell);
        if (leftNotPresent)
            this.addEdge(agentPos,leftCell);
        if (rightNotPresent)
            this.addEdge(agentPos,rightCell);
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
        this.addVertex(vertex);
        vertices.put(vertex.toString(), vertex);  // TODO
        updateVertex(vertex);
    }
}
