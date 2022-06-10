package testing;

import app.controller.io.FileManager;
import app.controller.linAlg.Vector;
import app.controller.settings.SettingsGenerator;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.Type;
import app.model.agents.MemoryGraph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MapTest
{
    @Test void TestGetFurnitureTypeInside()
    {
        Map map = new Map(SettingsGenerator.mapTest());
        Type exp = Type.WALL;
        Type act = map.objectAt(new Vector(70, 70));
        assertEquals(exp, act);
    }


    @Test void TestGetFurnitureTypeOutside()
    {
        Map map = new Map(SettingsGenerator.mapTest());
        Type act = map.objectAt(new Vector(25, 25));
        assertNull(act);
    }


    @Test void TestGetAgentTypeInside()
    {
        Map map = new Map(SettingsGenerator.mapTest());
        map.setHumanActive(true);
        Type exp = Type.INTRUDER;
        Type act = map.objectAt(new Vector(100, 100));
        assertEquals(exp, act);
    }

    @Test void TestGetAgentTypeBoundary()
    {
        Map map = new Map(SettingsGenerator.mapTest());
        map.setHumanActive(true);
        Type exp = Type.INTRUDER;
        Type act = map.objectAt(new Vector(110, 100));
        assertEquals(exp, act);
    }


    @Test void TestGetAgentTypeOutside()
    {
        Map map = new Map(SettingsGenerator.mapTest());
        Type act = map.objectAt(new Vector(10, 10));
        assertNull(act);
    }

    @Test void TestPerfectMapGraph()
    {
        String mapPath = "src/main/resources/";
        Settings settings = FileManager.loadSettings(mapPath + "perfection");

        Map map = new Map(settings);
        MemoryGraph graph = map.createFullGraph();
        
        boolean isObstacle_1 = graph.getVertexAt(new Vector(107, 89)).getObstacle();
        boolean isObstacle_2 = graph.getVertexAt(new Vector(map.getWidth() / 2, map.getHeight() / 2)).getObstacle();
        
        assertFalse(isObstacle_1);
        assertTrue(isObstacle_2);
    }
}
