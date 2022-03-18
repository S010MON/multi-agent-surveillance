package app.model.agents.WallFollow;

import app.controller.linAlg.Vector;
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
    ArrayList<BooleanCell> lastPositions = new ArrayList<>();
    @Getter BooleanCell prevAgentVertex = new BooleanCell();

    public BooleanCellGraph()
    {
        super(org.jgrapht.graph.DefaultEdge.class);
    }

    public void updateAgentPos(BooleanCell agentCell, BooleanCell forwardCell,
                               BooleanCell leftCell, BooleanCell rightCell)
    {
        agentPos = agentCell;
        //TODO
        /*if (agentPos.getObstacle())
        {
            throw new RuntimeException("Agent is in obstacle!");
        }*/
        updateLastPositions();
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
            else if (!vertex.getObstacle() && edgesOf(vertex).size() > 4)
            {
                throw new RuntimeException("Vertex has too many neighbours!");
            }
        }
        if (DEBUG)
        {
            System.out.println("Total nr of vertices explored: " + vertexSet().size());
            System.out.println("Nr of vertices with unexplored neighbours: " + unexploredFrontier.size());
        }
        return unexploredFrontier;
    }

    public Vector getNeighbourDir(BooleanCell vertex)
    {
        if (agentPos.getX() == vertex.getX() && vertex.getY() == agentPos.getY())
        {
            return new Vector(0,0);
        }
        else if (agentPos.getX() == vertex.getX() && vertex.getY() < agentPos.getY())
        {
            return new Vector(0,-1);  // north of agent
        }
        else if (agentPos.getX() == vertex.getX() && vertex.getY() > agentPos.getY())
        {
            return new Vector(0,1);  // south of agent
        }
        else if (agentPos.getY() == vertex.getY() && vertex.getX() < agentPos.getX())
        {
            return new Vector(-1,0);  // west of agent
        }
        else if (agentPos.getY() == vertex.getY() && vertex.getX() > agentPos.getX())
        {
            return new Vector(1,0);  // east of agent
        }
        throw new RuntimeException("Vertex that was checked is not a neighbour of agent's vertex! " +
                "Agent vertex: " + agentPos + " and checked vertex: " + vertex);
    }

    public void updateLastPositions()
    {
        if (lastPositions.size() >= 8) {
            lastPositions.remove(0);
        }
        if (lastPositions.size() > 0)
        {
            prevAgentVertex = lastPositions.get(lastPositions.size()-1);
        }
        lastPositions.add(agentPos);
    }

    public boolean agentInStuckMovement()
    {
        ArrayList<BooleanCell> diffVertices = new ArrayList<>();
        if (lastPositions.size() == 8)
        {
            for (BooleanCell vertex : lastPositions)
            {
                for (BooleanCell other: lastPositions)
                {
                    if (!diffVertices.contains(other))
                    {
                        diffVertices.add(other);
                        if (diffVertices.indexOf(other) == 7 && diffVertices.size() < 5)
                        {
                            return true;
                        }
                    }
                    if (!vertex.equals(other))
                    {
                        if (Math.abs(vertex.getX() - other.getX()) > edge || Math.abs(vertex.getY() - other.getY()) > edge)
                        {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean agentStuckInVertex()
    {
        for (BooleanCell vertex : lastPositions)
        {
            if (vertex != agentPos)
            {
                return false;
            }
        }
        return true;
    }

    public void moveAgentBack()
    {
        if (lastPositions.size() >= 3) {
            agentPos.setObstacle(true);  // TODO added this
            agentPos = prevAgentVertex;
            lastPositions.remove(lastPositions.size()-1);
            prevAgentVertex = lastPositions.get(lastPositions.size()-2);
        }

    }
}
