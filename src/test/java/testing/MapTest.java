package testing;

import app.controller.linAlg.Vector;
import app.controller.settings.SettingsGenerator;
import app.model.Map;
import app.model.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
}
