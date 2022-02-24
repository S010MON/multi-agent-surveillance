package testing;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.boundary.Boundary;
import app.model.boundary.InvisibleBoundary;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RayTracingTest
{
    @Test void hitTest_center()
    {
        Vector a = new Vector(10,20);
        Vector b = new Vector(20,20);
        Boundary bdy = new InvisibleBoundary(a,b);

        Vector u = new Vector(15,0);
        Vector v = new Vector(0,1);
        Ray ray = new Ray(u, v);

        assertTrue(bdy.isHit(ray));

        Vector exp = new Vector(15,20);
        Vector act = bdy.intersection(ray);
        assertEquals(exp.getX(), act.getX(), 0.001);
        assertEquals(exp.getY(), act.getY(), 0.001);
    }

    @Test void hitTest_endpoint()
    {
        Vector a = new Vector(10,20);
        Vector b = new Vector(20,20);
        Boundary bdy = new InvisibleBoundary(a,b);

        Vector u = new Vector(20,19);
        Vector v = new Vector(0,100);
        Ray ray = new Ray(u, v);

        assertTrue(bdy.isHit(ray));

        Vector exp = new Vector(20,20);
        Vector act = bdy.intersection(ray);
        assertEquals(exp.getX(), act.getX(), 0.3);
        assertEquals(exp.getY(), act.getY(), 0.3);
    }
}
