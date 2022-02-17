package app.controller;

import javafx.geometry.Rectangle2D;
import java.awt.Point;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
                    case "name":
                        map.setName((String) val);
                        break;
                    case "gameMode":
                        map.setGamemode(Integer.parseInt(val));
                        break;
                    case "height":
                        map.setHeight(Integer.parseInt(val));
                        break;
                    case "width":
                        map.setWidth(Integer.parseInt(val));
                        break;
                    case "numGuards":
                        map.setNoOfGuards(Integer.parseInt(val));
                        break;
                    case "numIntruders":
                        map.setNoOfIntruders(Integer.parseInt(val));
                        break;
                    case "baseSpeedGuard":
                        map.setWalkSpeedGuard(Double.parseDouble(val));
                        break;
                    case "sprintSpeedGuard":
                        map.setSprintSpeedGuard(Double.parseDouble(val));
                        break;
                    case "baseSpeedIntruder":
                        map.setWalkSpeedIntruder(Double.parseDouble(val));
                        break;
                    case "sprintSpeedIntruder":
                        map.setSprintSpeedIntruder(Double.parseDouble(val));
                        break;
                    case "timeStep":
                        map.setTimeStep(Double.parseDouble(val));
                        break;
                    case "scaling":
                        map.setScaling(Double.parseDouble(val));
                        break;
                    case "wall":
                        map.addWall(new Rectangle2D(
                                Double.parseDouble(coords[0]),
                                Double.parseDouble(coords[1]),
                                Integer.parseInt(coords[2]) - Integer.parseInt(coords[0]),
                                Integer.parseInt(coords[3]) - Integer.parseInt(coords[1])));
                        break;
                    case "shaded":
                        map.addShade(new Rectangle2D(Double.parseDouble(coords[0]),
                                Double.parseDouble(coords[1]),
                                Integer.parseInt(coords[2]) - Integer.parseInt(coords[0]),
                                Integer.parseInt(coords[3]) - Integer.parseInt(coords[1])));
                    case "glass":
                        map.addGlass(new Rectangle2D(Double.parseDouble(coords[0]),
                                Double.parseDouble(coords[1]),
                                Integer.parseInt(coords[2]) - Integer.parseInt(coords[0]),
                                Integer.parseInt(coords[3]) - Integer.parseInt(coords[1])));
                        break;
                    case "tower":
                        map.addTower(new Rectangle2D(Double.parseDouble(coords[0]),
                                Double.parseDouble(coords[1]),
                                Integer.parseInt(coords[2]) - Integer.parseInt(coords[0]),
                                Integer.parseInt(coords[3]) - Integer.parseInt(coords[1])));
                        break;
                    case "teleport":
                        map.addPortal(new Rectangle2D(Double.parseDouble(coords[0]),
                                Double.parseDouble(coords[1]),
                                Integer.parseInt(coords[2]) - Integer.parseInt(coords[0]),
                                Integer.parseInt(coords[3]) - Integer.parseInt(coords[1])));
                        map.addTeleportTo(new Point(Integer.parseInt(coords[4]),
                                                    Integer.parseInt(coords[5])));
                        map.addTeleportOrientation(Double.parseDouble(coords[6]));
                        break;
                    case "texture":
                        map.addTexture(new Rectangle2D(Double.parseDouble(coords[0]),
                                Double.parseDouble(coords[1]),
                                Integer.parseInt(coords[2]) - Integer.parseInt(coords[0]),
                                Integer.parseInt(coords[3]) - Integer.parseInt(coords[1])));
                        map.addTextureType(Integer.parseInt(coords[4]));
                        map.addTextureOrientation(Integer.parseInt(coords[5]));
                        break;
                    case "targetArea":
                        map.setTargetArea(new Rectangle2D(Double.parseDouble(coords[0]),
                                Double.parseDouble(coords[1]),
                                Integer.parseInt(coords[2]) - Integer.parseInt(coords[0]),
                                Integer.parseInt(coords[3]) - Integer.parseInt(coords[1])));
                        break;
                    case "spawnAreaIntruders":
                        map.setSpawnAreaIntruders(new Rectangle2D(Double.parseDouble(coords[0]),
                                Double.parseDouble(coords[1]),
                                Integer.parseInt(coords[2]) - Integer.parseInt(coords[0]),
                                Integer.parseInt(coords[3]) - Integer.parseInt(coords[1])));
                        break;
                    case "spawnAreaGuards":
                        map.setSpawnAreaGuards(new Rectangle2D(Double.parseDouble(coords[0]),
                                Double.parseDouble(coords[1]),
                                Integer.parseInt(coords[2]) - Integer.parseInt(coords[0]),
                                Integer.parseInt(coords[3]) - Integer.parseInt(coords[1])));
                        break;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println("Line causing failure: "+countLines);
            System.out.println("Content of line: "+nextLine);
        }
    }
}
