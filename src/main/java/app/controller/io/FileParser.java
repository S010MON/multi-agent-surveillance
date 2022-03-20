package app.controller.io;

import app.controller.settings.Settings;
import app.model.furniture.FurnitureType;
import app.controller.linAlg.Vector;
import javafx.geometry.Rectangle2D;

import java.util.Scanner;

public class FileParser
{
    public static Settings parse(Scanner scanner)
    {
        Settings settings = new Settings();
        int countLines = 1;
        while (scanner.hasNextLine())
        {
            parseNextLine(settings, scanner.nextLine(), countLines);
            countLines++;
        }
        return settings;
    }

    public static void parseNextLine(Settings settings, String nextLine, int countLines)
    {
        try (Scanner scan = new Scanner(nextLine))
        {
            scan.useDelimiter("=");
            if (scan.hasNext())
            {
                String id = scan.next();
                String val = scan.next();
                id = id.trim();
                val = val.trim();
                String[] coords = val.split(" ");
                switch (id)
                {
                    case "name" -> settings.setName(val);
                    case "gameMode" -> settings.setGameMode(Integer.parseInt(val));
                    case "height" -> settings.setHeight(Integer.parseInt(val));
                    case "width" -> settings.setWidth(Integer.parseInt(val));
                    case "numGuards" -> settings.setNoOfGuards(Integer.parseInt(val));
                    case "numIntruders" -> settings.setNoOfIntruders(Integer.parseInt(val));
                    case "baseSpeedGuard" -> settings.setWalkSpeedGuard(Double.parseDouble(val));
                    case "sprintSpeedGuard" -> settings.setSprintSpeedGuard(Double.parseDouble(val));
                    case "baseSpeedIntruder" -> settings.setWalkSpeedIntruder(Double.parseDouble(val));
                    case "sprintSpeedIntruder" -> settings.setSprintSpeedIntruder(Double.parseDouble(val));
                    case "timeStep" -> settings.setTimeStep(Double.parseDouble(val));
                    case "scaling" -> settings.setScaling(Double.parseDouble(val));
                    case "wall" -> settings.addFurniture(rectangleOf(coords), FurnitureType.WALL);
                    case "shaded" -> settings.addFurniture(rectangleOf(coords), FurnitureType.SHADE);
                    case "glass" -> settings.addFurniture(rectangleOf(coords), FurnitureType.GLASS);
                    case "tower" -> settings.addFurniture(rectangleOf(coords), FurnitureType.TOWER);
                    case "targetArea" -> settings.addFurniture(rectangleOf(coords), FurnitureType.TARGET);
                    case "spawnAreaIntruders" -> settings.addFurniture(rectangleOf(coords), FurnitureType.INTRUDER_SPAWN);
                    case "spawnAreaGuards" -> settings.addFurniture(rectangleOf(coords), FurnitureType.GUARD_SPAWN);
                    case "teleport" -> {
                        Vector teleportTo = new Vector(Integer.parseInt(coords[4]), Integer.parseInt(coords[5]));
                        Double telportRotation = Double.parseDouble(coords[6]);
                        settings.addTeleport(rectangleOf(coords), teleportTo, telportRotation);
                    }
                    case "soundSource" -> settings.addSoundSource(new Vector(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])), Double.parseDouble(coords[2]));
                }
            }
        } catch (Exception e)
        {
            System.out.println("Line causing failure: " + countLines);
            System.out.println("Content of line: " + nextLine);
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
