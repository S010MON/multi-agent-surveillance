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

    @Test void testGetIntersectionPointVertical()
    {
        Map map = new Map(createSettingsFile());
        Portal portal = (Portal) map.getPortals().get(0);
        Vector a = new Vector(15, 5);
        Vector b = new Vector(15, 25);

        Vector intersection1 = TeleportCoordinator.getIntersectionPoint(portal, a, b);
        Vector intersection2 = TeleportCoordinator.getIntersectionPoint(portal, b, a);

        assertEquals(15, intersection1.getX());
        assertEquals(10, intersection1.getY());
        assertEquals(15, intersection2.getX());
        assertEquals(20, intersection2.getY());
    }

    @Test void testGetIntersectionPointHorizontal()
    {
        Map map = new Map(createSettingsFile());
        Portal portal = (Portal) map.getPortals().get(0);
        Vector c = new Vector(5, 15);
        Vector d = new Vector(25, 15);

        Vector intersection3 = TeleportCoordinator.getIntersectionPoint(portal, c, d);
        Vector intersection4 = TeleportCoordinator.getIntersectionPoint(portal, d, c);

        assertEquals(10, intersection3.getX());
        assertEquals(15, intersection3.getY());
        assertEquals(20, intersection4.getX());
        assertEquals(15, intersection4.getY());
    }

    @Test void testGetIntersectionPointDiagonal()
    {
        Map map = new Map(createSettingsFile());
        Portal portal = (Portal) map.getPortals().get(0);
        Vector e = new Vector(9, 16);
        Vector f = new Vector(16, 9);

        Vector intersection5 = TeleportCoordinator.getIntersectionPoint(portal, e, f);
        Vector intersection6 = TeleportCoordinator.getIntersectionPoint(portal, f, e);

        assertEquals(10, intersection5.getX());
        assertEquals(15, intersection5.getY());
        assertEquals(15, intersection6.getX());
        assertEquals(10, intersection6.getY());
    }

    @Test void testGetTeleportMoveVertical()
    {
        Map map = new Map(createSettingsFile());
        Portal portal = (Portal) map.getPortals().get(0);
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(15, 5));
        a.updateDirection(new Vector(0, 1));

        Vector start = a.getPosition();
        Vector end = new Vector(15, 20);
        Vector intersection = TeleportCoordinator.getIntersectionPoint(portal, start, end);
        Move move = TeleportCoordinator.getTeleportMove(map, a, portal, intersection, start, end);


        Vector expectedEndPosition = new Vector(60, 50);
        Vector expectedDeltaPos = expectedEndPosition.sub(start);

        double error = 0.0000001;
        assertEquals(1, move.getEndDir().getX(), error);
        assertEquals(0, move.getEndDir().getY(), error);
        assertEquals(expectedDeltaPos.getX(), move.getDeltaPos().getX(), error);
        assertEquals(expectedDeltaPos.getY(), move.getDeltaPos().getY(), error);
    }

    @Test void testGetTeleportMoveHorizontal()
    {
        Map map = new Map(createSettingsFile());
        Portal portal = (Portal) map.getPortals().get(0);
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(5, 15));
        a.updateDirection(new Vector(1, 0));

        Vector start = a.getPosition();
        Vector end = new Vector(25, 15);
        Vector intersection = TeleportCoordinator.getIntersectionPoint(portal, start, end);
        Move move = TeleportCoordinator.getTeleportMove(map, a, portal, intersection, start, end);


        Vector expectedEndPosition = new Vector(50, 35);
        Vector expectedDeltaPos = expectedEndPosition.sub(start);

        double error = 0.0000001;
        assertEquals(0, move.getEndDir().getX(), error);
        assertEquals(-1, move.getEndDir().getY(), error);
        assertEquals(expectedDeltaPos.getX(), move.getDeltaPos().getX(), error);
        assertEquals(expectedDeltaPos.getY(), move.getDeltaPos().getY(), error);
    }

    @Test void testGetTeleportMoveDiagonal()
    {
        Map map = new Map(createSettingsFile());
        Portal portal = (Portal) map.getPortals().get(0);
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(9, 16));
        a.updateDirection(new Vector(1, -1));

        Vector start = a.getPosition();
        Vector end = new Vector(16, 9);
        Vector intersection = TeleportCoordinator.getIntersectionPoint(portal, start, end);
        Move move = TeleportCoordinator.getTeleportMove(map, a, portal, intersection, start, end);


        Vector expectedEndPosition = new Vector(44, 44);
        Vector expectedDeltaPos = expectedEndPosition.sub(start);

        double error = 0.0000001;
        assertEquals(-1, move.getEndDir().getX(), error);
        assertEquals(-1, move.getEndDir().getY(), error);
        assertEquals(expectedDeltaPos.getX(), move.getDeltaPos().getX(), error);
        assertEquals(expectedDeltaPos.getY(), move.getDeltaPos().getY(), error);
    }


    @Test void TestIfTeleportedBasic()
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

    @Test void testTeleportIfLegalEdgeCase()
    {
        Settings s = createSettingsFile();
        Map map = new Map(s);
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(10, 5));
        a.updateDirection(new Vector(0, -1));
        Move move = TeleportCoordinator.teleportIfLegal(a, a.getPosition(), new Vector(20, 15), map);
        assertNotNull(move);
    }

    @Test void testTeleportIfLegalPosAndDirChange()
    {
        Map map = new Map(createSettingsFile());
        Portal portal = (Portal) map.getPortals().get(0);
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(9, 16));
        a.updateDirection(new Vector(1, -1));

        Vector start = a.getPosition();
        Vector end = new Vector(16, 9);
        Vector intersection = TeleportCoordinator.getIntersectionPoint(portal, start, end);
        Move move = TeleportCoordinator.teleportIfLegal(a, start, end, map);


        Vector expectedEndPosition = new Vector(44, 44);
        Vector expectedDeltaPos = expectedEndPosition.sub(start);

        double error = 0.0000001;
        assertEquals(-1, move.getEndDir().getX(), error);
        assertEquals(-1, move.getEndDir().getY(), error);
        assertEquals(expectedDeltaPos.getX(), move.getDeltaPos().getX(), error);
        assertEquals(expectedDeltaPos.getY(), move.getDeltaPos().getY(), error);
    }

    @Test void testTeleportIfLegalIllegalCase()
    {
        Map map = new Map(createSettingsFile());
        Portal portal = (Portal) map.getPortals().get(0);
        Agent a = map.getAgents().get(0);

        a.updateLocation(new Vector(9, 16));
        a.updateDirection(new Vector(1, -1));

        Vector start = a.getPosition();
        Vector end = new Vector(0, 16);
        Vector intersection = TeleportCoordinator.getIntersectionPoint(portal, start, end);
        Move move = TeleportCoordinator.teleportIfLegal(a, start, end, map);


        Vector expectedEndPosition = new Vector(44, 44);
        Vector expectedDeltaPos = expectedEndPosition.sub(start);

        assertNull(move);
    }

    @Test void testTeleportIfLegalTwoPortals()
    {
        Settings s = createSettingsFile();
        s.addTeleport(new Rectangle2D(30, 10, 10, 10), new Vector(60, 60), 90);
        Map map = new Map(s);
        Agent a = map.getAgents().get(0);
        Portal portal = (Portal) map.getPortals().get(0);

        a.updateLocation(new Vector(5, 15));
        a.updateDirection(new Vector(1, 0));

        Vector start = a.getPosition();
        Vector end = new Vector(35, 15);
        Vector intersection = TeleportCoordinator.getIntersectionPoint(portal, start, end);
        Move move = TeleportCoordinator.getTeleportMove(map, a, portal, intersection, start, end);


        Vector expectedEndPosition = new Vector(50, 25);
        Vector expectedDeltaPos = expectedEndPosition.sub(start);

        double error = 0.0000001;
        assertEquals(0, move.getEndDir().getX(), error);
        assertEquals(-1, move.getEndDir().getY(), error);
        assertEquals(expectedDeltaPos.getX(), move.getDeltaPos().getX(), error);
        assertEquals(expectedDeltaPos.getY(), move.getDeltaPos().getY(), error);
    }


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
