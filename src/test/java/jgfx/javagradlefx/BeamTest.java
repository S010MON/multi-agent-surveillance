package jgfx.javagradlefx;

import app.controller.Beam;
import app.controller.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeamTest
{
    @Test void createBeam()
    {
        Vector start = new Vector();
        Vector end = new Vector(10,10);
        Beam r = new Beam(start, end);
        assertEquals(0, r.getU().getX());
        assertEquals(0, r.getU().getY());
        assertEquals(10, r.getV().getX());
        assertEquals(10, r.getV().getY());
    }

    @Test void rotate360()
    {
        Vector u = new Vector(0,0);
        Vector v = new Vector(0,10);
        Beam r = new Beam(u, v);
        Beam s = r.rotate(360);
        assertEquals(0, s.getU().getX(), 0.001);
        assertEquals(0, s.getU().getY(), 0.001);
        assertEquals(0, s.getV().getX(), 0.001);
        assertEquals(10, s.getV().getY(), 0.001);
    }

    @Test void rotate180()
    {
        Vector u = new Vector(0,0);
        Vector v = new Vector(0,10);
        Beam r = new Beam(u, v);
        Beam s = r.rotate(180);
        assertEquals(0, s.getU().getX(), 0.001);
        assertEquals(0, s.getU().getY(), 0.001);
        assertEquals(0, s.getV().getX(), 0.001);
        assertEquals(-10, s.getV().getY(), 0.001);
    }

    @Test void rotate90()
    {
        Vector u = new Vector(0,0);
        Vector v = new Vector(0,10);
        Beam r = new Beam(u, v);
        Beam s = r.rotate(90);
        assertEquals(0, s.getU().getX(), 0.001);
        assertEquals(0, s.getU().getY(), 0.001);
        assertEquals(-10, s.getV().getX(), 0.001);
        assertEquals(0, s.getV().getY(), 0.001);
    }

    @Test void rotate360Offset()
    {
        Vector u = new Vector(10,10);
        Vector v = new Vector(10,20);
        Beam r = new Beam(u, v);
        Beam s = r.rotate(360);
        assertEquals(10, s.getU().getX(), 0.001);
        assertEquals(10, s.getU().getY(), 0.001);
        assertEquals(10, s.getV().getX(), 0.001);
        assertEquals(20, s.getV().getY(), 0.001);
    }

    @Test void rotate180Offset()
    {
        Vector u = new Vector(10,10);
        Vector v = new Vector(10,20);
        Beam r = new Beam(u, v);
        Beam s = r.rotate(180);
        assertEquals(10, s.getU().getX(), 0.001);
        assertEquals(10, s.getU().getY(), 0.001);
        assertEquals(10, s.getV().getX(), 0.001);
        assertEquals(0, s.getV().getY(), 0.001);
    }

    @Test void rotate90Offset()
    {
        Vector u = new Vector(10,10);
        Vector v = new Vector(10,20);
        Beam r = new Beam(u, v);
        Beam s = r.rotate(90);
        assertEquals(10, s.getU().getX(), 0.001);
        assertEquals(10, s.getU().getY(), 0.001);
        assertEquals(0, s.getV().getX(), 0.001);
        assertEquals(10, s.getV().getY(), 0.001);
    }

    @Test void rotateInSteps()
    {
        Vector u = new Vector(10,10);
        Vector v = new Vector(10,20);
        Beam s = new Beam(u, v);
        for(int i = 0; i < 9; i++)
        {
            s = s.rotate(10);
        }
        assertEquals(10, s.getU().getX(), 0.001);
        assertEquals(10, s.getU().getY(), 0.001);
        assertEquals(0, s.getV().getX(), 0.001);
        assertEquals(10, s.getV().getY(), 0.001);
    }
}
