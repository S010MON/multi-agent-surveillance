package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.Move;
import app.model.agents.ACO.AcoAgent;
import app.model.agents.Cells.GraphCell;
import app.model.agents.Team;

import app.model.agents.Universe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AcoAgentTesting
{
    private Vector position = new Vector(250, 350);
    private Vector direction = new Vector(1, 0);
    private int radius = 10;
    private double viewingDistance = 100;
    private int moveDistance = 20;
    private GraphicsEngine graphicsEngine = new GraphicsEngine(80);
    private Settings settings = FileManager.loadSettings("src/main/resources/map_1.txt");
    private Map map = new Map(settings);

    public void agentSetup(AcoAgent agent)
    {
        agent.setDistance(moveDistance);
        agent.setVisionDistance(viewingDistance);
    }

    @BeforeEach
    public void clearMemoryAndWorld()
    {

    }

    //Bug testing
    public void agentVisualExploration(AcoAgent agent)
    {
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
    }

    @Test
    public void testStuckAtWindow()
    {
        position = new Vector(234.88019900183576, 63.27721732577038);
        AcoAgent agent = new AcoAgent(position, direction, radius, Team.GUARD);
        agentSetup(agent);
        agent.setPreviousMove(new Move(position, new Vector(0, moveDistance)));

        agent.updateLocation(new Vector(234.88019900183576, 83.27721732577038));

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agentVisualExploration(agent);

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.setMoveFailed(true);
        agent.updateView(graphicsEngine.compute(map, agent));
        Move moveAfterStuck = agent.move();

        assertNotEquals(moveAfterStuck.getDeltaPos(), new Vector(0, moveDistance));
    }

    //Test linked capabilities
    @Test
    public void testActionsAtWindowUsingMemory()
    {
        position = new Vector(677, 100);
        AcoAgent agent = new AcoAgent(position, direction, radius, Team.GUARD);

        //Agent smells pheromones
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        //Agent looking around...
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        //Testing
        Vector moveInMemory_1 = new Vector(-moveDistance, 0);
        agent.setPreviousMove(new Move(position, moveInMemory_1));
        agent.setMoveFailed(true);
        agent.updateView(graphicsEngine.compute(map, agent));
        Move movementFromMemory  = agent.move();
        assertEquals(movementFromMemory.getDeltaPos(), new Vector(0, -moveDistance));

        Vector moveInMemory_2 = new Vector(0, -moveDistance);
        agent.setPreviousMove(new Move(position, moveInMemory_2));
        agent.setMoveFailed(true);
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.setMoveFailed(true);
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        Vector expected = new Vector(0, moveDistance);
        assertTrue(agent.getShortTermMemory().containsKey(expected.hashCode()));

        //Movement using memory
        agent.setMoveFailed(false);
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        assertEquals(agent.getVisualDirectionsToExplore().size(), 2);
    }

    @Test
    public void testPheromoneToMovement()
    {
        AcoAgent agent = new AcoAgent(position, direction, radius, Team.GUARD);
        agentSetup(agent);
        agent.updateView(graphicsEngine.compute(map, agent));

        //Detect pheromones
        agent.move();
        assertEquals(agent.getVisualDirectionsToExplore().size(), 4);

        //Visually explore
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        assertEquals(agent.getPossibleMovements().size(), 3);

        agent.updateView(graphicsEngine.compute(map, agent));
        Move move = agent.move();
        assertNotEquals(move.getDeltaPos(), new Vector(0,0));
    }

    //Test vision capabilities
    @Test
    public void testCardinalPointDetection()
    {
        AcoAgent agent = new AcoAgent(position, direction, radius, Team.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));

        assertNotNull(agent.detectCardinalPoint(90));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {throw new RuntimeException("Indicator");});
        assertEquals("Indicator", exception.getMessage());
    }

    @Test
    void testDirectionsToVisiblyExplore()
    {
        AcoAgent agent = new AcoAgent(position, direction, radius, Team.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));

        ArrayList<Vector> pheromoneDirections = agent.getPheromoneDirections();
        ArrayList<Double> pheromones = agent.accessAvailableCellAggregatePheromones();

        assertEquals(pheromones.size(), pheromoneDirections.size());
        assertEquals(0.0, pheromones.get(0));
    }

    @Test
    void movePossible()
    {
        position = new Vector(678, 100);
        AcoAgent agent = new AcoAgent(position, direction, radius, Team.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));
        double angle = direction.getAngle();

        Ray ray = agent.detectCardinalPoint(angle);
        boolean movePossible =  agent.moveEvaluation(ray);
        assertFalse(movePossible);
    }

    @Test
    public void testObstacleLabelling()
    {
        //Should detect obstacles on movement (distance, 0) and (o, distance)
        Vector wallPosition = new Vector(1394.1540153394126, 889.0381125375702);
        AcoAgent agent = new AcoAgent(wallPosition, direction, radius, Team.GUARD);

        //Smell pheromones
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        //Visually explore marking obstacles and viable directions
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        GraphCell obstacleCell_1 = Universe.getMemoryGraph(Team.GUARD).getVertexAt(wallPosition.add(new Vector(moveDistance, 0)));
        GraphCell obstacleCell_2 = Universe.getMemoryGraph(Team.GUARD).getVertexAt(wallPosition.add(new Vector(0, moveDistance)));

        assertTrue(obstacleCell_1.getObstacle());
        assertTrue(obstacleCell_2.getObstacle());
    }

    //Test smell capabilities
    @Test
    public void testPheromoneSenseDirections()
    {
        AcoAgent agent = new AcoAgent(position, direction, radius, Team.GUARD);
        agentSetup(agent);

        ArrayList<Vector> pheromoneSenseDirections = agent.getPheromoneDirections();
        assertEquals(pheromoneSenseDirections.get(0), new Vector (0, -moveDistance));
        assertEquals(pheromoneSenseDirections.get(3), new Vector(-moveDistance, 0));
    }
}
