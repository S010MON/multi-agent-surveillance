package testing;

import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.controller.settings.Settings;
import app.controller.settings.SettingsGenerator;
import app.model.CoverageMap;
import app.model.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoverageTest
{
    @Test void testCoverageMapEmpty()
    {
        Settings settings = SettingsGenerator.coverageMapTest(10, 10);
        Map map = new Map(settings);
        CoverageMap cMap = new CoverageMap(map);

        assertEquals(40, cMap.getVectors().size());
    }

    @Test void testCoverageMapPercent_none()
    {
        Settings settings = SettingsGenerator.coverageMapTest(10, 10);
        Map map = new Map(settings);
        CoverageMap cMap = new CoverageMap(map);
        VectorSet seen = new VectorSet();
        assertEquals(0d, cMap.percentSeen(seen));
    }

    @Test void testCoverageMapPercent_half()
    {
        Settings settings = SettingsGenerator.coverageMapTest(10, 10);
        Map map = new Map(settings);
        CoverageMap cMap = new CoverageMap(map);
        VectorSet seen = new VectorSet();


        for(double x = 0; x <= 10; x++)
        {
            Vector v = new Vector(x, 0);
            seen.add(v);
        }

        for(double x = 0; x <= 10; x++)
        {
            Vector v = new Vector(x, 10);
            seen.add(v);
        }


        assertEquals(0.5d, cMap.percentSeen(seen), 0.1);
    }

    @Test void testCoverageMapPercent_full()
    {
        Settings settings = SettingsGenerator.coverageMapTest(10, 10);
        Map map = new Map(settings);
        CoverageMap cMap = new CoverageMap(map);
        VectorSet seen = new VectorSet();

        for(double x = 0; x <= 10; x++)
        {
            Vector v1 = new Vector(x, 0);
            seen.add(v1);
            Vector v2 = new Vector(x, 10);
            seen.add(v2);
        }

        for(double y = 0; y <= 10; y++)
        {
            Vector v1 = new Vector(0, y);
            seen.add(v1);
            Vector v2 = new Vector(10, y);
            seen.add(v2);
        }

        assertEquals(1d, cMap.percentSeen(seen));
    }
}
