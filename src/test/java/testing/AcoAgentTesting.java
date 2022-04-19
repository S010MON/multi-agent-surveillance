package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.Move;
import app.model.agents.ACO.AcoAgent;
import app.model.agents.Team;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AcoAgentTesting
{
    private Vector position = new Vector(250, 350);
    private Vector direction = new Vector(1, 0);
    private int radius = 10;
    private double viewingDistance = 100;
    private int moveDistance = 20;
    GraphicsEngine graphicsEngine = new GraphicsEngine(80);
    Settings settings = FileManager.loadSettings("src/main/resources/map_1.txt");
    Map map = new Map(settings);

    public void agentSetup(AcoAgent agent)
    {
        agent.setDistance(moveDistance);
        agent.setVisionDistance(viewingDistance);
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
    @Test
    public void testStuckAtPortal()
    {
        position = new Vector(465.148150363186, 343.4177615482815);
        AcoAgent agent = new AcoAgent(position, direction, radius, Team.GUARD);

        agent.setPreviousMove(new Move(position, new Vector(moveDistance, 0)));
        agent.updateLocation(new Vector(465.148150363186, 343.4177615482815));

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agentVisualExploration(agent);

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

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

        Vector moveInMemory_1 = new Vector(-20, 0);
        agent.setPreviousMove(new Move(position, moveInMemory_1));
        agent.setMoveFailed(true);
        agent.updateView(graphicsEngine.compute(map, agent));
        Move movementFromMemory  = agent.move();
        assertEquals(movementFromMemory.getDeltaPos(), new Vector(0, 20));

        Vector moveInMemory_2 = new Vector(0, 20);
        agent.setPreviousMove(new Move(position, moveInMemory_2));
        agent.setMoveFailed(true);
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        assertTrue(agent.getShortTermMemory().containsKey(moveInMemory_2.hashCode()));

        //Movement using memory
        agent.setMoveFailed(false);
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        assertEquals(agent.getVisualDirectionsToExplore().size(), 3);
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

    //Test smell capabilities
    @Test
    public void testPheromoneSenseDirections()
    {
        AcoAgent agent = new AcoAgent(position, direction, radius, Team.GUARD);
        agentSetup(agent);

        ArrayList<Vector> pheromoneSenseDirections = agent.getPheromoneDirections();
        assertEquals(pheromoneSenseDirections.get(0), new Vector (0, moveDistance));
        assertEquals(pheromoneSenseDirections.get(3), new Vector(-moveDistance, 0));
    }

    @Test
    public void testAgentUpdateReflectingToWorld()
    {
        AcoAgent agent = new AcoAgent(position, direction, radius, Team.GUARD);
        agentSetup(agent);


    }

}
