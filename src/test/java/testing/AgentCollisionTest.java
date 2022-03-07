package testing;

import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AgentCollisionTest
{


    @Test
    void intersectOnePoint()
    {
        assertTrue(Intersection.hasDirectionIntersect(new Vector(0,0),new Vector(0,5),1,new Vector(0,0),0.5));
    }

    @Test
    void intersectStatic()
    {
        assertFalse(Intersection.hasDirectionIntersect(new Vector(0,0),new Vector(0,0),5,new Vector(10.1,0),5));
    }


    @Test
    void intersectRec()
    {
        assertTrue(Intersection.hasDirectionIntersect(new Vector(0,0),new Vector(10,10),1,new Vector(3,3),0.5));
    }


    @Test
    void noIntersect()
    {
        assertFalse(Intersection.hasDirectionIntersect(new Vector(0, 0), new Vector(2, 2), 1, new Vector(5, 5), 0.5));
    }
}