package jgfx.javagradlefx;

import app.controller.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test void scaleVectorPos()
    {
        Vector v = new Vector(10,10);
        Vector t = v.scale(10);
        assertEquals(100d, t.getX());
        assertEquals(100d, t.getY());
    }

    @Test void scaleVectorNeg()
    {
        Vector v = new Vector(10,10);
        Vector t = v.scale(-1);
        assertEquals(-10d, t.getX());
        assertEquals(-10d, t.getY());
    }

    @Test void testDotPos()
    {
        Vector v = new Vector(10,10);
        Vector u = new Vector(10,10);
        double dotProduct = u.dot(v);
        assertEquals(200, dotProduct);
    }

    @Test void testDotNeg()
    {
        Vector v = new Vector(10,10);
        Vector u = new Vector(-12,-7);
        double dotProduct = u.dot(v);
        assertEquals(-190, dotProduct);
    }

    @Test void testCrossPos()
    {
        Vector v = new Vector(40,0);
        Vector u = new Vector(0,40);
        double crossProduct = u.cross(v);
        assertEquals(1600, crossProduct);
    }

    @Test void testCrossNeg()
    {
        Vector v = new Vector(10,10);
        Vector u = new Vector(-12,-7);
        double crossProduct = u.cross(v);
        assertEquals(50, crossProduct);
    }

    @Test void rotate360()
    {
        Vector u = new Vector(0,10);
        Vector v = u.rotate(360);
        assertEquals(v.getX(), u.getX(), 0.001);
        assertEquals(v.getY(), u.getY(), 0.001);
    }

    @Test void rotate180()
    {
        Vector u = new Vector(0,10);
        Vector v = u.rotate(180);
        assertEquals(v.getX(), 0, 0.001);
        assertEquals(v.getY(), -10, 0.001);
    }

    @Test void rotate90()
    {
        Vector u = new Vector(0,10);
        Vector v = u.rotate(90);
        assertEquals(v.getX(), -10, 0.001);
        assertEquals(v.getY(), 0, 0.001);
    }

}
