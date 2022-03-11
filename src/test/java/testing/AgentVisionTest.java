package testing;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AgentVisionTest
{
    @Test void noHit()
    {
        Vector pos = new Vector(0,0);
        Vector dir = new Vector(0,1);
        double radius = 10;
        Agent agent = new AgentImp(pos, dir, radius, null);

        Vector a = new Vector(11, 11);
        Vector b = new Vector(11, 12);
        Ray ray = new Ray(a, b);

        assertFalse(agent.isHit(ray));
    }

    @Test void hitWithinLine()
    {
        Vector pos = new Vector(0,0);
        Vector dir = new Vector(0,1);
        double radius = 10;
        Agent agent = new AgentImp(pos, dir, radius, null);

        Vector a = new Vector(0, -11);
        Vector b = new Vector(0, 11);
        Ray ray = new Ray(a, b);

        assertTrue(agent.isHit(ray));
    }

    @Test void hitBeyondLine()
    {
        Vector pos = new Vector(0,0);
        Vector dir = new Vector(0,1);
        double radius = 10;
        Agent agent = new AgentImp(pos, dir, radius, null);

        Vector a = new Vector(0, 11);
        Vector b = new Vector(0, 12);
        Ray ray = new Ray(a, b);

        assertTrue(agent.isHit(ray));
    }

    @Test void intersectionXAxisTouch()
    {
        Vector pos = new Vector(0,5);
        Vector dir = new Vector(0,1);
        double radius = 5;
        Agent agent = new AgentImp(pos, dir, radius, null);

        Vector a = new Vector(0, -10);
        Vector b = new Vector(0, 0);
        Ray ray = new Ray(a, b);

        assertTrue(agent.isHit(ray));
        Vector exp = new Vector(0,0);
        Vector act = agent.intersection(ray);
        assertEquals(exp, act);
    }

    @Test void intersectionYAxisTouch()
    {
        Vector pos = new Vector(5,0);
        Vector dir = new Vector(0,1);
        double radius = 5;
        Agent agent = new AgentImp(pos, dir, radius, null);

        Vector a = new Vector(-10, 0);
        Vector b = new Vector(0, 0);
        Ray ray = new Ray(a, b);

        assertTrue(agent.isHit(ray));
        Vector exp = new Vector(0,0);
        Vector act = agent.intersection(ray);
        assertEquals(exp, act);
    }

    @Test void intersectionXAxisNoTouch()
    {
        Vector pos = new Vector(0,5);
        Vector dir = new Vector(0,1);
        double radius = 5;
        Agent agent = new AgentImp(pos, dir, radius, null);

        Vector a = new Vector(0, -10);
        Vector b = new Vector(0, -5);
        Ray ray = new Ray(a, b);

        assertTrue(agent.isHit(ray));
        Vector exp = new Vector(0,0);
        Vector act = agent.intersection(ray);
        assertEquals(exp, act);
    }

    @Test void intersectionYAxisNoTouch()
    {
        Vector pos = new Vector(5,0);
        Vector dir = new Vector(0,1);
        double radius = 5;
        Agent agent = new AgentImp(pos, dir, radius, null);

        Vector a = new Vector(-10, 0);
        Vector b = new Vector(-5, 0);
        Ray ray = new Ray(a, b);

        assertTrue(agent.isHit(ray));
        Vector exp = new Vector(0,0);
        Vector act = agent.intersection(ray);
        assertEquals(exp, act);
    }

    @Test void intersectionOffset()
    {
        Vector pos = new Vector(5,0);
        Vector dir = new Vector(0,1);
        double radius = 5;
        Agent agent = new AgentImp(pos, dir, radius, null);

        Vector a = new Vector(5, 15);
        Vector b = new Vector(5, 10);
        Ray ray = new Ray(a, b);

        assertTrue(agent.isHit(ray));
        Vector exp = new Vector(5,5);
        Vector act = agent.intersection(ray);
        assertEquals(exp, act);
    }

    @Test void intersectionTempTest()
    {
        Vector pos = new Vector(0,0);
        double radius = 5;

        Vector a = new Vector(0, 10);
        Vector b = new Vector(0, 0);

        Vector exp = new Vector(0,5);
        Vector act = Intersection.findIntersection(a, b, pos, radius);
        assertEquals(exp, act);
    }

    public static Vector intersection(Vector pointA, Vector pointB, Vector center, double radius)
    {
        double baX = pointB.getX() - pointA.getX();
        double baY = pointB.getY() - pointA.getY();
        double caX = center.getX() - pointA.getX();
        double caY = center.getY() - pointA.getY();

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return null;
        }
        // if disc == 0 ... dealt with later
        double tmpSqrt = Math.sqrt(disc);
        double abScalingFactor1 = -pBy2 + tmpSqrt;
        double abScalingFactor2 = -pBy2 - tmpSqrt;

        Vector p1 = new Vector(pointA.getX() - baX * abScalingFactor1, pointA.getY() - baY * abScalingFactor1);
        if (disc == 0) { // abScalingFactor1 == abScalingFactor2
            return p1;
        }
        Vector p2 = new Vector(pointA.getX() - baX * abScalingFactor2, pointA.getY() - baY * abScalingFactor2);
        return p2;
    }
}
