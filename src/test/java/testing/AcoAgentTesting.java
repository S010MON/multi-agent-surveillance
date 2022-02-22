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

public class AcoAgentTesting
{
    private Vector position = new Vector(20, 20);
    private Vector viewDirection = new Vector(0, 1);
    private double radius = 10;

    Settings settings = FileManager.loadSettings("src/main/resources/map_1.txt");
    GraphicsEngine graphicsEngine = new RayTracing();
    Map map = new Map(settings);

    //TODO Finish testing using either brute force or binary search
    @Test
    void testCardinalPointDetection()
    {
        AcoAgent.initializeWorld(50, 50);
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        double targetAngle = 90;
        agent.updateView(graphicsEngine.compute(map, agent));

        agent.detectCardialPoint(targetAngle);
    }
}
