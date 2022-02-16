package jgfx.javagradlefx;

import app.controller.FileParser;
import app.controller.Settings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileParserTest
{
    @Test void testCreationOfSettings()
    {
        Settings s = FileParser.readGameFile("src/test/java/jgfx/javagradlefx/mytest.txt");
        double scale = s.getScaling();
        double error = 0.00001;
        // Gamemode
        assertEquals(44, s.getGamemode());
        // Height
        assertEquals((int)(95*scale), s.getHeight(), error);
        // Width
        assertEquals((int)(144*scale), s.getWidth(), error);
        // Number of guards
        assertEquals(7, s.getNoOfGuards());
        // Number of intruders
        assertEquals(4, s.getNoOfIntruders());
        // Walk speed guard
        assertEquals(13.0*scale, s.getWalkSpeedGuard(), error);
        // Sprint speed guard
        assertEquals(21.0*scale, s.getSprintSpeedGuard(), error);
        // Walk speed intruder
        assertEquals(13.0*scale, s.getWalkSpeedIntruder(), error);
        // Sprint speed intruder
        assertEquals(21.0*scale, s.getSprintSpeedIntruder(), error);
        // Time step
        assertEquals(0.5, s.getTimeStep());
        // Scaling
        assertEquals(0.2, s.getScaling());
        // Wall
        assertEquals(50.0*scale, s.getWalls().get(0).getMinX(), error);
        assertEquals(0.0*scale, s.getWalls().get(0).getMinY(), error);
        assertEquals(51.0*scale, s.getWalls().get(0).getMaxX(), error);
        assertEquals(20.0*scale, s.getWalls().get(0).getMaxY(), error);
        // Shade
        assertEquals(10.0*scale, s.getShade().get(0).getMinX(), error);
        assertEquals(20.0*scale, s.getShade().get(0).getMinY(), error);
        assertEquals(20.0*scale, s.getShade().get(0).getMaxX(), error);
        assertEquals(40.0*scale, s.getShade().get(0).getMaxY(), error);
        // Tower
        assertEquals(0.0*scale, s.getTowers().get(0).getMinX(), error);
        assertEquals(23.0*scale, s.getTowers().get(0).getMinY(), error);
        assertEquals(50.0*scale, s.getTowers().get(0).getMaxX(), error);
        assertEquals(63.0*scale, s.getTowers().get(0).getMaxY(), error);
        // Portal
        assertEquals(20.0*scale, s.getPortals().get(0).getMinX(), error);
        assertEquals(70.0*scale, s.getPortals().get(0).getMinY(), error);
        assertEquals(25.0*scale, s.getPortals().get(0).getMaxX(), error);
        assertEquals(75.0*scale, s.getPortals().get(0).getMaxY(), error);
        // Teleport to point
        assertEquals(90.0*scale, s.getTeleportTo().get(0).getX(), error);
        assertEquals(50.0*scale, s.getTeleportTo().get(0).getY(), error);
        // Texture
        assertEquals(10.0*scale, s.getTextures().get(0).getMinX(), error);
        assertEquals(20.0*scale, s.getTextures().get(0).getMinY(), error);
        assertEquals(20.0*scale, s.getTextures().get(0).getMaxX(), error);
        assertEquals(40.0*scale, s.getTextures().get(0).getMaxY(), error);
        // Texture type
        assertEquals(0, s.getTextureType().get(0), error);
        // Target area
        assertEquals(20.0*scale, s.getTargetArea().getMinX(), error);
        assertEquals(40.0*scale, s.getTargetArea().getMinY(), error);
        assertEquals(25.0*scale, s.getTargetArea().getMaxX(), error);
        assertEquals(45.0*scale, s.getTargetArea().getMaxY(), error);
        // Spawn area intruders
        assertEquals(2.0*scale, s.getSpawnAreaIntruders().getMinX(), error);
        assertEquals(2.0*scale, s.getSpawnAreaIntruders().getMinY(), error);
        assertEquals(20.0*scale, s.getSpawnAreaIntruders().getMaxX(), error);
        assertEquals(10.0*scale, s.getSpawnAreaIntruders().getMaxY(), error);
        // Spawn area guards
        assertEquals(2.0*scale, s.getSpawnAreaGuards().getMinX(), error);
        assertEquals(2.0*scale, s.getSpawnAreaGuards().getMinY(), error);
        assertEquals(20.0*scale, s.getSpawnAreaGuards().getMaxX(), error);
        assertEquals(10.0*scale, s.getSpawnAreaGuards().getMaxY(), error);
    }
}
