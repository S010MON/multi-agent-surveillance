package testing;

import app.controller.GameEngine;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.map.Move;
import app.model.agents.Agent;
import app.model.furniture.FurnitureType;
import app.view.simulation.Renderer;
import app.view.simulation.TeleportCoordinator;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TeleportCoordinatorTest {

    /** Create a GameEngine */
    Map createMapFile()
    {
        Settings s = new Settings();
        s.setGameMode(1);
        s.setHeight(200);
        s.setWidth(200);
        s.setNoOfGuards(1);
        s.setNoOfIntruders(0);
        s.setWalkSpeedGuard(10);
        s.setWalkSpeedIntruder(10);
        s.setSprintSpeedGuard(15);
        s.setSprintSpeedGuard(15);
        s.setTimeStep(1);
        s.setScaling(1);
        s.addFurniture(new Rectangle2D(50, 50, 10, 10), FurnitureType.INTRUDER_SPAWN);
        s.addFurniture(new Rectangle2D(60, 50, 10, 10), FurnitureType.GUARD_SPAWN);
        s.addTeleport(new Rectangle2D(10, 10, 10, 10), new Vector(50, 50), 0);

        return new Map(s);
    }

    @Test void TestIfTeleported()
    {
        Map map = createMapFile();
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(10, 15));
        a.updateDirection(new Vector(0, -1));
        Move move = TeleportCoordinator.teleportIfLegal(a, a.getPosition(), new Vector(20, 15), map);
        assertNotEquals(null, move);
    }

    @Test void TestTeleportPosChange()
    {
        Map map = createMapFile();
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(10, 15));
        a.updateDirection(new Vector(0, -1));
        Move move = TeleportCoordinator.teleportIfLegal(a, a.getPosition(), new Vector(20, 15), map);
        assertNotEquals(null, move);
    }

}
