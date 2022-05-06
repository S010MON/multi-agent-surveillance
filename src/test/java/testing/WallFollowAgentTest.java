package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.controller.settings.SettingsObject;
import app.model.Map;
import app.model.Move;
import app.model.agents.MemoryGraph;
import app.model.agents.Team;
import app.model.agents.WallFollow.WallFollowAgent;
import app.model.agents.WallFollow.WfWorld;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureFactory;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WallFollowAgentTest
{
    GraphicsEngine graphicsEngine = new GraphicsEngine(181);
    Vector initialPosition = new Vector(200, 100);
    Vector initialDirection = new Vector(0,1);
    double moveLen = 75.0;
    WallFollowAgent agent = new WallFollowAgent(initialPosition, initialDirection, 1, Team.INTRUDER, moveLen);
    private app.model.agents.WallFollow.WfWorld tempWfWorld = new WfWorld(new MemoryGraph((int)moveLen));

    @Test
    void testAgentDirectionAngle()
    {
        agent.setDirection(new Vector(0,-1));
        assertEquals(180.0, agent.getDirection().getAngle());
        agent.setDirection(new Vector(1,0));
        assertEquals(90.0, agent.getDirection().getAngle());
        agent.setDirection(new Vector(0,1));
        assertEquals(0.0, agent.getDirection().getAngle());
        agent.setDirection(new Vector(-1,0));
        assertEquals(270.0, agent.getDirection().getAngle());
    }

    @Test
    void testAgentViewRays()
    {
        //Map
        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(150, 150, 100, 2);
        SettingsObject obj = new SettingsObject(obstacle, obstacleType);
        ArrayList<Furniture> walls = new ArrayList<>(List.of(FurnitureFactory.make(obj)));
        Map map = new Map(agent, walls);

        //Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));
        ArrayList<Double> hittingRayAngles = new ArrayList<>();

        for (Ray r : agent.getView())
        {
            if (!agent.noWallDetected(r.angle())) {
                hittingRayAngles.add(r.angle());
            }
        }

        System.out.println("The angles of rays that hit the wall:");
        System.out.println(hittingRayAngles);
        assertTrue(agent.getView().size() > 0);
    }

    @Test
    void testNoWallDetectedInFront()
    {
        //Map
        ArrayList<Furniture> walls = new ArrayList<>();
        Map map = new Map(agent, walls);

        //Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));

        //Detect obstacle
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
        boolean noWallDetected = agent.noWallDetected(agent.getAngleOfLeftRay());

        assertTrue(noWallDetected);
    }

    @Test
    void testWallDetectedInFront()
    {
        //Map
        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(150, 150, 100, 2);
        SettingsObject obj = new SettingsObject(obstacle, obstacleType);
        ArrayList<Furniture> walls = new ArrayList<>(List.of(FurnitureFactory.make(obj)));
        Map map = new Map(agent, walls);

        //Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));

        //Detect obstacle
        boolean noWallDetected = agent.noWallDetected(agent.getDirection().getAngle());

        assertFalse(noWallDetected);
    }

    @Test
    void testWallDetectedOnLeft()
    {
        //Map
        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(250, 50, 2, 100);
        SettingsObject obj = new SettingsObject(obstacle, obstacleType);
        ArrayList<Furniture> walls = new ArrayList<>(List.of(FurnitureFactory.make(obj)));
        Map map = new Map(agent, walls);

        //Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));

        //Detect obstacle
        boolean noWallDetected = agent.noWallDetected(agent.getAngleOfLeftRay());

        assertFalse(noWallDetected);
    }

    @Test
    void testNoWallDetectedInFrontButDetectedOnLeft()
    {
        //Map
        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(250, 50, 2, 100);
        SettingsObject obj = new SettingsObject(obstacle, obstacleType);
        ArrayList<Furniture> walls = new ArrayList<>(List.of(FurnitureFactory.make(obj)));
        Map map = new Map(agent, walls);

        //Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));

        //Detect obstacle
        boolean noWallDetectedFront = agent.noWallDetected(agent.getDirection().getAngle());
        boolean noWallDetectedLeft = agent.noWallDetected(agent.getAngleOfLeftRay());

        assertTrue(noWallDetectedFront);
        assertFalse(noWallDetectedLeft);
    }

    @Test
    void testNoWallDetectedOnLeftButDetectedInFront()
    {
        //Map
        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(150, 150, 100, 2);
        SettingsObject obj = new SettingsObject(obstacle, obstacleType);
        ArrayList<Furniture> walls = new ArrayList<>(List.of(FurnitureFactory.make(obj)));
        Map map = new Map(agent, walls);

        //Detect surroundings
        agent.updateView(graphicsEngine.compute(map, agent));

        //Detect obstacle
        boolean noWallDetectedFront = agent.noWallDetected(agent.getDirection().getAngle());
        boolean noWallDetectedLeft = agent.noWallDetected(agent.getAngleOfLeftRay());

        assertTrue(noWallDetectedLeft);
        assertFalse(noWallDetectedFront);
    }

    @Test
    void testRotateLeft()
    {
        // dir. before rotate = (0,-1)
        agent.setDirection(new Vector(0,-1));
        agent.setDirection(agent.rotateAgentLeft());
        Vector expectedDir = new Vector(-1,0);
        assertEquals(expectedDir, agent.getDirection());
        // dir. before rotate = (1,0)
        agent.setDirection(agent.rotateAgentLeft());
        expectedDir = new Vector(0,1);
        assertEquals(expectedDir, agent.getDirection());
        // dir. before rotate = (0,1)
        agent.setDirection(agent.rotateAgentLeft());
        expectedDir = new Vector(1,0);
        assertEquals(expectedDir, agent.getDirection());
        // dir. before rotate = (1,0)
        agent.setDirection(agent.rotateAgentLeft());
        expectedDir = new Vector(0,-1);
        assertEquals(expectedDir, agent.getDirection());
    }

    @Test
    void testRotateRight()
    {
        // dir. before rotate = (0,-1)
        agent.setDirection(new Vector(0,-1));
        agent.setDirection(agent.rotateAgentRight());
        Vector expectedDir = new Vector(1,0);
        assertEquals(expectedDir, agent.getDirection());
        // dir. before rotate = (-1,0)
        agent.setDirection(agent.rotateAgentRight());
        expectedDir = new Vector(0,1);
        assertEquals(expectedDir, agent.getDirection());
        // dir. before rotate = (0,1)
        agent.setDirection(agent.rotateAgentRight());
        expectedDir = new Vector(-1,0);
        assertEquals(expectedDir, agent.getDirection());
        // dir. before rotate = (1,0)
        agent.setDirection(agent.rotateAgentRight());
        expectedDir = new Vector(0,-1);
        assertEquals(expectedDir, agent.getDirection());
    }

    @Test
    void testAlgorithmCase0()
    {
        /*
        ALGORITHM CASE 0
        if !wallEncountered
            if (no wall forward)
                go forward
            else
                turn 90 deg right
                set wallEcountered = true
         */
        if (!agent.getWorld().getClass().equals(tempWfWorld.getClass()))
        {
            agent.setWorld(new WfWorld(new MemoryGraph((int)moveLen)));
            agent.initializeWorld();
        }
        agent.setDEBUG(true);
        agent.updateLocation(initialPosition);
        agent.setDirection(initialDirection);
        System.out.println("Agent direction: " + agent.getDirection());
        System.out.println("Agent direction angle: " + agent.getDirection().getAngle());

        //Map
        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(150, 275, 100, 2);
        Rectangle2D obstacle2 = new Rectangle2D(10, 10, 2, 700);
        Rectangle2D obstacle3 = new Rectangle2D(700, 10, 2, 700);
        SettingsObject obj = new SettingsObject(obstacle, obstacleType);
        SettingsObject obj2 = new SettingsObject(obstacle2, obstacleType);
        SettingsObject obj3 = new SettingsObject(obstacle3, obstacleType);
        ArrayList<Furniture> walls = new ArrayList<>(List.of(FurnitureFactory.make(obj),FurnitureFactory.make(obj2),
                FurnitureFactory.make(obj3)));
        Map map = new Map(agent, walls);

        // Update surroundings and move 3 times
        // Agent should move forward 2 times and third move should be turning right
        double moveLength = agent.getMoveLength();
        // move 1
        agent.updateView(graphicsEngine.compute(map, agent));
        Move newMove1 = agent.move();
        agent.updateLocation(initialPosition.add(newMove1.getDeltaPos()));
        Vector expectedDeltaPos1 = new Vector(moveLength * initialDirection.getX(), moveLength * initialDirection.getY());
        // move 2
        agent.updateView(graphicsEngine.compute(map, agent));
        Move newMove2 = agent.move();
        agent.updateLocation(agent.getPosition().add(newMove2.getDeltaPos()));
        Vector expectedDeltaPos2 = new Vector(moveLength * agent.getDirection().getX(), moveLength * agent.getDirection().getY());
        // move 3
        agent.updateView(graphicsEngine.compute(map, agent));
        Move newMove3 = agent.move();
        agent.updateLocation(agent.getPosition().add(newMove3.getDeltaPos()));
        Vector expectedDeltaPos3 = new Vector(0,0);
        Vector expectedPos = new Vector(initialPosition.getX(),initialPosition.getY()+2*moveLength);
        Vector expectedDir = new Vector(-1,0);

        // Check moves and final position
        assertEquals(initialDirection,newMove1.getEndDir());
        assertEquals(expectedDeltaPos1,newMove1.getDeltaPos());
        assertEquals(initialDirection,newMove2.getEndDir());
        assertEquals(expectedDeltaPos2,newMove2.getDeltaPos());
        assertEquals(expectedDir,newMove3.getEndDir());
        assertEquals(expectedDeltaPos3,newMove3.getDeltaPos());

        assertEquals(expectedPos,agent.getPosition());
        assertEquals(WallFollowAgent.TurnType.RIGHT, agent.getLastTurn());
        assertEquals(expectedDir,agent.getDirection());
        assertFalse(agent.isMovedForwardLast());
        assertTrue(agent.isWallEncountered());
    }

    @Test
    void testAlgorithmCase1()
    {
        // ALGORITHM CASE 1
        // if (turned left previously and forward no wall)
        //      go forward
        if (!agent.getWorld().getClass().equals(tempWfWorld.getClass()))
        {
            agent.setWorld(new WfWorld(new MemoryGraph((int)moveLen)));
            agent.initializeWorld();
        }
        agent.setWallEncountered(true);
        agent.setLastTurn(WallFollowAgent.TurnType.LEFT);
        agent.setMovedForwardLast(false);
        agent.updateLocation(initialPosition);
        agent.setDirection(initialDirection);

        // Map
        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(150, 275, 100, 2);
        Rectangle2D obstacle2 = new Rectangle2D(10, 10, 2, 700);
        Rectangle2D obstacle3 = new Rectangle2D(700, 10, 2, 700);
        SettingsObject obj = new SettingsObject(obstacle, obstacleType);
        SettingsObject obj2 = new SettingsObject(obstacle2, obstacleType);
        SettingsObject obj3 = new SettingsObject(obstacle3, obstacleType);
        ArrayList<Furniture> walls = new ArrayList<>(List.of(FurnitureFactory.make(obj),FurnitureFactory.make(obj2),
                FurnitureFactory.make(obj3)));
        Map map = new Map(agent, walls);

        // Detect surroundings and move
        agent.updateView(graphicsEngine.compute(map, agent));

        double moveLength = agent.getMoveLength();
        Move newMove = agent.move();
        agent.updateLocation(initialPosition.add(newMove.getDeltaPos()));
        Vector expectedDeltaPos = new Vector(moveLength * initialDirection.getX(), moveLength * initialDirection.getY());

        Vector expectedPos = new Vector(initialPosition.getX(),initialPosition.getY()+moveLength);
        Vector expectedDir = initialDirection;

        // Check position change, last turn type, direction and if moved forward
        assertEquals(expectedDir,newMove.getEndDir());
        assertEquals(expectedDeltaPos,newMove.getDeltaPos());

        assertEquals(expectedPos,agent.getPosition());
        assertEquals(WallFollowAgent.TurnType.NO_TURN, agent.getLastTurn());
        assertEquals(expectedDir,initialDirection);
        assertTrue(agent.isMovedForwardLast());
    }

    @Test
    void testAlgorithmCase2LastTurnNotLeft()
    {
        // ALGORITHM CASE 2.1
        // (case 1 condition violated as last turn was not LEFT (i.e. was NO_TURN or RIGHT)) ->
        // if (no wall at left)
        //    turn 90 deg left

        if (!agent.getWorld().getClass().equals(tempWfWorld.getClass()))
        {
            agent.setWorld(new WfWorld(new MemoryGraph((int)moveLen)));
            agent.initializeWorld();
        }

        agent.setWallEncountered(true);
        agent.setLastTurn(WallFollowAgent.TurnType.NO_TURN);
        agent.setMovedForwardLast(false);

        // Map
        ArrayList<Furniture> walls = new ArrayList<>();
        Map map = new Map(agent, walls);

        // Detect surroundings and move
        agent.updateView(graphicsEngine.compute(map, agent));
        double moveLength = 100;  // move length in reality is way smaller
        agent.setMoveLength(moveLength);
        boolean noLeftWallDetected = agent.noWallDetected(agent.getAngleOfLeftRay());
        Move newMove = agent.move();
        agent.updateLocation(initialPosition.add(newMove.getDeltaPos()));
        Vector expectedDeltaPos = new Vector(0,0);
        Vector expectedPos = initialPosition;
        Vector expectedDir = new Vector(1,0);

        // Check position, last turn type, direction and if moved forward
        assertEquals(expectedDir,newMove.getEndDir());
        assertEquals(expectedDeltaPos,newMove.getDeltaPos());
        assertTrue(noLeftWallDetected);
        assertEquals(expectedPos,agent.getPosition());
        assertEquals(WallFollowAgent.TurnType.LEFT, agent.getLastTurn());
        assertEquals(expectedDir,agent.getDirection());
        assertFalse(agent.isMovedForwardLast());
    }

    @Test
    void testAlgorithmCase2WallInFront()
    {
        // ALGORITHM CASE 2.2
        // (case 1 condition violated as there is a wall in front) ->
        // if (no wall at left)
        //    turn 90 deg left

        if (!agent.getWorld().getClass().equals(tempWfWorld.getClass()))
        {
            agent.setWorld(new WfWorld(new MemoryGraph((int)moveLen)));
            agent.initializeWorld();
        }
        agent.setWallEncountered(true);
        // Map
        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(150, 150, 100, 2);
        SettingsObject obj = new SettingsObject(obstacle, obstacleType);
        ArrayList<Furniture> walls = new ArrayList<>(List.of(FurnitureFactory.make(obj)));
        Map map = new Map(agent, walls);

        // Detect surroundings and turn
        agent.updateView(graphicsEngine.compute(map, agent));
        double moveLength = 100;
        agent.setMoveLength(moveLength);
        boolean noWallDetected = agent.noWallDetected(agent.getDirection().getAngle());
        boolean noLeftWallDetected = agent.noWallDetected(agent.getAngleOfLeftRay());
        Move newMove = agent.move();
        agent.updateLocation(initialPosition.add(newMove.getDeltaPos()));
        Vector expectedDeltaPos = new Vector(0,0);
        Vector expectedPos = initialPosition;
        Vector expectedDir = new Vector(1,0);

        // Check wall in front detected, position, last turn type, direction and if moved forward
        assertEquals(expectedDir,newMove.getEndDir());
        assertEquals(expectedDeltaPos,newMove.getDeltaPos());
        assertFalse(noWallDetected);
        assertTrue(noLeftWallDetected);
        assertEquals(expectedPos,agent.getPosition());
        assertEquals(WallFollowAgent.TurnType.LEFT, agent.getLastTurn());
        assertEquals(expectedDir,agent.getDirection());
        assertFalse(agent.isMovedForwardLast());
    }

    @Test
    void testAlgorithmCase3()
    {
        // ALGORITHM CASE 3
        // (case 1 violated as last turn not left; case 2 violated as there is wall on left) ->
        // if (no wall forward)
        //    go forward

        if (!agent.getWorld().getClass().equals(tempWfWorld.getClass()))
        {
            agent.setWorld(new WfWorld(new MemoryGraph((int)moveLen)));
            agent.initializeWorld();
        }
        agent.setWallEncountered(true);
        agent.setLastTurn(WallFollowAgent.TurnType.NO_TURN);

        //Map
        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(150, 400, 100, 2);
        Rectangle2D obstacle2 = new Rectangle2D(250, 50, 2, 200);
        Rectangle2D obstacle3 = new Rectangle2D(50, 10, 2, 700);
        SettingsObject obj = new SettingsObject(obstacle, obstacleType);
        SettingsObject obj2 = new SettingsObject(obstacle2, obstacleType);
        SettingsObject obj3 = new SettingsObject(obstacle3, obstacleType);
        ArrayList<Furniture> walls = new ArrayList<>(List.of(FurnitureFactory.make(obj),FurnitureFactory.make(obj2),
                FurnitureFactory.make(obj3)));
        Map map = new Map(agent, walls);

        // Detect surroundings and move
        agent.updateView(graphicsEngine.compute(map, agent));
        double moveLength = agent.getMoveLength();
        boolean noFrontWallDetected = agent.noWallDetected(agent.getDirection().getAngle());
        boolean noLeftWallDetected = agent.noWallDetected(agent.getAngleOfLeftRay());
        Move newMove = agent.move();
        agent.updateLocation(initialPosition.add(newMove.getDeltaPos()));
        Vector expectedDeltaPos = new Vector(moveLength * initialDirection.getX(), moveLength * initialDirection.getY());
        Vector expectedPos = new Vector(initialPosition.getX(),initialPosition.getY()+moveLength);
        Vector expectedDir = initialDirection;

        // Check wall on left detected, no wall in front detected, new position, direction, last turn type, if moved forward
        assertEquals(expectedDir,newMove.getEndDir());
        assertEquals(expectedDeltaPos,newMove.getDeltaPos());
        assertFalse(noLeftWallDetected);
        assertTrue(noFrontWallDetected);
        assertEquals(expectedPos, agent.getPosition());
        assertEquals(expectedDir, agent.getDirection());
        assertEquals(WallFollowAgent.TurnType.NO_TURN, agent.getLastTurn());
        assertTrue(agent.isMovedForwardLast());
    }

    @Test
    void testAlgorithmCase4()
    {
        // ALGORITHM CASE 4
        // (case 1, 2 nd 3 violated as walls in front and on left) ->
        //    turn 90 deg right

        agent.setWallEncountered(true);
        //Map
        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacleLeft = new Rectangle2D(250, 50, 2, 100);
        Rectangle2D obstacleFront = new Rectangle2D(150, 150, 100, 2);
        SettingsObject obj1 = new SettingsObject(obstacleLeft, obstacleType);
        SettingsObject obj2 = new SettingsObject(obstacleFront, obstacleType);
        ArrayList<Furniture> walls = new ArrayList<>(Arrays.asList(FurnitureFactory.make(obj1),
                FurnitureFactory.make(obj2)));
        Map map = new Map(agent, walls);

        // Detect surroundings and turn
        agent.updateView(graphicsEngine.compute(map, agent));
        double moveLength = 100;
        agent.setMoveLength(moveLength);
        boolean noFrontWallDetected = agent.noWallDetected(agent.getDirection().getAngle());
        boolean noLeftWallDetected = agent.noWallDetected(agent.getAngleOfLeftRay());
        Vector expectedPos = initialPosition;
        Vector expectedDir = agent.rotateAgentRight();
        Move newMove = agent.move();
        agent.updateLocation(initialPosition.add(newMove.getDeltaPos()));
        Vector expectedDeltaPos = new Vector(0,0);

        // Check front and left wall detected, position, new direction, last turn type, if moved forward
        assertEquals(expectedDir,newMove.getEndDir());
        assertEquals(expectedDeltaPos,newMove.getDeltaPos());
        assertFalse(noFrontWallDetected);
        assertFalse(noLeftWallDetected);
        assertEquals(expectedPos, agent.getPosition());
        assertEquals(expectedDir, agent.getDirection());
        assertEquals(WallFollowAgent.TurnType.RIGHT, agent.getLastTurn());
        assertFalse(agent.isMovedForwardLast());
    }
}

