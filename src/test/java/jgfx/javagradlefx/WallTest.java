package jgfx.javagradlefx;

import app.controller.Vector;
import app.model.Placeable;
import app.model.Wall;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class WallTest
{
    @Test void testWallHit()
    {
        Placeable wall = new Wall(10, 10, 30,30);
        Vector shot = new Vector(15, 15);
        assertTrue(wall.isHit(shot));
    }

    @Test void testWallMissWest()
    {
        Placeable wall = new Wall(10, 10, 30,30);
        Vector shot = new Vector(50, 15);
        assertFalse(wall.isHit(shot));
    }

    @Test void testWallMissEast()
    {
        Placeable wall = new Wall(10, 10, 30,30);
        Vector shot = new Vector(-5, 15);
        assertFalse(wall.isHit(shot));
    }

    @Test void testWallMissSouth()
    {
        Placeable wall = new Wall(10, 10, 30,30);
        Vector shot = new Vector(15, 50);
        assertFalse(wall.isHit(shot));
    }

    @Test void testWallMissNorth()
    {
        Placeable wall = new Wall(10, 10, 30,30);
        Vector shot = new Vector(15, -5);
        assertFalse(wall.isHit(shot));
    }
}
