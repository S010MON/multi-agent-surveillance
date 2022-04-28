package testing;

import app.controller.linAlg.Vector;
import app.controller.settings.SettingsGenerator;
import app.model.Map;
import app.model.furniture.FurnitureType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MapTest
{
    @Test void TestGetFurnitureTypeInside()
    {
        Map map = new Map(SettingsGenerator.mapTest());
        FurnitureType exp = FurnitureType.WALL;
        FurnitureType act = map.furnitureAt(new Vector(70, 70));
        assertEquals(exp, act);
    }


    @Test void TestGetFurnitureTypeOutside()
    {
        Map map = new Map(SettingsGenerator.mapTest());
        FurnitureType act = map.furnitureAt(new Vector(25, 25));
        assertNull(act);
    }


    @Test void TestGetAgentTypeInside()
    {
        Map map = new Map(SettingsGenerator.mapTest());
        FurnitureType exp = FurnitureType.WALL;
        FurnitureType act = map.furnitureAt(new Vector(70, 70));
        assertEquals(exp, act);
    }


    @Test void TestGetAgentTypeOutside()
    {
        Map map = new Map(SettingsGenerator.mapTest());
        FurnitureType act = map.furnitureAt(new Vector(25, 25));
        assertNull(act);
    }
}
