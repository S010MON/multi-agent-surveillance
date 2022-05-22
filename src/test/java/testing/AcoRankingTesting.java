package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.Move;
import app.model.Type;
import app.model.agents.ACO.AcoRanking;
import app.model.agents.Universe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcoRankingTesting
{
    private Vector position;
    private Vector direction = new Vector(1, 0);
    private int radius = 10;
    private double viewingDistance = 25;
    private int moveDistance = 20;
    private GraphicsEngine graphicsEngine = new GraphicsEngine(91);
    private Settings settings;
    private Map map;
    private double stochasticHeuristic = 0;

    public void independentTestSetup()
    {
        settings = FileManager.loadSettings("src/main/resources/AcoTestMap");
        map = new Map(settings);
        map.setHumanActive(false);
        Universe.clearUniverse();
    }

    @Test
    public void testRankingSystem()
    {
        position = new Vector(10, 10);
        AcoRanking agent = new AcoRanking(position, direction, radius, Type.GUARD);
        agent.setStochasticHeuristic(stochasticHeuristic);
        independentTestSetup();

        Move longestMove = singleMoveCycle(agent);

        assertEquals(longestMove.getDeltaPos(), new Vector(moveDistance, 0));
    }

    @Test
    public void testRankingClearance()
    {
        position = new Vector(10, 10);
        AcoRanking agent = new AcoRanking(position, direction, radius, Type.GUARD);
        agent.setStochasticHeuristic(stochasticHeuristic);
        independentTestSetup();

        Vector nextLocation = position.add(new Vector(moveDistance, 0));
        agent.updateLocation(nextLocation);
        assertTrue(agent.getMoveRanking().isEmpty());
    }

    @Test
    public void testNextBestMoveAfterFail()
    {
        position = new Vector(20, 20);
        AcoRanking agent = new AcoRanking(position, direction, radius, Type.GUARD);
        agent.setStochasticHeuristic(stochasticHeuristic);
        independentTestSetup();

        Move move = singleMoveCycle(agent);
        assertEquals(move.getDeltaPos(), new Vector(0, moveDistance));

        agent.setMoveFailed(true);

        //Next ranked move
        agent.updateView(graphicsEngine.compute(map, agent));
        Move nextMove = agent.move();
        assertEquals(nextMove.getDeltaPos(), new Vector(moveDistance, 0));
    }

    public Move singleMoveCycle(AcoRanking agent)
    {
        //Detect pheromones
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        //Determine possible moves (#1)
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        //Determine possible moves (#2)
        agent.updateView(graphicsEngine.compute(map, agent));
        agent.move();

        //Select longest move according to ranking
        agent.updateView(graphicsEngine.compute(map, agent));
        return agent.move();
    }
}


