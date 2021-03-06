package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.controller.settings.SettingsObject;
import app.model.Map;
import app.model.agents.Agent;
import app.model.agents.Human;
import app.model.Type;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureFactory;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RayFeedbackTest
{
    GraphicsEngine ge = new GraphicsEngine(180);
    Vector agent1Pos = new Vector(10, 10);
    Vector agent1Dir = new Vector(0, 1);
    Vector agent2Pos = new Vector(10, 20);
    Vector agent2Dir = new Vector(0, -1);

    Human agent1 = new Human(agent1Pos, agent1Dir, 1, Type.GUARD);
    Human agent2 = new Human(agent2Pos, agent2Dir, 1, Type.INTRUDER);

    private void reset()
    {
        agent1 = new Human(agent1Pos, agent1Dir, 1, Type.GUARD);
        agent2 = new Human(agent2Pos, agent2Dir, 1, Type.INTRUDER);
        ge = new GraphicsEngine(180);
    }

    @Test void rayWithGuard()
    {
        Vector start = new Vector();
        Vector end = new Vector(10,10);
        Ray r = new Ray(start, end, Type.GUARD);
        assertEquals(Type.GUARD, r.getType());
    }

    @Test void rayWithIntruder()
    {
        Vector start = new Vector();
        Vector end = new Vector(10,10);
        Ray r = new Ray(start, end, Type.INTRUDER);
        assertEquals(Type.INTRUDER, r.getType());
    }

    @Test void rayWithNull()
    {
        Vector start = new Vector();
        Vector end = new Vector(10,10);
        Ray r = new Ray(start, end);
        assertNull(r.getType());
    }

    @Test void seeIntruder()
    {
        ArrayList<Agent> agents = new ArrayList<>();
        agents.add(agent1);
        agents.add(agent2);

        agent1.setDirection(new Vector(0, -1));

        Map map = new Map(agents, new ArrayList<>());

        // Compute rays
        agent1.updateView(ge.compute(map, agent1));

        boolean seeAgent = false;
        for(Ray r : agent1.getView())
        {
            if(r.getType() == Type.INTRUDER)
            {
                seeAgent = true;
            }
        }
        System.out.println(agent1.getView().size());
        assertEquals(true, seeAgent);
    }

    @Test void seeIntruderOneRay()
    {
        ArrayList<Agent> agents = new ArrayList<>();
        agents.add(agent1);
        agents.add(agent2);

        Ray ray = new Ray(agent1Pos, new Vector(10, 30));

        assertNotNull(ge.getIntersection(ray, agents, agent1));
    }

    @Test void seeIntruderTwoRays()
    {
        ArrayList<Agent> agents = new ArrayList<>();
        agents.add(agent1);
        agents.add(agent2);

        Ray ray = new Ray(agent1Pos, new Vector(10, 30));

        assertNotNull(ge.getIntersection(ray, agents, agent1));
    }

    @Test void seeGuard()
    {
        ArrayList<Agent> agents = new ArrayList<>();
        agents.add(agent1);
        agents.add(agent2);

        agent2.setDirection(new Vector(0, 1));

        Map map = new Map(agents, new ArrayList<>());

        // Compute rays
        agent2.updateView(ge.compute(map, agent2));

        boolean seeAgent = false;
        for(Ray r : agent2.getView())
        {
            if(r.getType() == Type.GUARD)
            {
                seeAgent = true;
            }
        }
        assertEquals(true, seeAgent);
    }

    @Test void seeNoAgents()
    {
        Map map = new Map(agent1, new ArrayList<>());

        // Compute rays
        agent1.updateView(ge.compute(map, agent1));
        for(Ray r : agent1.getView())
        {
            assertNull(r.getType());
        }
    }


    @Test void agentBehindWall()
    {
        SettingsObject wallObject = new SettingsObject(new Rectangle2D(0, 20, 20, 5), FurnitureType.WALL);
        Furniture wall = FurnitureFactory.make(wallObject);
        ArrayList furniture = new ArrayList<>();
        furniture.add(wall);

        ArrayList<Agent> agents = new ArrayList<>();
        agents.add(agent1);
        agents.add(agent2);

        agent2.updateLocation(new Vector(10, 50));
        agent2.setDirection(new Vector(0,1));

        Map map = new Map(agents, furniture);

        agent1.updateView(ge.compute(map, agent1));

        for(Ray r : agent1.getView())
        {
            assertNotEquals(Type.INTRUDER, r.getType());
            assertNotEquals(Type.GUARD, r.getType());
        }

        agent2.updateView(ge.compute(map, agent1));

        for(Ray r : agent2.getView())
        {
            assertNotEquals(Type.INTRUDER, r.getType());
            assertNotEquals(Type.GUARD, r.getType());
        }

        reset();
    }

    @Test void bothSeeNoAgents()
    {
        ge = new GraphicsEngine();
        ArrayList<Agent> agents = new ArrayList<>();
        agents.add(agent1);
        agents.add(agent2);

        agent1.setDirection(new Vector(0,-1));
        double angle1 = agent1.getDirection().getAngle();
        agent2.setDirection(new Vector(0,1));

        Map map = new Map(agents, new ArrayList<>());

        agent1.updateView(ge.compute(map, agent1));

        for(Ray r : agent1.getView())
        {
            assertNotEquals(Type.INTRUDER, r.getType());
            assertNotEquals(Type.GUARD, r.getType());
        }

        agent2.updateView(ge.compute(map, agent1));

        for(Ray r : agent2.getView())
        {
            assertNotEquals(Type.INTRUDER, r.getType());
            assertNotEquals(Type.GUARD, r.getType());
        }

        reset();
    }
}
