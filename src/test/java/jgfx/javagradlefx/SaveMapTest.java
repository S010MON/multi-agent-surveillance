package jgfx.javagradlefx;

import app.controller.Settings;
import app.model.map.Map;
import app.model.map.SaveMap;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static java.nio.file.Files.deleteIfExists;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SaveMapTest {

    @Test
    void CreateFile()
    {
        Settings settings = new Settings();
        settings.setName("createFileTest");
        Map map = new Map(settings);

        SaveMap safeMap = new SaveMap();
        safeMap.saveMap(map);

        String filePathName = "src/main/java/app/model/map/files/createFileTest.txt";

        try
        {
            Scanner scanner = new Scanner(filePathName);

            Path path = Paths.get(filePathName);
            deleteIfExists(path);
        }
        catch(Exception e)
        {
            fail("an exception occured why trying to access the file");
        }
    }
}
