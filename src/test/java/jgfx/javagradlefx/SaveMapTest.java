package jgfx.javagradlefx;

import app.controller.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import java.io.File;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveMapTest {

    @BeforeAll
    void createMapFile()
    {
        Settings testSetting = FileParser.readGameFile("src/main/resources/map_1.txt");
        SaveMap.saveMap(testSetting);

        String filePathNameTest = FilePath.getFilePath("Save_map_1.txt");

        try
        {
            Scanner testScanner = new Scanner(filePathNameTest);
            testScanner.close();

            File mapFile = new File(FilePath.getFilePath("Save_map_1.txt"));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            fail("an exception occured why trying to create/access/delete the file");
        }
    }

    @Test
    void compareFiles()
    {
        String filePathNameTest = FilePath.getFilePath("Save_map_1.txt");
        String filePathNameMap = "src/main/resources/map_1.txt";
        try
        {
            File testFile = new File(filePathNameTest);
            File mapFile = new File(filePathNameMap);

            Scanner testScanner = new Scanner(testFile);
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

    }

    @AfterAll
    void deleteMapFile()
    {
        String filePathNameTest = FilePath.getFilePath("Save_map_1.txt");

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
    }
}

