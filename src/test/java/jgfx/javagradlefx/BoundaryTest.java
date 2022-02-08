package jgfx.javagradlefx;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.furniture.Placeable;
import app.model.furniture.Boundary;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoundaryTest
{
    @Test void testWallHit()
    {
        Vector a = new Vector(10,10);
        Vector b = new Vector(30,10);
        Placeable wall = new Boundary(a, b);

        Vector origin = new Vector(20, 0);
        Vector direction = new Vector(20, 1);
        Ray ray = new Ray(origin, direction);

        assertTrue(wall.isHit(ray));
    }

    @Test void testWallMiss()
    {
        Vector a = new Vector(10,10);
        Vector b = new Vector(30,10);
        Placeable wall = new Boundary(a, b);

        Vector origin = new Vector(20, 20);
        Vector direction = new Vector(20, 21);
        Ray ray = new Ray(origin, direction);

        assertFalse(wall.isHit(ray));
    }

    @Test void testIntersectionStraight()
    {
        Vector a = new Vector(10,10);
        Vector b = new Vector(30,10);
        Placeable wall = new Boundary(a, b);

        Vector origin = new Vector(20, 0);
        Vector direction = new Vector(20, 1);
        Ray ray = new Ray(origin, direction);

        Vector exp = new Vector(20, 10);
        Vector act = wall.intersection(ray);
        assertEquals(exp, act);
    }

    @Test void testIntersectionAngle()
    {
        Vector a = new Vector(10,10);
        Vector b = new Vector(30,10);
        Placeable wall = new Boundary(a, b);

        Vector origin = new Vector(0, 0);
        Vector direction = new Vector(10, 5);
        Ray ray = new Ray(origin, direction);

        Vector exp = new Vector(20, 10);
        Vector act = wall.intersection(ray);
        assertEquals(exp, act);
    }
}
