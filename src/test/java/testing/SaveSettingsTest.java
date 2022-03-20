package testing;

import app.controller.settings.Settings;
import app.controller.settings.SettingsGenerator;
import app.controller.io.FileManager;
import app.controller.io.FilePath;
import app.controller.settings.SettingsObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveSettingsTest
{

    /**
     * Delete any exiting test files and create a new test file
     */
    @BeforeAll void createMapFile()
    {

        File oldFile = new File(FilePath.get("Save_map_saveSetting_test.txt"));
        if (oldFile.exists())
            oldFile.delete();
        Settings testSetting = FileManager.loadSettings("src/test/resources/map_saveSetting_test.txt");

        FileManager.saveSettings(testSetting, "Save_map_saveSetting_test.txt");

        String filePathNameTest = FilePath.get("Save_map_saveSetting_test.txt");


        try
        {
            Scanner testScanner = new Scanner(filePathNameTest);
            testScanner.close();

            File mapFile = new File(FilePath.get("Save_map_saveSetting_test.txt"));

        } catch (Exception e)
        {
            e.printStackTrace();
            fail("an exception occured while trying to create/access/delete the file");
        }
    }

    @Test
    void testSaveSettings()
    {
        String filePath = FilePath.get("Save_map_saveSetting_test.txt");

        //FileManager.saveSettings(SettingsGenerator.saveSettingsTest());
        String[] exp = expected();

        ArrayList<String> act = new ArrayList<>();
        try
        {
            File testFile = new File(filePath);
            Scanner scanner = new Scanner(testFile);

            while (scanner.hasNextLine())
            {
                act.add(scanner.nextLine());
            }
            scanner.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            fail("error occured while reading files");
        }

        // Assert both are the same length
        assertEquals(exp.length, act.size());

        // Check each line content
        for (int i = 0; i < exp.length; i++)
        {
            assertEquals(exp[i], act.get(i));
        }
    }

    /**
     * Clean up
     */
    @AfterAll void deleteMapFile()
    {
        File oldFile = new File(FilePath.get("Save_map_saveSetting_test.txt"));
        if (oldFile.exists())
            oldFile.delete();
    }

    private String[] expected()
    {
        return new String[]
                {
                        "name = map_saveSetting_test",
                        "gameFile = " + FilePath.get("Save_map_saveSetting_test.txt"),
                        "gameMode = 1",
                        "height = 95",
                        "width = 144",
                        "scaling = 0.2",
                        "numGuards = 7",
                        "numIntruders = 4",
                        "baseSpeedIntruder = 13.0",
                        "sprintSpeedIntruder = 21.0",
                        "baseSpeedGuard = 13.0",
                        "sprintSpeedGuard = 21.0",
                        "timeStep = 0.5",
                        "targetArea = 20 40 25 45",
                        "spawnAreaIntruders = 500 100 560 160",
                        "spawnAreaGuards = 100 500 160 560",
                        "wall = 10 10 110 110",
                        "wall = 200 200 220 220",
                        "wall = 400 100 440 420",
                        "glass = 320 320 350 350",
                        "tower = 0 23 50 63",
                        "tower = 12 40 20 44",
                        "teleport = 20 70 25 75 90 50 0.0",
                        "shaded = 10 20 20 40",
                        "soundSource = 20 66 93"
                };
    }
}

