package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.agents.ACO.AcoAgent360Vision;
import app.model.agents.ACO.AcoAgentLimitedVision;
import app.model.agents.ACO.AcoGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AcoAgentLimitedVisionTesting
{
    private Vector position = new Vector(250, 350);
    private Vector viewDirection = new Vector(1, 0);
    private double radius = 10;

    Settings settings = FileManager.loadSettings("src/main/resources/map_1.txt");
    GraphicsEngine graphicsEngine = new GraphicsEngine(90);
    Map map = new Map(settings);

    @BeforeEach
    void init()
    {
        AcoGrid world = new AcoGrid(settings.getWidth(), settings.getHeight(), 1);
        AcoAgent360Vision.initializeWorld(world);
        AcoAgent360Vision.clearAcoCounts();
    }

    @Test
    void testDirectionsToVisiblyExplore()
    {
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius);
        agent.updateView(graphicsEngine.compute(map, agent));

        ArrayList<Vector> pheromoneDirections = agent.getPheromoneDirections();
        ArrayList<Double> pheromones = agent.accessAvaliableCellPheromones(pheromoneDirections);

        assertEquals(pheromones.size(), pheromoneDirections.size());
        assertEquals(0.0, pheromones.get(0));
    }

    @Test
    void testDirectionsToVisiblyExploreAgainstWall()
    {
        position = new Vector(0, 0);
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius);
        agent.updateView(graphicsEngine.compute(map, agent));

        ArrayList<Vector> pheromoneDirections = agent.getPheromoneDirections();
        ArrayList<Double> pheromones = agent.accessAvaliableCellPheromones(pheromoneDirections);

        //No gird access off of map boundaries (Maintains direction order)
        assertEquals(pheromones.get(0), 0.0);
        assertEquals(pheromones.get(1), 0.0);
        assertNull(pheromones.get(2));
        assertNull(pheromones.get(3));
    }

    @Test
    void testPheromoneSenseDirections()
    {
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius);
        agent.updateView(graphicsEngine.compute(map, agent));

        ArrayList<Vector> directions = agent.getPheromoneDirections();
        assertEquals(directions.size(), agent.getCardinalAngles().length);
    }

    @Test
    void movePossible()
    {
        position = new Vector(678, 100);
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius);
        agent.updateView(graphicsEngine.compute(map, agent));
        double angle = viewDirection.getAngle();

        Ray ray = agent.detectCardinalPoint(angle);
        boolean movePossible =  agent.movePossible(ray);

        assertFalse(movePossible);
    }

    @Test
    void testCardinalPointDetection()
    {
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius);
        agent.updateView(graphicsEngine.compute(map, agent));
        double angle = viewDirection.getAngle();

        Ray ray = agent.detectCardinalPoint(angle);

        assertNotEquals(ray.getU().getX(), ray.getV().getX());
        assertEquals(ray.getU().getY(), ray.getV().getY());
    }
}
