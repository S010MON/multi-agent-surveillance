package testing;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;
import app.model.agents.MemoryGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MemoryGraphTesting
{
    private int distance = 20;

    @Test
    public void testVertexCenters()
    {
        MemoryGraph<GraphCell, DefaultEdge> world = new MemoryGraph<>(distance);

        Vector agentPosition_1 = new Vector(37, 78);
        Vector vertexCentre = world.determineVertexCentre(agentPosition_1);
        assertEquals(vertexCentre, new Vector(30, 70));
    }

    @Test
    public void testVerticesEqual()
    {
        Vector agentPosition_1 = new Vector(30, 70);
        GraphCell vertex_1 = new GraphCell(agentPosition_1, false, false, 2);

        Vector agentPosition_2 = new Vector(30, 70);
        GraphCell vertex_2 = new GraphCell(agentPosition_2, false, true, 2);

        assertEquals(vertex_1, vertex_2);
    }

    @Test
    public void testVertexAdd_Adjust()
    {
        MemoryGraph<GraphCell, DefaultEdge> world = new MemoryGraph<>(distance);
        Vector agentPosition_1 = new Vector(37, 78);
        world.add_or_adjust_Vertex(agentPosition_1, false, false, 2);

        assertEquals(world.getVertices().size(), 1);

        Vector agentPosition_2 = new Vector(23, 74);
        world.add_or_adjust_Vertex(agentPosition_2, false, true, 1);

        Set<GraphCell> vertexSet = world.vertexSet();
        assertEquals(vertexSet.size(), 1);
        assertEquals(world.getVertexAt(agentPosition_1).getPheromone(), 3);
    }
}
