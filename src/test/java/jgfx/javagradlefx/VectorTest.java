package jgfx.javagradlefx;

import app.controller.linAlg.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        assertEquals(v.getX(), 10, 0.001);
        assertEquals(v.getY(), 0, 0.001);
    }

    @Test void getAngle0_PosPos()
    {
        Vector v = new Vector(0, 4);
        assertEquals(0, v.getAngle());
    }

    @Test void getAngle90_PosPos()
    {
        Vector v = new Vector(4, 0);
        assertEquals(90, v.getAngle());
    }

    @Test void getAngle45_PosPos()
    {
        Vector v = new Vector(4, 4);
        assertEquals(45, v.getAngle());
    }

    @Test void getAngle45_PosNeg()
    {
        Vector v = new Vector(4, -4);
        assertEquals(135, v.getAngle());
    }

    @Test void getAngle45_NegNeg()
    {
        Vector v = new Vector(-4, -4);
        assertEquals(225, v.getAngle());
    }

    @Test void getAngle45_NegPos()
    {
        Vector v = new Vector(-4, 4);
        assertEquals(315, v.getAngle());
    }

    @Test void rotateThrough360in30Degrees()
    {
        Vector v = new Vector(0,10);
        for(int i = 0; i < 360; i+=30)
        {
            double angle = (double) i;
            double act = v.rotate(angle).getAngle();
            assertEquals(angle, act, 0.001);
        }
    }

    @Test void testLength()
    {
        Vector u = new Vector(1, -2);
        Vector v = new Vector(0,0);
        Vector w = new Vector(3,0);
        Vector z = new Vector(2, -6.5);
        assertEquals(2.236, u.length(), 0.01);
        assertEquals(0, v.length(), 0.01);
        assertEquals(3, w.length(), 0.01);
        assertEquals(6.801, z.length(), 0.01);
    }

    @Test void normaliseTest()
    {
        Vector u = new Vector(437, 24);
        Vector normU = u.normalise();
        Vector v = new Vector(-256, 152);
        Vector normV = v.normalise();

        assertEquals(1, normU.length(), 0.01);
        assertEquals(1, normV.length(), 0.01);

        //check that same still direction
        assertEquals(u.getX()/u.getY(), normU.getX()/normU.getY(), 0.01);
        assertEquals(v.getX()/v.getY(), normV.getX()/normV.getY(), 0.01);
    }
    @Test void vectorEquals()
    {
        Vector u = new Vector(345, 200);
        Vector v = new Vector(345, 200);

        assertTrue(u.equals(v));
    }

    @Test void vectorNotEquals()
    {
        Vector u = new Vector(345, 201);
        Vector v = new Vector(345, 200);

        assertFalse(u.equals(v));
    }

    @Test void testHashCodeDeterministic()
    {
        Vector u = new Vector(345, 201);
        Integer u_hashCode = u.vectorHashCode();

        Vector u_copy = new Vector(345, 201);
        Integer uCopy_hashCode = u.vectorHashCode();

        assertEquals(u_hashCode, uCopy_hashCode);
    }
}
