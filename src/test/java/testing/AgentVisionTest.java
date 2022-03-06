package testing;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.agents.AgentImp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AgentVisionTest
{
    @Test void noHit()
    {
        Vector pos = new Vector(0,0);
        Vector dir = new Vector(0,1);
        double radius = 10;
        Agent agent = new AgentImp(pos, dir, radius);

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
        Agent agent = new AgentImp(pos, dir, radius);

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
        Agent agent = new AgentImp(pos, dir, radius);

        Vector a = new Vector(0, 11);
        Vector b = new Vector(0, 12);
        Ray ray = new Ray(a, b);

        assertTrue(agent.isHit(ray));
    }
}
