package app.model.agents.WallFollow;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;
import app.model.agents.MemoryGraph;

public class WfWorld<Object, DefaultWeightedEdge> extends MemoryGraph
{
    public WfWorld(int distance)
    {
        super(distance);
    }

    @Override
    public void add_or_adjust_Vertex(Vector position)
    {
        GraphCell cell = getVertexAt(position);

        if(cell != null)
        {
            modifyVertex(cell);
        }
        else
        {
            Vector vertexCentre = determineVertexCentre(position);
            cell = new GraphCell(vertexCentre);
            addVertex(cell);
            super.getVertices().put(keyGenerator(position), cell);
        }
        connectNeighbouringVertices(cell);
    }

    @Override
    public void connectNeighbouringVertices(GraphCell currentCell)
    {
        Vector currentPosition = currentCell.getPosition();
        for(java.lang.Object cardinalObject: super.getCardinalDirections().values())
        {
            Vector cardinal = (Vector) cardinalObject;
            GraphCell neighbouringCell;
            Vector resultingPosition = currentPosition.add(cardinal);
            String neighbourKey = keyGenerator(resultingPosition);
            if (super.getVertices().containsKey(neighbourKey))
            {
                neighbouringCell = (GraphCell) super.getVertices().get(neighbourKey);
            }
            else
            {
                neighbouringCell = new GraphCell(resultingPosition);
                super.getVertices().put(neighbourKey,neighbouringCell);
                addVertex(neighbouringCell);
            }
            //GraphCell neighbouringCell = vertices.get(keyGenerator(resultingPosition));

            if(neighbouringCell != null && !containsEdge(currentCell, neighbouringCell))
            {
                DefaultWeightedEdge edge = (DefaultWeightedEdge) this.addEdge(currentCell, neighbouringCell);
                this.setEdgeWeight(edge, super.getTravelDistance());
            }
        }
    }
}
