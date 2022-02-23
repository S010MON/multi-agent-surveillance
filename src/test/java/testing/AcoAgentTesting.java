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

import static org.junit.jupiter.api.Assertions.*;

public class AcoAgentTesting
{
    private Vector position = new Vector(20, 20);
    private Vector viewDirection = new Vector(0, 1);
    private double radius = 10;

    Settings settings = FileManager.loadSettings("src/main/resources/map_1.txt");
    GraphicsEngine graphicsEngine = new RayTracing();
    Map map = new Map(settings);

    @Test
    void testAvaliableMovements()
    {
        AcoAgent.initializeWorld(50, 50);
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        agent.updateView(graphicsEngine.compute(map, agent));
        Ray[] cardinalRays = agent.detectCardinalRays();

        ArrayList<Vector> avaliableMovements = agent.determineAvaliableMovements(cardinalRays);

        //Wihtin the map there are 4 avaliable movements. Vector position link already tested
        assertEquals(avaliableMovements.size(), cardinalRays.length);
    }

    @Test
    void testAngleToMovementLink()
    {
        AcoAgent.initializeWorld(50, 50);
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        Vector link_0 = agent.angleToGridMovementLink(0);
        Vector link_180 = agent.angleToGridMovementLink(180);

        assertEquals(link_0, new Vector(1, 0));
        assertEquals(link_180, new Vector(-1, 0));
    }

    @Test
    void testCardinalPointDetection()
    {
        AcoAgent.initializeWorld(50, 50);
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        double targetAngle =63;
        agent.updateView(graphicsEngine.compute(map, agent));

        Ray cardinalRay = agent.detectCardinalPoint(targetAngle);
        assertTrue(agent.approximateAngleRange(cardinalRay.angle(), targetAngle));
    }

    @Test
    void testCardinalPointNonDetection()
    {
        AcoAgent.initializeWorld(50, 50);
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        double targetAngle =-163;
        agent.updateView(graphicsEngine.compute(map, agent));

        Ray cardinalRay = agent.detectCardinalPoint(targetAngle);
        assertNull(cardinalRay);
    }

    @Test
    void test4CardinalAngles()
    {
        AcoAgent.initializeWorld(50, 50);
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        agent.updateView(graphicsEngine.compute(map, agent));

        Ray[] cardinalRays = agent.detectCardinalRays();
        int[] cardinalAngles = agent.getCardinalAngles();

        assertTrue(agent.approximateAngleRange(cardinalRays[0].angle(), cardinalAngles[0]));
        assertTrue(agent.approximateAngleRange(cardinalRays[1].angle(), cardinalAngles[1]));
        assertTrue(agent.approximateAngleRange(cardinalRays[2].angle(), cardinalAngles[2]));
        assertTrue(agent.approximateAngleRange(cardinalRays[3].angle(), cardinalAngles[3]));
    }
}
