package app.model.agents.WallFollow;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;
import app.model.agents.MemoryGraph;
import app.model.agents.World;
import org.jgrapht.graph.DefaultWeightedEdge;

public class WfWorld extends World
{
    public WfWorld(MemoryGraph G)
    {
        super(G);
    }

    public void add_or_adjust_Vertex(Vector position)
    {
        GraphCell cell = G.getVertexAt(position);
        if(cell != null)
        {
            modifyVertex(cell);
        }
        else
        {
            cell = addNewVertex(position);
        }
        connectNeighbouringVertices(cell);
    }

    @Override
    public void connectNeighbouringVertices(GraphCell currentCell)
    {
        Vector currentPosition = currentCell.getPosition();
        for(java.lang.Object cardinalObject: G.cardinalDirections.values())
        {
            Vector cardinal = (Vector) cardinalObject;
            GraphCell neighbouringCell;
            Vector resultingPosition = currentPosition.add(cardinal);
            String neighbourKey = G.keyGenerator(resultingPosition);
            if (G.vertices.containsKey(neighbourKey))
            {
                neighbouringCell = (GraphCell) G.vertices.get(neighbourKey);
            }
            else
            {
                neighbouringCell = new GraphCell(resultingPosition);
                G.vertices.put(neighbourKey,neighbouringCell);
                G.addVertex(neighbouringCell);
            }

            if(neighbouringCell != null && !G.containsEdge(currentCell, neighbouringCell))
            {
                DefaultWeightedEdge edge = (DefaultWeightedEdge) G.addEdge(currentCell, neighbouringCell);
                G.setEdgeWeight(edge, G.travelDistance);
            }
        }
    }
}
