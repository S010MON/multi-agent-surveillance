package app.controller;

import javafx.geometry.Rectangle2D;
import java.awt.Point;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileParser
{
    public static Settings readGameFile(String path)
    {
        Path file = Paths.get(path);
        Settings settings = new Settings();
        try(Scanner scan = new Scanner(file))
        {
            int countLines=1;
            while(scan.hasNextLine())
            {
                parseNextLine(settings, scan.nextLine(), countLines);
                countLines++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Failed creating scanner object using path supplied.");
        }
        settings.lock(); // Settings object now immutable
        return settings;
    }

    public static void parseNextLine(Settings settings, String nextLine, int countLines)
    {
        try(Scanner scan = new Scanner(nextLine))
        {
            scan.useDelimiter("=");
            if(scan.hasNext())
            {
                String id=scan.next();
                String val=scan.next();
                id = id.trim();
                val = val.trim();
                String[] coords = val.split(" ");
                switch(id)
                {
                    case "name" -> settings.setName(val);
                    case "gameMode" -> settings.setGamemode(Integer.parseInt(val));
                    case "height" -> settings.setHeight(Integer.parseInt(val));
                    case "width" -> settings.setWidth(Integer.parseInt(val));
                    case "numGuards" -> settings.setNoOfGuards(Integer.parseInt(val));
                    case "numIntruders" -> settings.setNoOfIntruders(Integer.parseInt(val));
                    case "baseSpeedGuard" -> settings.setWalkSpeedGuard(Double.parseDouble(val));
                    case "sprintSpeedGuard" -> settings.setSprintSpeedGuard(Double.parseDouble(val));
                    case "baseSpeedIntruder" -> settings.setWalkSpeedIntruder(Double.parseDouble(val));
                    case "sprintSpeedIntruder" -> settings.setSprintSpeedIntruder(Double.parseDouble(val));
                    case "timeStep" ->  settings.setTimeStep(Double.parseDouble(val));
                    case "scaling" -> settings.setScaling(Double.parseDouble(val));
                    case "wall" -> settings.addWall(rectangleOf(coords));
                    case "shaded" -> settings.addShade(rectangleOf(coords));
                    case "glass" ->  settings.addGlass(rectangleOf(coords));
                    case "tower" ->  settings.addTower(rectangleOf(coords));
                    case "targetArea" -> settings.setTargetArea(rectangleOf(coords));
                    case "spawnAreaIntruders" ->  settings.setSpawnAreaIntruders(rectangleOf(coords));
                    case "spawnAreaGuards" -> settings.setSpawnAreaGuards(rectangleOf(coords));
                    case "texture" -> {
                            settings.addTexture(rectangleOf(coords));
                            settings.addTextureType(Integer.parseInt(coords[4]));
                            settings.addTextureOrientation(Integer.parseInt(coords[5])); }
                    case "teleport" -> {
                        settings.addPortal(rectangleOf(coords));
                        settings.addTeleportTo(new Point(Integer.parseInt(coords[4]),
                                                    Integer.parseInt(coords[5])));
                        settings.addTeleportOrientation(Double.parseDouble(coords[6]));}
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Line causing failure: "+countLines);
            System.out.println("Content of line: "+nextLine);
        }
    }

    private static Rectangle2D rectangleOf(String[] coords)
    {
        return new Rectangle2D(Double.parseDouble(coords[0]),
                               Double.parseDouble(coords[1]),
                         Integer.parseInt(coords[2]) - Integer.parseInt(coords[0]),
                         Integer.parseInt(coords[3]) - Integer.parseInt(coords[1]));
    }
}
