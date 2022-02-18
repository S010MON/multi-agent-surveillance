package app.controller;

import javafx.geometry.Rectangle2D;
import java.awt.Point;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileParser
{
    private static Settings map;

    public static Settings readGameFile(String path)
    {
        Path file = Paths.get(path);
        map = new Settings();
        try(Scanner scan = new Scanner(file))
        {
            int countLines=1;
            while(scan.hasNextLine())
            {
                parseNextLine(scan.nextLine(), countLines);
                countLines++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Failed creating scanner object using path supplied.");
        }
        map.lock(); // Settings object now immutable
        return map;
    }

    public static void parseNextLine(String nextLine, int countLines)
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
                    case "name" -> map.setName((String) val);
                    case "gameMode" -> map.setGamemode(Integer.parseInt(val));
                    case "height" -> map.setHeight(Integer.parseInt(val));
                    case "width" -> map.setWidth(Integer.parseInt(val));
                    case "numGuards" -> map.setNoOfGuards(Integer.parseInt(val));
                    case "numIntruders" -> map.setNoOfIntruders(Integer.parseInt(val));
                    case "baseSpeedGuard" -> map.setWalkSpeedGuard(Double.parseDouble(val));
                    case "sprintSpeedGuard" -> map.setSprintSpeedGuard(Double.parseDouble(val));
                    case "baseSpeedIntruder" -> map.setWalkSpeedIntruder(Double.parseDouble(val));
                    case "sprintSpeedIntruder" -> map.setSprintSpeedIntruder(Double.parseDouble(val));
                    case "timeStep" ->  map.setTimeStep(Double.parseDouble(val));
                    case "scaling" -> map.setScaling(Double.parseDouble(val));
                    case "wall" -> map.addWall(rectangleOf(coords));
                    case "shaded" -> map.addShade(rectangleOf(coords));
                    case "glass" ->  map.addGlass(rectangleOf(coords));
                    case "tower" ->  map.addTower(rectangleOf(coords));
                    case "targetArea" -> map.setTargetArea(rectangleOf(coords));
                    case "spawnAreaIntruders" ->  map.setSpawnAreaIntruders(rectangleOf(coords));
                    case "spawnAreaGuards" -> map.setSpawnAreaGuards(rectangleOf(coords));
                    case "texture" -> {
                            map.addTexture(rectangleOf(coords));
                            map.addTextureType(Integer.parseInt(coords[4]));
                            map.addTextureOrientation(Integer.parseInt(coords[5])); }
                    case "teleport" -> {
                        map.addPortal(rectangleOf(coords));
                        map.addTeleportTo(new Point(Integer.parseInt(coords[4]),
                                                    Integer.parseInt(coords[5])));
                        map.addTeleportOrientation(Double.parseDouble(coords[6]));}
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
