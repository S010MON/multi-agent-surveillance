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

            // use the scaling factor (can only do after cause don't know if order of variables can change
            double scale = map.getScaling();
            map.setHeight(((int)(map.getHeight()*scale)));
            map.setWidth(((int)(map.getWidth()*scale)));
            map.setWalkSpeedGuard(map.getWalkSpeedGuard()*scale);
            map.setWalkSpeedIntruder(map.getWalkSpeedIntruder()*scale);
            map.setSprintSpeedGuard(map.getSprintSpeedGuard()*scale);
            map.setSprintSpeedIntruder(map.getSprintSpeedIntruder()*scale);

            ArrayList<Rectangle2D> walls = map.getWalls();
            ArrayList<Rectangle2D> newWalls = new ArrayList<>();
            for(Rectangle2D rec: walls)
            {
                Rectangle2D newRec = new Rectangle2D(rec.getMinX()*scale, rec.getMinY()*scale, rec.getWidth()*scale, rec.getHeight()*scale);
                newWalls.add(newRec);
            }
            map.setWalls(newWalls);

            ArrayList<Rectangle2D> shade = map.getShade();
            ArrayList<Rectangle2D> newShade = new ArrayList<>();
            for(Rectangle2D rec: shade)
            {
                Rectangle2D newRec = new Rectangle2D(rec.getMinX()*scale, rec.getMinY()*scale, rec.getWidth()*scale, rec.getHeight()*scale);
                newShade.add(newRec);
            }
            map.setShade(newShade);

            ArrayList<Rectangle2D> glass = map.getGlass();
            ArrayList<Rectangle2D> newGlass = new ArrayList<>();
            for(Rectangle2D rec: glass)
            {
                Rectangle2D newRec = new Rectangle2D(rec.getMinX()*scale, rec.getMinY()*scale, rec.getWidth()*scale, rec.getHeight()*scale);
                newGlass.add(newRec);
            }
            map.setGlass(newGlass);

            ArrayList<Rectangle2D> towers = map.getTowers();
            ArrayList<Rectangle2D> newTowers = new ArrayList<>();
            for(Rectangle2D rec: towers)
            {
                Rectangle2D newRec = new Rectangle2D(rec.getMinX()*scale, rec.getMinY()*scale, rec.getWidth()*scale, rec.getHeight()*scale);
                newTowers.add(newRec);
            }
            map.setTowers(newTowers);

            ArrayList<Rectangle2D> portals = map.getPortals();
            ArrayList<Rectangle2D> newPortals = new ArrayList<>();
            for(Rectangle2D rec: portals)
            {
                Rectangle2D newRec = new Rectangle2D(rec.getMinX()*scale, rec.getMinY()*scale, rec.getWidth()*scale, rec.getHeight()*scale);
                newPortals.add(newRec);
            }
            map.setPortals(newPortals);

            ArrayList<Point> teleportTo = map.getTeleportTo();
            ArrayList<Point> newTeleportTo = new ArrayList<>();
            for(Point p: teleportTo)
            {
                p.setLocation(p.getX()*scale, p.getY()*scale);
                newTeleportTo.add(p);
            }
            map.setTeleportTo(newTeleportTo);

            ArrayList<Rectangle2D> textures = map.getTextures();
            ArrayList<Rectangle2D> newTextures = new ArrayList<>();
            for(Rectangle2D rec: textures)
            {
                Rectangle2D newRec = new Rectangle2D(rec.getMinX()*scale, rec.getMinY()*scale, rec.getWidth()*scale, rec.getHeight()*scale);
                newTextures.add(newRec);
            }
            map.setTextures(newTextures);

            Rectangle2D targetArea = map.getTargetArea();
            map.setTargetArea(new Rectangle2D(targetArea.getMinX()*scale, targetArea.getMinY()*scale, targetArea.getWidth()*scale, targetArea.getHeight()*scale));

            Rectangle2D spawnIntrud = map.getSpawnAreaIntruders();
            map.setSpawnAreaIntruders(new Rectangle2D(spawnIntrud.getMinX()*scale, spawnIntrud.getMinY()*scale, spawnIntrud.getWidth()*scale, spawnIntrud.getHeight()*scale));

            Rectangle2D spawnGuards = map.getSpawnAreaGuards();
            map.setSpawnAreaGuards(new Rectangle2D(spawnGuards.getMinX()*scale, spawnGuards.getMinY()*scale, spawnGuards.getWidth()*scale, spawnGuards.getHeight()*scale));
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
