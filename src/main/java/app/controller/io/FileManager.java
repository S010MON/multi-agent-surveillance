package app.controller.io;

import app.controller.settings.Settings;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileManager
{
    public static Settings loadSettings(String path)
    {
        Path file = Paths.get(path);
        try(Scanner scan = new Scanner(file))
        {
            return FileParser.parse(scan);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Failed creating scanner object using path supplied.");
        }
        return null;
    }

    public static void saveSettings(Settings settings, String fileName)
    {
        String filePath = FilePath.get(fileName);

        File file = new File(filePath);
        FileSaver.save(file, settings);
    }
}
