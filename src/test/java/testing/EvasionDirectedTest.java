package testing;

import app.controller.GameEngine;
import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.Move;
import app.model.agents.Agent;
import app.model.agents.Universe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EvasionDirectedTest
{
    private Settings settings;
    private GraphicsEngine graphicsEngine = new GraphicsEngine(91);
    private Map map;
    private GameEngine gm;

    public void independentTestSetup()
    {
        settings = FileManager.loadSettings("src/main/resources/evasion_test_map");
        map = new Map(settings);
        map.setHumanActive(false);
        Universe.clearUniverse();
        gm = new GameEngine(map);
        map.getAgents().get(1).setDirection(new Vector(-1, 0));
        gm.tick();
    }

    @Test
    public void testDirectedMove()
    {
        independentTestSetup();
        Agent evasion = map.getAgents().get(1);
        Move directedMove = evasion.move();
        assertTrue(directedMove.getDeltaPos().getX() > 15);
    }
}
