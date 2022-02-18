package app.controller.io;

import app.controller.Settings;

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
        String fileName = settings.getName();
        if (fileName == null)
            fileName = "Save_Map_" + LocalDateTime.now() + ".txt";
        else
            fileName = "Save_" + fileName + ".txt";

        String filePath = FilePath.getFilePath(fileName);
        File file = new File(filePath);
        FileSaver.save(file, settings);
    }
}
