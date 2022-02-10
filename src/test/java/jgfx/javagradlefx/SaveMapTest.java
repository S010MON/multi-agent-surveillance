package jgfx.javagradlefx;

import app.controller.FileParser;
import app.controller.PathCreater;
import app.controller.Settings;
import app.model.map.Map;
import app.controller.SaveMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static java.nio.file.Files.deleteIfExists;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveMapTest {

    @BeforeAll
    void createMapFile()
    {
        System.out.println("call to createMapFile");

        Settings testSetting = FileParser.readGameFile("src/main/resources/map_1.txt");
        SaveMap.saveMap(testSetting);

        String filePathNameTest = PathCreater.getPathMap("Save_map_1.txt");
                //PathCreater.getPathMap("Save_map_1.txt");

        try
        {
            Scanner testScanner = new Scanner(filePathNameTest);
            testScanner.close();

            File mapFile = new File(PathCreater.getPathMap("Save_map_1.txt"));

            System.out.println("create map, file exist: " + mapFile.exists());
            System.out.println("create map, file can be read: " + mapFile.canRead());

            System.out.println("Succesfully created file");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("an exception occured why trying to create/access/delete the file");
        }
        System.out.println();
    }

    @Test
    void compareFiles()
    {
        String filePathNameTest = PathCreater.getPathMap("Save_map_1.txt");
        String filePathNameMap = "src/main/resources/map_1.txt";
        try
        {
            File testFile = new File(filePathNameTest);
            File mapFile = new File(filePathNameMap);

            System.out.println("create map, file exist: " + testFile.exists());
            System.out.println("create map, file can be read: " + testFile.canRead());

            System.out.println("try to create scanner for testFile");
            Scanner testScanner = new Scanner(testFile);
            System.out.println("succeeded to create scanner for testFile");
            Scanner mapScanner = new Scanner(mapFile);


            // read in the first 2 lines of each, as they will be different
            testScanner.nextLine();
            testScanner.nextLine();
            mapScanner.nextLine();
            mapScanner.nextLine();

            String testLine;
            String mapLine;

            while (testScanner.hasNext() || mapScanner.hasNext())
            {
                if(!testScanner.hasNext() || !mapScanner.hasNext())
                {
                    fail("files don't have the same number of lines.");
                }
                testLine = testScanner.nextLine();
                mapLine = mapScanner.nextLine();
                assertEquals(testLine, mapLine);
            }

            testScanner.close();
            mapScanner.close();


        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("error occured while reading files");
        }
        System.out.println();
    }

    @AfterAll
    void deleteMapFile()
    {
        System.out.println("call to deleteMapFile");
        String filePathNameTest = PathCreater.getPathMap("Save_map_1.txt");

        try
        {
            File testFile = new File(filePathNameTest);
            if(!testFile.delete())
                fail("Failed to delete file");
        }
        catch(Exception e)
        {
            fail("an exception occured why trying to delete the file");
            e.printStackTrace();
        }
        System.out.println();
    }
}

