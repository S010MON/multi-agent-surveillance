package jgfx.javagradlefx;

import app.controller.Settings;
import app.model.map.MapNotTemp;
import app.model.map.SafeMap;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static java.nio.file.Files.deleteIfExists;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SafeMapTest {

    @Test
    void CreateFile()
    {
        Settings settings = new Settings();
        settings.setName("createFileTest");
        MapNotTemp map = new MapNotTemp(settings);

        SafeMap safeMap = new SafeMap();
        safeMap.safeMap(map);

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
