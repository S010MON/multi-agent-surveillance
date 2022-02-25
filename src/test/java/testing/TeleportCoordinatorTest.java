package testing;

import app.controller.GameEngine;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.furniture.Portal;
import app.model.map.Move;
import app.model.agents.Agent;
import app.model.furniture.FurnitureType;
import app.view.simulation.Renderer;
import app.view.simulation.TeleportCoordinator;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeleportCoordinatorTest {

    @Test void testGetPortalsOnPathOne()
    {
        Map map = new Map(createSettingsFile());
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(15, 5));
        a.updateDirection(new Vector(0, 1));
        List<Portal> portalsOnPath = TeleportCoordinator.getPortalsOnPath(map, a.getPosition(), new Vector(15, 20));
        assertEquals(1, portalsOnPath.size());
    }

    @Test void testGetPortalsOnPathTwo()
    {
        Settings s = createSettingsFile();
        s.addTeleport(new Rectangle2D(10, 20, 10, 10), new Vector(60, 60), 90);
        Map map = new Map(s);
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(15, 5));
        a.updateDirection(new Vector(0, 1));
        List<Portal> portalsOnPath = TeleportCoordinator.getPortalsOnPath(map, a.getPosition(), new Vector(15, 25));
        assertEquals(2, portalsOnPath.size());
    }

    @Test void testGetPortalsOnPathZero()
    {
        Map map = new Map(createSettingsFile());
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(15, 5));
        a.updateDirection(new Vector(0, 1));
        List<Portal> portalsOnPath = TeleportCoordinator.getPortalsOnPath(map, a.getPosition(), new Vector(15, -5));
        assertEquals(0, portalsOnPath.size());
    }

    /*
    @Test void TestIfTeleported()
    {
        Settings s = createSettingsFile();
        Map map = new Map(s);
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(15, 5));
        a.updateDirection(new Vector(0, 1));
        Move move = TeleportCoordinator.teleportIfLegal(a, a.getPosition(), new Vector(15, 20), map);
        System.out.println(move);
        assertNotNull(move);
    }

    @Test void TestIfTeleportedEdgeCase()
    {
        Settings s = createSettingsFile();
        Map map = new Map(s);
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(10, 5));
        a.updateDirection(new Vector(0, -1));
        Move move = TeleportCoordinator.teleportIfLegal(a, a.getPosition(), new Vector(20, 15), map);
        assertNotEquals(null, move);
    }

    @Test void TestTeleportPosChange()
    {
        Settings s = createSettingsFile();
        Map map = new Map(s);
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(10, 15));
        a.updateDirection(new Vector(0, -1));
        Move move = TeleportCoordinator.teleportIfLegal(a, a.getPosition(), new Vector(20, 15), map);
        assertNotEquals(null, move);
        //assertEquals();
    }
    */


    /** Create a GameEngine */
    Settings createSettingsFile()
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
        s.addTeleport(new Rectangle2D(10, 10, 10, 10), new Vector(50, 50), 90);

        return s;
    }
}
