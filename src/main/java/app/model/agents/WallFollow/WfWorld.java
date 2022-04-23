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
            cell = addNewVertex(position);
        }
        connectNeighbouringVertices(cell);
    }

    @Override
    protected GraphCell addNewVertex(Vector position)
    {
        Vector vertexCentre = determineVertexCentre(position);
        GraphCell cell = new GraphCell(vertexCentre);
        super.addVertex(cell);
        super.getVertices().put(keyGenerator(position), cell);
        return cell;
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
                super.addVertex(neighbouringCell);
            }

            if(neighbouringCell != null && !containsEdge(currentCell, neighbouringCell))
            {
                DefaultWeightedEdge edge = (DefaultWeightedEdge) super.addEdge(currentCell, neighbouringCell);
                super.setEdgeWeight(edge, super.getTravelDistance());
            }
        }
    }
}
