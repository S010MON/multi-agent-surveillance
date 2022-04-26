package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.controller.settings.SettingsObject;
import app.model.Map;
import app.model.agents.DirectionFollowAgent.DirectionFollowAgent;
import app.model.agents.Team;
import app.model.agents.WallFollow.WallFollowAgent;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureFactory;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DirectionFollowAgentTest
{
    //Agent
    GraphicsEngine graphicsEngine = new GraphicsEngine(181);
    Vector initialPosition = new Vector(200, 100);
    Vector initialDirection = new Vector(0,1);
    Vector targetLocation = new Vector(100, 100);

    DirectionFollowAgent agent = new DirectionFollowAgent(initialPosition, initialDirection, 1, Team.INTRUDER, targetLocation.sub(initialPosition), 75);

    @Test public void noObstacleTest(){
        Vector initialPosition = new Vector(0, 0);
        Vector initialDirection = new Vector(0,1);

        DirectionFollowAgent agent = new DirectionFollowAgent(initialPosition, initialDirection, 1, Team.INTRUDER, new Vector(0,1));

        agent.move();
        assertEquals(DirectionFollowAgent.InternalState.goToTarget, agent.getInternalState());
    }

    @Test public void obstacleHit(){
        GraphicsEngine graphicsEngine = new GraphicsEngine(181);

        Vector initialPosition = new Vector(0, 0);
        Vector initialDirection = new Vector(0,1);

        DirectionFollowAgent agent = new DirectionFollowAgent(initialPosition, initialDirection, 1, Team.INTRUDER, new Vector(0,1));

        FurnitureType obstacleType = FurnitureType.WALL;
        Rectangle2D obstacle = new Rectangle2D(-10, 10, 20, 2);
        SettingsObject obj = new SettingsObject(obstacle, obstacleType);
        ArrayList<Furniture> walls = new ArrayList<>(List.of(FurnitureFactory.make(obj)));
        Map map = new Map(agent, walls);

        agent.updateView(graphicsEngine.compute(map,agent));
        agent.move();
        assertEquals(agent.getInternalState(), DirectionFollowAgent.InternalState.followWall);
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
    void stateFollowRight()
    {

    }

    // TODO: set direction to one of 4 directions when going into wallfollowPart



}
