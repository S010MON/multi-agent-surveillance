package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.graphicsEngine.RayTracing;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.agents.ACO.AcoAgent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcoAgentTesting
{
    private Vector position = new Vector(20, 20);
    private Vector viewDirection = new Vector(0, 1);
    private double radius = 10;

    Settings settings = FileManager.loadSettings("src/main/resources/map_1.txt");
    GraphicsEngine graphicsEngine = new RayTracing();
    Map map = new Map(settings);

    @Test
    void testCardinalPointDetection()
    {
        AcoAgent.initializeWorld(50, 50);
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        double targetAngle =63;
        agent.updateView(graphicsEngine.compute(map, agent));

        Ray cardinalRay = agent.detectCardialPoint(targetAngle);
        assertTrue(agent.approximateAngleRange(cardinalRay.angle(), targetAngle));
    }

    @Test
    void testCardinalPointNonDetection()
    {
        AcoAgent.initializeWorld(50, 50);
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        double targetAngle =-163;
        agent.updateView(graphicsEngine.compute(map, agent));

        Ray cardinalRay = agent.detectCardialPoint(targetAngle);
        assertNull(cardinalRay);
    }
}
