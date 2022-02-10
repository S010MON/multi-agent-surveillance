package jgfx.javagradlefx;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.RayTracing;
import app.controller.linAlg.Vector;
import app.model.agents.WallFollowAgent;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureFactory;
import app.model.furniture.FurnitureType;
import app.model.map.Map;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WallFollowAgentTest
{
    GraphicsEngine graphicsEngine = new RayTracing();

    //Agent setup
    Vector agentsPosition = new Vector(100, 100);
    Vector agentsDirection = new Vector(0,-1);
    WallFollowAgent agent = new WallFollowAgent(agentsPosition, agentsDirection, 1);

    @Test
    void testNoWallDetectedInFront()
    {
        //Map
        ArrayList<Furniture> walls = new ArrayList<>();
        Map map = new Map(agent, walls);

        //Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));

        //Detect obstacle
        agent.setMoveLength(300);
        boolean noWallDetected = agent.noWallDetected(agent.getDirection().getAngle());

        assertTrue(noWallDetected);
    }

    @Test
    void testNoWallDetectedOnLeft()
    {
        //Map
        ArrayList<Furniture> walls = new ArrayList<>();
        Map map = new Map(agent, walls);

        //Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));

        //Detect obstacle
        agent.setMoveLength(300);
        boolean noWallDetected = agent.noWallDetected(agent.getAngleOfLeftRay());

        assertTrue(noWallDetected);
    }

    @Test
    void testWallDetectedInFront()
    {
        //Map
        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(150, 150, 100, 2);
        ArrayList<Furniture> walls = new ArrayList<>(Arrays.asList(FurnitureFactory.make(obstacleType, obstacle)));
        Map map = new Map(agent, walls);

        //Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));

        //Detect obstacle
        agent.setMoveLength(300);
        boolean noWallDetected = agent.noWallDetected(agent.getDirection().getAngle());

        assertFalse(noWallDetected);
    }

    @Test
    void testWallDetectedOnLeft()
    {
        //Map
        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(50, 50, 2, 100);
        ArrayList<Furniture> walls = new ArrayList<>(Arrays.asList(FurnitureFactory.make(obstacleType, obstacle)));
        Map map = new Map(agent, walls);

        //Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));

        //Detect obstacle
        agent.setMoveLength(300);
        boolean noWallDetected = agent.noWallDetected(agent.getAngleOfLeftRay());

        assertFalse(noWallDetected);
    }

    @Test
    void testGoForwardAfterTurningLeftNoWall()
    {
        // ALGORITHM CASE 1
        agent.setDEBUG(true);
        agent.setLastTurn(WallFollowAgent.TurnType.LEFT);
        agent.setMovedForwardLast(false);

        // Map
        ArrayList<Furniture> walls = new ArrayList<>();
        Map map = new Map(agent, walls);
        Vector initialPos = agentsPosition;

        // Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));

        // Move
        agent.setMoveLength(100);
        agent.move();
        Vector expectedPos = new Vector(initialPos.getX(),initialPos.getY()+100);

        // Check position change, last turn type and if we moved forward
        assertTrue(agent.getPosition().equals(expectedPos));
        assertEquals(WallFollowAgent.TurnType.NO_TURN, agent.getLastTurn());
        assertTrue(agent.isMovedForwardLast());
    }

    @Test
    void testTurnLeftAfterLastTurnNotLeft() {
        // ALGORITHM CASE 2
        agent.setDEBUG(true);
    }

    @Test
    void testTurnLeftWallInFront() {
        // ALGORITHM CASE 2
        agent.setDEBUG(true);
    }

    @Test
    void testGoForwardLastTurnNotLeftAndWallOnLeft() {
        // ALGORITHM CASE 3
        agent.setDEBUG(true);
    }

    @Test
    void testTurnRightWallInFrontAndOnLeft() {
        // ALGORITHM CASE 4
        agent.setDEBUG(true);
    }
}

