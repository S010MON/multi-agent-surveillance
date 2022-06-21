package testing;

import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundRay;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import app.model.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class IntersectionTest
{

    @Test void findIntersectionTest_withIntersect()
    {
        Vector p1 = new Vector();
        Vector p2 = new Vector(3, 3);
        Vector p3 = new Vector(0, 3);
        Vector p4 = new Vector(3, 0);

        assertTrue(Intersection.hasIntersection(p1, p2, p3, p4));
    }

    @Test void findIntersectionTest_noIntersect()
    {
        Vector p1 = new Vector();
        Vector p2 = new Vector(1, 0);
        Vector p3 = new Vector(0, 1);
        Vector p4 = new Vector(1, 1);

        assertFalse(Intersection.hasIntersection(p1, p2, p3, p4));
    }

    @Test void findIntersectionTest_boundaryCheck_inOneLine()
    {
        Vector p1 = new Vector();
        Vector p2 = new Vector(0, 1);
        Vector p3 = new Vector(0, 2);
        Vector p4 = new Vector(0, 3);

        assertFalse(Intersection.hasIntersection(p1, p2, p3, p4));
    }

    @Test void findIntersectionTest_boundaryCheck_linearlyIntersect()
    {
        Vector p1 = new Vector();
        Vector p2 = new Vector(0, 1);
        Vector p3 = new Vector(1, 0);
        Vector p4 = new Vector(6, 0);

        assertFalse(Intersection.hasIntersection(p1, p2, p3, p4));
    }

    @Test void findIntersectionTest_boundaryCheck_onePointOnLine()
    {
        Vector p1 = new Vector();
        Vector p2 = new Vector(0, 10);
        Vector p3 = new Vector(0, 4);
        Vector p4 = new Vector(3, 4);

        assertTrue(Intersection.hasIntersection(p1, p2, p3, p4));
    }

    @Test void testLimitedIntersectionTrue()
    {
        Vector u = new Vector(0,0);
        Vector v = new Vector(0, 10);
        Vector a_pos = new Vector(0, 5);

        Agent agent = new AgentImp(a_pos, new Vector(), 1, Type.GUARD);
        SoundRay soundRay = new SoundRay(u, v);

        assertTrue(Intersection.hasLimitedIntersection(soundRay, agent.getPosition(), agent.getRadius()));
        Vector intersection = Intersection.findIntersection(soundRay, agent.getPosition(), agent.getRadius());

        assertEquals(0, intersection.getX(), 0.1);
        assertEquals(4, intersection.getY(), 0.1);
    }

    @Test void testLimitedIntersectionFalse()
    {
        Vector u = new Vector(0,0);
        Vector v = new Vector(0, 10);
        Vector a_pos = new Vector(0, 15);

        Agent agent = new AgentImp(a_pos, new Vector(), 1, Type.GUARD);
        SoundRay soundRay = new SoundRay(u, v);

        assertFalse(Intersection.hasLimitedIntersection(soundRay, agent.getPosition(), agent.getRadius()));
    }
}
