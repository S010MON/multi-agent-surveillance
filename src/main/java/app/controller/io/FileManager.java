package app.controller.io;

import app.controller.settings.Settings;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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

    public static void saveSettings(Settings settings)
    {
        String filePath;
        if (settings.getName() == null)
            filePath = FilePath.get("Save_Map_" + LocalDateTime.now() + ".txt");
        else
            filePath = FilePath.get("Save_" + settings.getName() + ".txt");

        File file = new File(filePath);
        FileSaver.save(file, settings);
    }
}
