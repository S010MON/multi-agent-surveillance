package testing;

import app.controller.settings.Settings;
import app.controller.settings.SettingsGenerator;
import app.model.CoverageMap;
import app.model.Map;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoverageTest
{
    @Test void testCoverageMapEmpty()
    {
        Settings settings = SettingsGenerator.coverageMapTest(10, 10);
        Map map = new Map(settings);
        CoverageMap cMap = new CoverageMap(map);
        boolean[][] act = cMap.getPixelMap();

        boolean[][] exp = {{true, true, true, true, true, true, true, true, true, true},
                           {true, false,false,false,false,false,false,false,false,true},
                           {true, false,false,false,false,false,false,false,false,true},
                           {true, false,false,false,false,false,false,false,false,true},
                           {true, false,false,false,false,false,false,false,false,true},
                           {true, false,false,false,false,false,false,false,false,true},
                           {true, false,false,false,false,false,false,false,false,true},
                           {true, false,false,false,false,false,false,false,false,true},
                           {true, false,false,false,false,false,false,false,false,true},
                           {true, true, true, true, true, true, true, true, true, true}};

        for(int i = 0; i < act.length; i++)
        {
            for(int j = 0; j < act[i].length; j++)
            {
                //assertEquals(exp[i][j], act[i][j]);
            }
        }
    }

    @Test void testCoverageMapWithFurniture()
    {
        Settings settings = SettingsGenerator.coverageMapTest(10, 10);
        settings.addFurniture(new Rectangle2D(3.5, 3.5, 3, 3), FurnitureType.WALL);
        Map map = new Map(settings);
        CoverageMap cMap = new CoverageMap(map);
        boolean[][] act = cMap.getPixelMap();

        boolean[][] exp = {{true, true, true, true, true, true, true, true, true, true},
                            {true, false,false,false,false,false,false,false,false,true},
                            {true, false,false,false,false,false,false,false,false,true},
                            {true, false,false,true ,true ,true ,true ,false,false,true},
                            {true, false,false,true ,false,false,true ,false,false,true},
                            {true, false,false,true ,false,false,true ,false,false,true},
                            {true, false,false,true ,true ,true ,true ,false,false,true},
                            {true, false,false,false,false,false,false,false,false,true},
                            {true, false,false,false,false,false,false,false,false,true},
                            {true, true, true, true, true, true, true, true, true, true}};

        for(int i = 0; i < act.length; i++)
        {
            for(int j = 0; j < act[i].length; j++)
            {
                //assertEquals(exp[i][j], act[i][j]);
            }
        }
    }
}
