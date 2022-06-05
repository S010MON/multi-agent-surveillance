package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
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
    private int moveDistance = 20;
    private GraphicsEngine graphicsEngine = new GraphicsEngine(91);
    private Settings settings;
    private Map map;


    public void independentTestSetup()
    {
        settings = FileManager.loadSettings("src/main/resources/AcoTestMap");
        map = new Map(settings);
        map.setHumanActive(false);
        Universe.clearUniverse();
    }

    // Test connectivity and labelling its world
    @Test
    public void testObstacleLabelling()
    {
        independentTestSetup();
        position = new Vector(350, 50);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);
        //Smell pheromones
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        //Visually explore marking obstacles and viable directions
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        GraphCell obstacleCell_1 = Universe.getMemoryGraph(Type.GUARD).getVertexAt(position.add(new Vector(moveDistance, 0)));

        assertTrue(obstacleCell_1.getObstacle());
    }

    //Testing movement
    @Test
    public void testOccupiedLabelling()
    {
        independentTestSetup();
        position = new Vector(100, 100);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);

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
        independentTestSetup();
        position = new Vector(100, 100);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);

        Vector nextPosition = position.add(new Vector(moveDistance, 0));
        agent.updateLocation(nextPosition);

        GraphCell cell = Universe.getMemoryGraph(Type.GUARD).getVertexAt(position);
        assertFalse(cell.getOccupied());
    }

    // Testing visual exploration
    @Test
    public void testExplorationWithinVisualField()
    {
        independentTestSetup();
        position = new Vector(150, 150);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);

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
        position = new Vector(150, 150);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);
        independentTestSetup();

        agent.updateView(graphicsEngine.compute(map, agent));
        assertNotNull(agent.detectCardinalPoint(90));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {throw new RuntimeException("Indicator");});
        assertEquals("Indicator", exception.getMessage());
    }

    @Test
    void movePossible()
    {
        position = new Vector(350, 50);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);
        independentTestSetup();

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
        position = new Vector(150, 150);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);
        independentTestSetup();

        ArrayList<Vector> pheromoneSenseDirections = agent.getPheromoneDirections();
        assertEquals(pheromoneSenseDirections.get(0), new Vector (0, -moveDistance));
        assertEquals(pheromoneSenseDirections.get(3), new Vector(-moveDistance, 0));
    }

    @Test
    public void testPheromoneSmellToDirections()
    {
        position = new Vector(150, 150);
        AcoAgent agent = new AcoMomentum(position, direction, radius, Type.GUARD);
        independentTestSetup();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        assertEquals(agent.getVisualDirectionsToExplore().size(), 4);
    }
}
