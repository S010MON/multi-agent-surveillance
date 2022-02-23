package app.controller.io;

import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.controller.settings.SettingsObject;
import javafx.geometry.Rectangle2D;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public abstract class FileSaver
{
    private static final String newLn = System.getProperty( "line.separator" );

    public static void save(File file, Settings settings)
    {
        try
        {
            StringBuilder sb = new StringBuilder();

            sb.append(toLine("name", settings.getName()));
            sb.append(toLine("gameFile", file.getPath()));
            sb.append(toLine("gameMode", settings.getGameMode()));
            sb.append(toLine("height", settings.getHeight()));
            sb.append(toLine("width", settings.getWidth()));
            sb.append(toLine("scaling", settings.getScaling()));
            sb.append(toLine("numGuards", settings.getNoOfGuards()));
            sb.append(toLine("numIntruders", settings.getNoOfIntruders()));
            sb.append(toLine("baseSpeedIntruder", settings.getWalkSpeedIntruder()));
            sb.append(toLine("sprintSpeedIntruder", settings.getSprintSpeedIntruder()));
            sb.append(toLine("baseSpeedGuard", settings.getWalkSpeedGuard()));
            sb.append(toLine("sprintSpeedGuard", settings.getSprintSpeedGuard()));
            sb.append(toLine("timeStep", settings.getTimeStep()));

            settings.getFurniture().forEach(e -> sb.append(e.toString()).append(newLn));

            FileWriter writer = new FileWriter(file);
            writer.write(sb.toString());
            writer.close();
        }
        catch(Exception e)
        {
            System.out.println("An error occurred while creating a map file\n");
            e.printStackTrace();
        }

    }

    private static String toLine(String variable, String value)
    {
        return variable + " = " + value + newLn;
    }

    private static String toLine(String variable, int value)
    {
        return variable + " = " + value + newLn;
    }

    private static String toLine(String variable, double value)
    {
        return variable + " = " + value + newLn;
    }

}
