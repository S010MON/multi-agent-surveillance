package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.agents.ACO.AcoAgentLimitedVision;
import app.model.agents.ACO.AcoGrid;
import app.model.agents.Team;
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
        AcoAgentLimitedVision.initializeWorld(world);
        AcoAgentLimitedVision.clearAcoCounts();
    }

    @Test
    void testMemoryCycle()
    {
        AcoGrid world = new AcoGrid(settings.getWidth(), settings.getHeight(), 20);
        AcoAgentLimitedVision.initializeWorld(world);
        AcoAgentLimitedVision.clearAcoCounts();

        viewDirection = new Vector(-1, 0);
        position = new Vector(72.4758040349858, 367.0214134418279);
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius, Team.GUARD);

        getAgentStuckAtWindow(agent);

        agent.setMoveFailed(true);
        agent.updateLocation(new Vector(51.4758040349858, 367.0214134418279));
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        agent.setMoveFailed(false);
        assertEquals(agent.getPheromoneDirections().size(), 3);

        firstMoveCycleAfterStuck(agent);

        secondMoveCycleAfterStuck(agent);

        thirdMoveCycleAfterStuck(agent);

        fourthMoveCycleUsingSTM(agent);
    }

    public void firstMoveCycleAfterStuck(AcoAgentLimitedVision agent)
    {
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.setMoveFailed(true);
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        agent.updateLocation(new Vector(51.4758040349858, 367.0214134418279));
        agent.setMoveFailed(false);

        assertEquals(agent.getPheromoneDirections().size(), 2);
    }

    public void secondMoveCycleAfterStuck(AcoAgentLimitedVision agent)
    {
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.setMoveFailed(true);
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        agent.updateLocation(new Vector(51.4758040349858, 367.0214134418279));
        agent.setMoveFailed(false);
        assertEquals(agent.getPheromoneDirections().size(), 1);
    }

    //Point at which is relies on short term memory only
    public void thirdMoveCycleAfterStuck(AcoAgentLimitedVision agent)
    {
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.setMoveFailed(true);
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        agent.setMoveFailed(false);
        assertEquals(agent.getPheromoneDirections().size(), 4);
        agent.updateLocation(new Vector(51.4758040349858, 367.0214134418279));
    }

    public void fourthMoveCycleUsingSTM(AcoAgentLimitedVision agent)
    {
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.setMoveFailed(true);
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        agent.updateLocation(new Vector(51.4758040349858, 367.0214134418279));
        assertEquals(agent.getSizeOfDirectionsToVisiblyExplore(), 1);
        //Used directly to make the move
        assertEquals(agent.getShortTermMemorySize(), 1);
        assertEquals(agent.getPheromoneDirections().size(), 3);
        assertEquals(agent.getPossibleMovements().size(), 0);
        agent.setMoveFailed(false);
    }

    public void getAgentStuckAtWindow(AcoAgentLimitedVision agent)
    {
        agent.updateLocation(new Vector(51.4758040349858, 387.0214134418279));
        agent.updateLocation(new Vector(51.4758040349858, 347.0214134418279));
        agent.updateLocation(new Vector(51.4758040349858, 367.0214134418279));

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
    }

    @Test
    void testStuckAtWindowsUsingMemory()
    {
        AcoGrid world = new AcoGrid(settings.getWidth(), settings.getHeight(), 20);
        AcoAgentLimitedVision.initializeWorld(world);
        AcoAgentLimitedVision.clearAcoCounts();

        viewDirection = new Vector(-1, 0);
        position = new Vector(72.4758040349858, 367.0214134418279);
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius, Team.GUARD);

        getAgentStuckAtWindow(agent);

        agent.setMoveFailed(true);
        agent.move();
        agent.updateLocation(new Vector(51.4758040349858, 367.0214134418279));
        agent.setMoveFailed(false);

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        assertEquals(agent.getSizeOfDirectionsToVisiblyExplore(), 1);
    }

    @Test
    void testExplorationToMovementFacingWallEdgeCase()
    {
        position = new Vector(677, 100);
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius, Team.GUARD);

        agent.updateLocation(new Vector(678, 101));
        agent.updateLocation(new Vector(678, 99));
        agent.updateLocation(new Vector(678, 100));

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        assertEquals(agent.getPheromoneDirections().size(), 3);

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        ArrayList<Vector> possibleMovements = agent.getPossibleMovements();
        assertEquals(possibleMovements.size(), 2);
    }

    @Test
    void testBasicMovement()
    {
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius, Team.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));

        //Case 1: Smelling pheromones
        agent.move();
        assertEquals(agent.getPreviousMove().getEndDir(), position);
        assertEquals(agent.getPreviousMove().getDeltaPos(), new Vector());
        assertEquals(agent.getDirection(), new Vector(-1, 0));

        //Case 2: Exploring directions
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        assertEquals(agent.getDirection(), new Vector(0, -1));
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        // Case 3: Agent makes move based on information extracted
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
        assertEquals(agent.getPreviousMove().getDeltaPos(), new Vector(0, -1));
    }

    @Test
    void testMovementAgainstWall()
    {
        position = new Vector(678, 100);
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius, Team.GUARD);

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();
    }

    @Test
    void testDirectionsToVisiblyExplore()
    {
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius, Team.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));

        ArrayList<Vector> pheromoneDirections = agent.getPheromoneDirections();
        ArrayList<Double> pheromones = agent.accessAvaliableCellPheromones(pheromoneDirections);

        assertEquals(pheromones.size(), pheromoneDirections.size());
        assertEquals(0.0, pheromones.get(0));
    }

    @Test
    void testExplorationToViableMovementFreeSpace()
    {
        viewDirection = new Vector(-1, 0);
        position = new Vector(678, 100);
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius, Team.GUARD);

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.pheromonesDetection();

        agent.updateView(graphicsEngine.compute(map, agent));
        agent.explorationToViableMovement();

        ArrayList<Vector> possibleMovements = agent.getPossibleMovements();
        assertEquals(possibleMovements.size(), 1);
    }

    @Test
    void testDirectionsToVisiblyExploreAgainstWall()
    {
        position = new Vector(0, 0);
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius, Team.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));

        ArrayList<Vector> pheromoneDirections = agent.getPheromoneDirections();
        ArrayList<Double> pheromones = agent.accessAvaliableCellPheromones(pheromoneDirections);
        agent.directionsToVisiblyExplore(pheromones);

        assertEquals(agent.getSizeOfDirectionsToVisiblyExplore(), 2);

        //No gird access off of map boundaries (Maintains direction order)
        assertEquals(pheromones.get(0), 0.0);
        assertEquals(pheromones.get(1), 0.0);
        assertNull(pheromones.get(2));
        assertNull(pheromones.get(3));
    }

    @Test
    void testPheromoneSenseDirections()
    {
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius, Team.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));

        ArrayList<Vector> directions = agent.getPheromoneDirections();
        assertEquals(directions.size(), agent.getCardinalAngles().length);
    }

    @Test
    void movePossible()
    {
        position = new Vector(678, 100);
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius, Team.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));
        double angle = viewDirection.getAngle();

        Ray ray = agent.detectCardinalPoint(angle);
        boolean movePossible =  agent.movePossible(ray);

        assertFalse(movePossible);
    }

    @Test
    void testCardinalPointDetection()
    {
        AcoAgentLimitedVision agent = new AcoAgentLimitedVision(position, viewDirection, radius, Team.GUARD);
        agent.updateView(graphicsEngine.compute(map, agent));
        double angle = viewDirection.getAngle();

        Ray ray = agent.detectCardinalPoint(angle);

        assertNotEquals(ray.getU().getX(), ray.getV().getX());
        assertEquals(ray.getU().getY(), ray.getV().getY());
    }
}
