package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.Move;
import app.model.agents.ACO.AcoAgent;
import app.model.agents.ACO.AcoAgentLimitedVision;
import app.model.agents.Cells.GraphCell;
import app.model.agents.Team;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AcoTesting_2
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

    //Test linked capabilities

    @Test
    public void testActionsAtWindow()
    {
        position = new Vector(677, 100);
        AcoAgent agent = new AcoAgent(position, direction, radius, Team.GUARD);

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
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, direction, radius, Team.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));

        ArrayList<Vector> pheromoneDirections = agent.getPheromoneDirections();
        ArrayList<Double> pheromones = agent.accessAvaliableCellPheromones(pheromoneDirections);

        assertEquals(pheromones.size(), pheromoneDirections.size());
        assertEquals(0.0, pheromones.get(0));
    }

    @Test
    void movePossible()
    {
        position = new Vector(678, 100);
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, direction, radius, Team.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));
        double angle = direction.getAngle();

        Ray ray = agent.detectCardinalPoint(angle);
        boolean movePossible =  agent.movePossible(ray);

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
