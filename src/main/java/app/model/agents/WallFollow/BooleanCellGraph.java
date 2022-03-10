package app.model.agents.WallFollow;

import app.model.agents.Cells.BooleanCell;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.graph.SimpleGraph;

import java.util.HashMap;

public class BooleanCellGraph<Object,DefaultEdge> extends SimpleGraph
{
    @Setter private BooleanCell initialPos;
    @Getter private BooleanCell agentPos;
    @Getter private HashMap<String,BooleanCell> vertices = new HashMap<>();

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
        vertices.put(forwardCell.getXY(), forwardCell);
        vertices.put(leftCell.getXY(), leftCell);
        vertices.put(rightCell.getXY(), rightCell);
        if (forwardNotPresent) this.addEdge(agentPos,forwardCell);
        if (leftNotPresent) this.addEdge(agentPos,leftCell);
        if (rightNotPresent) this.addEdge(agentPos,rightCell);
        updateVertex(forwardCell);
        updateVertex(leftCell);
        updateVertex(rightCell);
    }

    public void updateVertex(BooleanCell vertex)
    {
        int vertexX = vertex.getX();
        int vertexY = vertex.getY();
        if (vertices.containsKey(vertexX + " " + (vertexY-1)))  // north neighbour
        {
            this.addEdge(vertex,vertices.get(vertexX + " " + (vertexY-1)));
        }
        if (vertices.containsKey(vertexX-1 + " " + (vertexY)))  // east neighbour
        {
            this.addEdge(vertex,vertices.get(vertexX-1 + " " + (vertexY)));
        }
        if (vertices.containsKey(vertexX + " " + (vertexY+1)))  // south neighbour
        {
            this.addEdge(vertex,vertices.get(vertexX + " " + (vertexY+1)));
        }
        if (vertices.containsKey(vertexX+1 + " " + (vertexY)))  // west neighbour
        {
            this.addEdge(vertex,vertices.get(vertexX+1 + " " + (vertexY)));
        }
    }
}
