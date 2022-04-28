package testing;

import app.controller.linAlg.Vector;
import app.controller.settings.SettingsGenerator;
import app.model.Map;
import app.model.furniture.FurnitureType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FurnitureTest
{
    @Test void TestGetTypeInside()
    {
        Map map = new Map(SettingsGenerator.furnitureTypeTest());
        FurnitureType exp = FurnitureType.WALL;
        FurnitureType act = map.furnitureAt(new Vector(70, 70));
        assertEquals(exp, act);
    }

    @Test void TestGetTypeOutside()
    {
        Map map = new Map(SettingsGenerator.furnitureTypeTest());
        FurnitureType act = map.furnitureAt(new Vector(25, 25));
        assertNull(act);
    }
}
