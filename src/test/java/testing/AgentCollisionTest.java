package testing;

import app.controller.linAlg.Vector;
import app.controller.linAlg.agentCollision;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AgentCollisionTest
{


    @Test
    void intersectStartPoint()
    {
        assertTrue(agentCollision.hasCollision(new Vector(0,0),new Vector(20,20),5,new Vector(-2,-2),0.5));
    }


    @Test
    void intersectRec()
    {
        assertTrue(agentCollision.hasCollision(new Vector(0,0),new Vector(10,10),1,new Vector(3,3),0.5));
    }


    @Test
    void noIntersect()
    {
        assertFalse(agentCollision.hasCollision(new Vector(0, 0), new Vector(2, 2), 1, new Vector(5, 5), 0.5));
    }


    @Test
    void testAngle(){
        Vector a = new Vector(0,0);
        Vector b = new Vector(4,4);

        assertEquals(45,agentCollision.findAngle(a,b));
    }
}
