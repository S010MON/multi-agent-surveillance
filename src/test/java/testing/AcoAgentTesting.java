package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.graphicsEngine.RayTracing;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.map.Move;
import app.model.agents.ACO.AcoAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AcoAgentTesting
{
    private Vector position = new Vector(14, 12);
    private Vector viewDirection = new Vector(0, 1);
    private double radius = 10;

    Settings settings = FileManager.loadSettings("src/main/resources/map_1.txt");
    GraphicsEngine graphicsEngine = new RayTracing();
    Map map = new Map(settings);

    @BeforeEach
    void init()
    {
        AcoAgent.initializeWorld(settings.getWidth(), settings.getHeight());
    }

    @Test
    void testBoundaryMovementEdgeCase()
    {
        position = new Vector(91, 94);
        Vector nextPosition = new Vector(91, 95);
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);
        agent.updateLocation(nextPosition);
        assertEquals(agent.getPosition(), nextPosition);
    }

    @Test
    void testMove()
    {
        Vector position_2 = position.add(new Vector(1, 0));
        Vector position_3 = position.add(new Vector(-1, 0));
        Vector position_4 = position.add(new Vector(0, -1));

        AcoAgent agent_1 = new AcoAgent(position, viewDirection, radius);
        AcoAgent agent_2 = new AcoAgent(position_2, viewDirection, radius);
        AcoAgent agent_3 = new AcoAgent(position_3, viewDirection, radius);
        AcoAgent agent_4 = new AcoAgent(position_4, viewDirection, radius);

        agent_1.updateView(graphicsEngine.compute(map, agent_1));
        Move agentMove = agent_1.move();
        assertEquals(agentMove.getDeltaPos(), new Vector(0, 1));
    }

    @Test
    void testDetermineEquivalentMoves3D()
    {
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);
        agent.updateView(graphicsEngine.compute(map, agent));

        Vector nextPosition = new Vector(14, 13);
        agent.updateLocation(nextPosition);

        Ray[] cardinalRays = agent.detectCardinalRays();
        ArrayList<Vector> availableMovements = agent.determineAvailableMovements(cardinalRays);
        ArrayList<Double> pheromoneValues = agent.accessAvaliableCellPheromones(availableMovements);


        ArrayList<Vector> equivalentMoves = agent.determineEquivalentMinMoves(pheromoneValues, availableMovements);

        //Agent moves one cell from original position, therefore 3 x 0-values remain
        int expectedEquivalentMoves = 3;
        assertEquals(equivalentMoves.size(), expectedEquivalentMoves);
    }

    @Test
    void testAvailablePheromoneValues()
    {
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        Vector nextPosition = new Vector(15, 12);
        agent.updateLocation(nextPosition);

        agent.updateView(graphicsEngine.compute(map, agent));

        Ray[] cardinalRays = agent.detectCardinalRays();
        ArrayList<Vector> availableMovements = agent.determineAvailableMovements(cardinalRays);

        ArrayList<Double> pheromoneValues = agent.accessAvaliableCellPheromones(availableMovements);
        int indexOfLastValue = pheromoneValues.size() - 1;
        assertEquals(pheromoneValues.get(indexOfLastValue), agent.releaseMaxPheromone());
    }

    @Test
    void testAvailablePheromonesDefaultValues()
    {
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        agent.updateView(graphicsEngine.compute(map, agent));

        Ray[] cardinalRays = agent.detectCardinalRays();
        ArrayList<Vector> availableMovements = agent.determineAvailableMovements(cardinalRays);

        ArrayList<Double> pheromoneValues = agent.accessAvaliableCellPheromones(availableMovements);
        for(Double pheromoneValue : pheromoneValues)
        {
            assertEquals(pheromoneValue, 0);
        }
    }

    @Test
    void testAvaliableMovementsWithObstacle()
    {
        position = new Vector(20, 19);

        AcoAgent agent = new AcoAgent(position, viewDirection, radius);
        agent.updateView(graphicsEngine.compute(map, agent));

        Ray[] cardinalRays = agent.detectCardinalRays();
        ArrayList<Vector> availableMovements = agent.determineAvailableMovements(cardinalRays);

        //One dimension (North of agent) is blocked by a wall
        assertEquals(availableMovements.size(), 3);
    }

    @Test
    void testAvailableMovements()
    {
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        agent.updateView(graphicsEngine.compute(map, agent));
        Ray[] cardinalRays = agent.detectCardinalRays();

        ArrayList<Vector> availableMovements = agent.determineAvailableMovements(cardinalRays);

        //Within the map there are 4 available movements. Vector position link already tested
        assertEquals(availableMovements.size(), cardinalRays.length);
    }

    @Test
    void testAngleToMovementLink()
    {
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        Vector link_0 = agent.angleToGridMovementLink(0);
        Vector link_180 = agent.angleToGridMovementLink(180);

        assertEquals(link_0, new Vector(0, 1));
        assertEquals(link_180, new Vector(0, -1));
    }

    @Test
    void testCardinalPointDetection()
    {
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        double targetAngle =63;
        agent.updateView(graphicsEngine.compute(map, agent));

        Ray cardinalRay = agent.detectCardinalPoint(targetAngle);
        assertTrue(agent.approximateAngleRange(cardinalRay.angle(), targetAngle));
    }

    @Test
    void testCardinalPointNonDetection()
    {
        AcoAgent agent = new AcoAgent(position, viewDirection, radius);

        double targetAngle =-163;
        agent.updateView(graphicsEngine.compute(map, agent));

        Ray cardinalRay = agent.detectCardinalPoint(targetAngle);
        assertNull(cardinalRay);
    }

    @Test
    void test4CardinalAngles()
    {
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
