package testing;

import app.controller.linAlg.AgentCollision;
import app.controller.linAlg.Vector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AgentCollisionTest
{


    @Test
    void intersectOnePoint()
    {
        assertTrue(AgentCollision.hasCollision(new Vector(0,0),new Vector(0,0),5,new Vector(10,0),5));
    }

    @Test
    void intersectStatic()
    {
        assertFalse(AgentCollision.hasCollision(new Vector(0,0),new Vector(0,0),5,new Vector(10.1,0),5));
    }

    @Test
    void intersectStartPoint()
    {
        assertTrue(AgentCollision.hasCollision(new Vector(0,0),new Vector(20,20),5,new Vector(-2,-2),0.5));
    }


    @Test
    void intersectRec()
    {
        assertTrue(AgentCollision.hasCollision(new Vector(0,0),new Vector(10,10),1,new Vector(3,3),0.5));
    }


    @Test
    void noIntersect()
    {
        assertFalse(AgentCollision.hasCollision(new Vector(0, 0), new Vector(2, 2), 1, new Vector(5, 5), 0.5));
    }


    @Test
    void testAngle(){
        Vector a = new Vector(0,0);
        Vector b = new Vector(4,4);

        assertEquals(45,AgentCollision.findAngle(a,b));
    }
}
