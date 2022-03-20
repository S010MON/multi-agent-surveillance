package testing;

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
}
