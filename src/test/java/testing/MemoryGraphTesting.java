package testing;

import app.controller.linAlg.Vector;
import app.model.agents.Cells.GraphCell;
import app.model.agents.MemoryGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        world.add_or_adjust_Vertex(agentPosition_1);
        world.leaveVertex(agentPosition_1, 2);

        assertEquals(world.getVertices().size(), 1);

        Vector agentPosition_2 = new Vector(23, 74);
        world.add_or_adjust_Vertex(agentPosition_2);
        world.leaveVertex(agentPosition_2, 1);

        Set<GraphCell> vertexSet = world.vertexSet();
        assertEquals(vertexSet.size(), 1);
        assertEquals(world.getVertexAt(agentPosition_1).getPheromone(), 3);
    }

    @Test
    public void testNeighbourConnections()
    {
        MemoryGraph<GraphCell, DefaultEdge> world = new MemoryGraph<>(distance);

        //Add centre vertex
        Vector agentPosition_1 = new Vector(37, 78);
        world.add_or_adjust_Vertex(agentPosition_1);
        world.leaveVertex(agentPosition_1, 2);

        //Add left vertex
        Vector agentPosition_2 = new Vector(17, 74);
        world.add_or_adjust_Vertex(agentPosition_2);
        world.leaveVertex(agentPosition_2, 1);

        //Check & verify left edge
        GraphCell cell_centre = world.getVertexAt(agentPosition_1);
        GraphCell cell_1 = world.getVertexAt(agentPosition_2);
        assertTrue(world.containsEdge(cell_centre, cell_1));

        //Add right vertex and verify
        Vector agentPosition_3 = new Vector(57, 74);
        world.add_or_adjust_Vertex(agentPosition_3);
        world.leaveVertex(agentPosition_3, 2);
        GraphCell cell_2 = world.getVertexAt(agentPosition_3);
        assertTrue(world.containsEdge(cell_centre, cell_2));

        //Add above vertex and verify
        Vector agentPosition_4 = new Vector(37, 94);
        world.add_or_adjust_Vertex(agentPosition_4);
        world.leaveVertex(agentPosition_4, 2);
        GraphCell cell_3 = world.getVertexAt(agentPosition_3);
        assertTrue(world.containsEdge(cell_centre, cell_3));

        //Verify edge does not exist
        assertFalse(world.containsEdge(cell_2, cell_3));
    }

    @Test
    public void aggregateTesting()
    {
        MemoryGraph<GraphCell, DefaultEdge> world = new MemoryGraph<>(distance);

        //Current vertex
        Vector agentPosition_1 = new Vector(37, 78);
        world.add_or_adjust_Vertex(agentPosition_1);
        world.leaveVertex(agentPosition_1, 2);

        Vector agentPosition_2 = new Vector(57, 78);
        world.add_or_adjust_Vertex(agentPosition_2);
        world.leaveVertex(agentPosition_2, 2);

        Vector agentPosition_3 = new Vector(77, 78);
        world.add_or_adjust_Vertex(agentPosition_3);
        world.leaveVertex(agentPosition_3, 2);

        Vector agentPosition_4 = new Vector(77, 58);
        world.add_or_adjust_Vertex(agentPosition_4);
        world.leaveVertex(agentPosition_4, 2);

        Vector agentPosition_5 = new Vector(77, 98);
        world.add_or_adjust_Vertex(agentPosition_5);
        world.leaveVertex(agentPosition_5, 2);

        Vector agentPosition_6 = new Vector(97, 78);
        world.add_or_adjust_Vertex(agentPosition_6);
        world.leaveVertex(agentPosition_6, 2);

        Vector agentPosition_7 = new Vector(97, 58);
        world.add_or_adjust_Vertex(agentPosition_7);
        world.leaveVertex(agentPosition_7, 2);

        Vector agentPosition_8 = new Vector(97, 98);
        world.add_or_adjust_Vertex(agentPosition_8);
        world.leaveVertex(agentPosition_8, 2);

        Vector agentPosition_9 = new Vector(117, 78);
        world.add_or_adjust_Vertex(agentPosition_9);
        world.leaveVertex(agentPosition_9, 2);

        double aggregatePheromone = world.aggregateCardinalPheromones(agentPosition_1, new Vector(20, 0));
        assertEquals(aggregatePheromone, 4);
    }

    @Test
    public void testObstacleLabelling()
    {
        Vector agentPosition = new Vector(37, 78);
        Vector movement = new Vector(0, 20);

        MemoryGraph<GraphCell, DefaultWeightedEdge> world = new MemoryGraph<>(distance);
        world.add_or_adjust_Vertex(agentPosition);

        world.setVertexAsObstacle(agentPosition, movement);

        GraphCell obstacle = world.getVertexAt(agentPosition.add(movement));
        assertTrue(obstacle.getObstacle());
    }

    //TODO fill in this test -> Code in aggregate fetching
    public void testAggregateWithObstacle()
    {

    }
}
