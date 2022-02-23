package testing;

import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileParserTest
{
    @Test void testCreationOfSettings()
    {
        Settings s = FileManager.loadSettings("src/test/resources/mytest.txt");

        // Gamemode
        assertEquals(44, s.getGameMode());
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
        // Target area
        assertEquals(new Rectangle2D(20, 40, 5, 5), s.getFurniture().get(0).getRect());
        assertEquals("targetArea", s.getFurniture().get(0).getType().label);
        // Intruder spawn
        assertEquals(new Rectangle2D(2, 2, 18, 8), s.getFurniture().get(1).getRect());
        assertEquals("spawnAreaIntruders", s.getFurniture().get(1).getType().label);
        // Guard spawn
        assertEquals(new Rectangle2D(2, 2, 18, 8), s.getFurniture().get(2).getRect());
        assertEquals("spawnAreaGuards", s.getFurniture().get(2).getType().label);
        // Wall
        assertEquals(new Rectangle2D(0, 79, 120, 1), s.getFurniture().get(5).getRect());
        assertEquals("wall", s.getFurniture().get(5).getType().label);
        // Tower
        assertEquals(new Rectangle2D(12, 40, 8, 4), s.getFurniture().get(9).getRect());
        assertEquals("tower", s.getFurniture().get(9).getType().label);
        // Teleport
        assertEquals(new Rectangle2D(20, 70, 5, 5), s.getFurniture().get(10).getRect());
        assertEquals(new Vector(90, 50), s.getFurniture().get(10).getTeleportTo());
        assertEquals(0.0, s.getFurniture().get(10).getTeleportRotation(), 0.001);
        assertEquals("teleport", s.getFurniture().get(10).getType().label);
        // Shade
        assertEquals(new Rectangle2D(10, 20, 10, 20), s.getFurniture().get(11).getRect());
        assertEquals("shaded", s.getFurniture().get(11).getType().label);
        // Texture
        assertEquals(new Rectangle2D(10, 20, 10, 20), s.getFurniture().get(12).getRect());
        assertEquals(0, s.getFurniture().get(12).getTextureType());
        assertEquals(0, s.getFurniture().get(12).getTextureOrientation());
        assertEquals("texture", s.getFurniture().get(12).getType().label);
        // Glass
        assertEquals(new Rectangle2D(0, 0, 20, 20), s.getFurniture().get(13).getRect());
        assertEquals("glass", s.getFurniture().get(13).getType().label);
    }
}
