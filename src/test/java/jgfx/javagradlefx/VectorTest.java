package jgfx.javagradlefx;

import app.controller.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VectorTest
{
    @Test void createDefaultVector()
    {
        Vector v = new Vector();
        assertEquals(0d, v.getX());
        assertEquals(0d, v.getY());
    }

    @Test void createSpecifiedVector()
    {
        Vector v = new Vector(10,-10);
        assertEquals(10d, v.getX());
        assertEquals(-10d, v.getY());
    }

    @Test void addVectorPos()
    {
        Vector v = new Vector(10,10);
        Vector u = new Vector(0.5,0.5);
        Vector t = v.add(u);
        assertEquals(10.5d, t.getX());
        assertEquals(10.5d, t.getY());
    }

    @Test void addVectorNeg()
    {
        Vector v = new Vector(10,10);
        Vector u = new Vector(-0.5,-0.5);
        Vector t = v.add(u);
        assertEquals(9.5d, t.getX());
        assertEquals(9.5d, t.getY());
    }

    @Test void subVectorPos()
    {
        Vector v = new Vector(10,10);
        Vector u = new Vector(0.5,0.5);
        Vector t = v.sub(u);
        assertEquals(9.5d, t.getX());
        assertEquals(9.5d, t.getY());
    }

    @Test void subVectorNeg()
    {
        Vector v = new Vector(10,10);
        Vector u = new Vector(-0.5,-0.5);
        Vector t = v.sub(u);
        assertEquals(10.5d, t.getX());
        assertEquals(10.5d, t.getY());
    }
}
