package app.controller.io;

import app.controller.Settings;
import javafx.geometry.Rectangle2D;
import java.awt.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
            sb.append(toLine("gameFile", "filePath"));
            sb.append(toLine("gameMode", settings.getGamemode()));
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
            sb.append(toLine("targetArea", settings.getTargetArea()));
            sb.append(toLine("spawnAreaIntruders", settings.getSpawnAreaIntruders()));
            sb.append(toLine("spawnAreaGuards", settings.getSpawnAreaGuards()));


            settings.getWalls().forEach(r -> sb.append(toLine("wall", r)));
            settings.getTowers().forEach(t -> sb.append(toLine("tower", t)));


            ArrayList<Rectangle2D> portals = settings.getPortals();
            ArrayList<Point> teleportPoints = settings.getTeleportTo();
            ArrayList<Double> teleportOrientations = settings.getTeleportOrientations();
            for(int i=0; i<portals.size(); i++)
            {
                sb.append(toLine(portals.get(i), teleportPoints.get(i), teleportOrientations.get(i)));
            }

            ArrayList<Rectangle2D> shades = settings.getShade();
            for(Rectangle2D shade : shades)
            {
                sb.append(toLine("shaded", shade));
            }

            FileWriter writer = new FileWriter(file);
            writer.write(sb.toString());
            writer.close();
        }
        catch(Exception e)
        {
            System.out.println("An error occured while creating a map file.");
            e.printStackTrace();
        }

    }

    private static String toLine(Rectangle2D rectangle, int textureType, int orientation)
    {
        String rectangleString = (int) rectangle.getMinX() + " "
                + (int) rectangle.getMinY() + " "
                + (int) rectangle.getMaxX() + " "
                + (int) rectangle.getMaxY();
        String typeAndOrientation = textureType + " " + orientation;
        return "texture = " + rectangleString + " " + typeAndOrientation + newLn;
    }

    private static String toLine(Rectangle2D rectangle, Point point, double orientation)
    {
        String portal = (int) rectangle.getMinX() + " "
                + (int) rectangle.getMinY() + " "
                + (int) rectangle.getMaxX() + " "
                + (int) rectangle.getMaxY();
        String pointString = (int) point.getX() + " " + (int) point.getY();
        String orient = Double.toString(orientation);
        return "teleport = " + portal + " " + pointString + " " + orient + newLn;
    }

    private static String toLine(String variable, Rectangle2D rectangle)
    {
        String value = (int) rectangle.getMinX() + " "
                + (int) rectangle.getMinY() + " "
                + (int) rectangle.getMaxX() + " "
                + (int) rectangle.getMaxY();
        return variable + " = " + value + newLn;
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
