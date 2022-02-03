package jgfx.javagradlefx;

import app.controller.Ray;
import app.controller.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RayTest
{
    @Test void createRay()
    {
        Vector start = new Vector();
        Vector end = new Vector(10,10);
        Ray r = new Ray(start, end);
        assertEquals(0, r.getU().getX());
        assertEquals(0, r.getU().getY());
        assertEquals(10, r.getV().getX());
        assertEquals(10, r.getV().getY());
    }
}
