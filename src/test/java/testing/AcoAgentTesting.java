package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.Move;
import app.model.agents.ACO.AcoAgent;
import app.model.agents.ACO.AcoMomentum;
import app.model.agents.Cells.GraphCell;
import app.model.agents.Universe;
import app.model.Type;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AcoAgentTesting
{
    private Vector position;
    private Vector direction = new Vector(1, 0);
    private int radius = 10;
    private double viewingDistance = 100;
    private int moveDistance = 20;
    private GraphicsEngine graphicsEngine = new GraphicsEngine(91);
    private Settings settings = FileManager.loadSettings("src/main/resources/map_1.txt");
    private Map map = new Map(settings);

    public void agentSetup(AcoAgent agent)
    {
        agent.setMoveLength(moveDistance);
        agent.setVisionDistance(viewingDistance);
    }

    // Test connectivity and labelling its world
    @Test
    public void testObstacleLabelling()
    {
        //Should detect obstacles on movement (distance, 0) and (o, distance)
        Vector wallPosition = new Vector(1394.1540153394126, 889.0381125375702);
        AcoAgent agent = new AcoMomentum(wallPosition, direction, radius, Type.GUARD);

        //Smell pheromones
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        //Visually explore marking obstacles and viable directions
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        GraphCell obstacleCell_1 = Universe.getMemoryGraph(Type.GUARD).getVertexAt(wallPosition.add(new Vector(moveDistance, 0)));
        GraphCell obstacleCell_2 = Universe.getMemoryGraph(Type.GUARD).getVertexAt(wallPosition.add(new Vector(0, moveDistance)));

        assertTrue(obstacleCell_1.getObstacle());
        assertTrue(obstacleCell_2.getObstacle());
    }

    //Testing movement
    @Test
    public void testOccupiedLabelling()
    {
        position = new Vector(190, 350);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);
        agentSetup(agent);

        assertTrue(Universe.getMemoryGraph(agent.getType()).getVertexAt(position).getOccupied());

        // Detect pheromones
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        //Make move
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        // Explore remaining direction
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        //Make move
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        Vector newLocation = position.add(new Vector(moveDistance, 0));
        agent.updateLocation(newLocation);
        assertTrue(Universe.getMemoryGraph(agent.getType()).getVertexAt(newLocation).getOccupied());
        assertFalse(Universe.getMemoryGraph(agent.getType()).getVertexAt(position).getOccupied());
    }

    @Test
    public void testLeavingCell()
    {
        position = new Vector(170, 350);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);
        agentSetup(agent);

        agent.setPreviousMove(new Move(new Vector(170, 350), new Vector(moveDistance, 0)));

        agent.updateLocation(new Vector(190, 350));

    }

    // Testing visual exploration
    @Test
    public void testExplorationWithinVisualField()
    {
        position = new Vector(210, 350);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);
        agentSetup(agent);

        //Smell pheromones
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        // Detect possible movements
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        // One direction to still explore
        assertEquals(agent.getVisualDirectionsToExplore().size(), 1);
        assertEquals(agent.getPossibleMovements().size(), 3);

        //Explore one remaining unknown direction
        agent.updateView(graphicsEngine.compute(map, agent));

        agent.move();
        assertEquals(agent.getPossibleMovements().size(), 4);
    }

    //Test vision capabilities
    @Test
    public void testCardinalPointDetection()
    {
        position = new Vector(210, 370);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));

        assertNotNull(agent.detectCardinalPoint(90));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {throw new RuntimeException("Indicator");});
        assertEquals("Indicator", exception.getMessage());
    }

    @Test
    void movePossible()
    {
        position = new Vector(678, 100);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));
        double angle = direction.getAngle();

        Ray ray = agent.detectCardinalPoint(angle);
        boolean movePossible =  agent.moveEvaluation(ray);
        assertFalse(movePossible);
    }

    //Test smell capabilities
    @Test
    public void testPheromoneSenseDirections()
    {
        position = new Vector(250, 350);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);
        agentSetup(agent);

        ArrayList<Vector> pheromoneSenseDirections = agent.getPheromoneDirections();
        assertEquals(pheromoneSenseDirections.get(0), new Vector (0, -moveDistance));
        assertEquals(pheromoneSenseDirections.get(3), new Vector(-moveDistance, 0));
    }

    @Test
    public void testPheromoneSmellToDirections()
    {
        position = new Vector(250, 390);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);
        agentSetup(agent);

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        assertEquals(agent.getVisualDirectionsToExplore().size(), 4);
    }
}
