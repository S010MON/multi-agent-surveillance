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

public class SaveMapTest {

    //@Test
    File createMapFile()
    {

        SaveMap saveMap = new SaveMap();
        Settings testSetting = FileParser.readGameFile("src/main/resources/map_1.txt");
        testSetting.setName("createFileTest");
        Map testMap = new Map(testSetting);
        saveMap.saveMap(testMap);

        String filePathNameTest = PathCreater.getPathMap() + "createFileTest.txt";

        try
        {
            Scanner testScanner = new Scanner(filePathNameTest);

            File mapFile = new File(filePathNameTest);

            System.out.println(mapFile.exists());
            System.out.println(mapFile.canRead());

            System.out.println("Succesfully created file");
            return mapFile;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("an exception occured why trying to create/access/delete the file");
        }
        return null;
    }

    @Test
    void compareFiles()
    {
        String filePathNameTest = PathCreater.getPathMap() + "createFileTest.txt";
        String filePathNameMap = "src/main/resources/map_1.txt";
        try
        {
            File testFile = createMapFile();
            File mapFile = new File(filePathNameMap);
            System.out.println(testFile.exists());
            System.out.println(testFile.canRead());

            System.out.println("try to create scanner for testFile");
            Scanner testScanner = new Scanner(testFile);
            System.out.println("succeeded to create scanner for testFile");
            Scanner mapScanner = new Scanner(mapFile);


            // read in the first 2 lines of each, as they will be different
            /**
            testScanner.nextLine();
            testScanner.nextLine();
            mapScanner.nextLine();
            mapScanner.nextLine();

            String testLine = testScanner.nextLine();
            String mapLine = mapScanner.nextLine();

            while (testLine!=null || mapLine!=null)
            {
                if(testLine==null || mapLine==null)
                {
                    fail("files don't have the same number of lines.");
                }
                assertEquals(testLine, mapLine);
            }
             */

            deleteMapFile();

        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("error occured while reading files");
        }
    }

    //@Test
    void deleteMapFile()
    {
        String filePathNameTest = PathCreater.getPathMap() + "createFileTest.txt";

        try
        {
            Path path = Paths.get(filePathNameTest);
            deleteIfExists(path);
        }
        catch(Exception e)
        {
            fail("an exception occured why trying to delete the file");
            e.printStackTrace();
        }
    }
}

