package jgfx.javagradlefx;

import app.controller.FileParser;
import app.controller.Settings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileParserTest
{
    @Test void testCreationOfSettings()
    {
        Settings s = FileParser.readGameFile("src\\test\\java\\jgfx\\javagradlefx\\mytest.txt");

        // Gamemode
        assertEquals(44, s.getGamemode());
        // Height
        assertEquals(95, s.getHeight());
        // Width
        assertEquals(144, s.getWidth());
        // Number of guards
        assertEquals(7, s.getNoOfGuards());
        // Number of intruders
        assertEquals(4, s.getNoOfIntruders());
        // Walk speed guard
        assertEquals(13.0, s.getWalkSpeedGuard());
        // Sprint speed guard
        assertEquals(21.0, s.getSprintSpeedGuard());
        // Walk speed intruder
        assertEquals(13.0, s.getWalkSpeedIntruder());
        // Sprint speed intruder
        assertEquals(21.0, s.getSprintSpeedIntruder());
        // Time step
        assertEquals(0.5, s.getTimeStep());
        // Scaling
        assertEquals(0.2, s.getScaling());
        // Wall
        assertEquals(50.0, s.getWalls().get(0).getMinX());
        assertEquals(0.0, s.getWalls().get(0).getMinY());
        assertEquals(51.0, s.getWalls().get(0).getMaxX());
        assertEquals(20.0, s.getWalls().get(0).getMaxY());
        // Shade
        assertEquals(10.0, s.getShade().get(0).getMinX());
        assertEquals(20.0, s.getShade().get(0).getMinY());
        assertEquals(20.0, s.getShade().get(0).getMaxX());
        assertEquals(40.0, s.getShade().get(0).getMaxY());
        // Tower
        assertEquals(0.0, s.getTowers().get(0).getMinX());
        assertEquals(23.0, s.getTowers().get(0).getMinY());
        assertEquals(50.0, s.getTowers().get(0).getMaxX());
        assertEquals(63.0, s.getTowers().get(0).getMaxY());
        // Portal
        assertEquals(20.0, s.getPortals().get(0).getMinX());
        assertEquals(70.0, s.getPortals().get(0).getMinY());
        assertEquals(25.0, s.getPortals().get(0).getMaxX());
        assertEquals(75.0, s.getPortals().get(0).getMaxY());
        // Teleport to point
        assertEquals(90.0, s.getTeleportTo().get(0).getX());
        assertEquals(50.0, s.getTeleportTo().get(0).getY());
        // Texture
        assertEquals(10.0, s.getTextures().get(0).getMinX());
        assertEquals(20.0, s.getTextures().get(0).getMinY());
        assertEquals(20.0, s.getTextures().get(0).getMaxX());
        assertEquals(40.0, s.getTextures().get(0).getMaxY());
        // Texture type
        assertEquals(0, s.getTextureType().get(0));
        // Target area
        assertEquals(20.0, s.getTargetArea().getMinX());
        assertEquals(40.0, s.getTargetArea().getMinY());
        assertEquals(25.0, s.getTargetArea().getMaxX());
        assertEquals(45.0, s.getTargetArea().getMaxY());
        // Spawn area intruders
        assertEquals(2.0, s.getSpawnAreaIntruders().getMinX());
        assertEquals(2.0, s.getSpawnAreaIntruders().getMinY());
        assertEquals(20.0, s.getSpawnAreaIntruders().getMaxX());
        assertEquals(10.0, s.getSpawnAreaIntruders().getMaxY());
        // Spawn area guards
        assertEquals(2.0, s.getSpawnAreaGuards().getMinX());
        assertEquals(2.0, s.getSpawnAreaGuards().getMinY());
        assertEquals(20.0, s.getSpawnAreaGuards().getMaxX());
        assertEquals(10.0, s.getSpawnAreaGuards().getMaxY());
    }
}
