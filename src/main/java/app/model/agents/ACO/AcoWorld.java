package app.model.agents.ACO;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;
import app.model.agents.MemoryGraph;
import app.model.agents.World;
import org.jgrapht.graph.DefaultWeightedEdge;

public class AcoWorld extends World
{
    public AcoWorld(MemoryGraph G)
    {
        super(G);
    }

    public GraphCell addNewVertex(Vector position)
    {
        Vector vertexCentre = G.determineVertexCentre(position);
        GraphCell cell = new GraphCell(vertexCentre);
        G.addVertex(cell);

        G.vertices.put(G.keyGenerator(position), cell);
        connectNeighbouringVertices(cell);
        return cell;
    }

    public void connectNeighbouringVertices(GraphCell currentCell)
    {
        Vector currentPosition = currentCell.getPosition();
        for(java.lang.Object cardinalObject: G.cardinalDirections.values())
        {
            Vector cardinal = (Vector) cardinalObject;
            Vector resultingPosition = currentPosition.add(cardinal);
            GraphCell neighbouringCell = G.vertices.get(G.keyGenerator(resultingPosition));
            if(neighbouringCell != null && !G.containsEdge(currentCell, neighbouringCell))
            {
                DefaultWeightedEdge edge = (DefaultWeightedEdge) G.addEdge(currentCell, neighbouringCell);
                G.setEdgeWeight(edge, G.travelDistance);
            }
        }
    }
}
