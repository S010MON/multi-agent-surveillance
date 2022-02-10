package app.controller;

import app.model.map.Map;
import javafx.geometry.Rectangle2D;
import java.awt.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SaveMap {

    public static void main(String[] args) {
        SaveMap saveMap = new SaveMap();
        Settings testSetting = FileParser.readGameFile("src/main/resources/map_1.txt");
        Map testMap = new Map(testSetting);
        saveMap.saveMap(testMap);
    }

    public void saveMap(Map map){
        try
        {
            String filePath = PathCreater.getPathMap();

            Settings setting = map.getSetting();
            String fileName = setting.getName();
            if (fileName == null)
                fileName = "Map_" + String.valueOf(Math.round(Math.random() * 100000) + ".txt");
            else
                fileName = fileName + ".txt";
            String pathFile = filePath + fileName;
                    System.out.println(pathFile);


            File mapFile = new File(pathFile);

            FileWriter writer = new FileWriter(pathFile);


            writeLine(writer, "name", setting.getName());
            writeLine(writer, "gameFile", filePath + fileName);
            writeLine(writer, "gameMode", Integer.toString(setting.getGamemode()));
            writeLine(writer, "height", Integer.toString(setting.getHeight()));
            writeLine(writer, "width", Integer.toString(setting.getWidth()));
            writeLine(writer, "scaling", Double.toString(setting.getScaling()));
            writeLine(writer, "numGuards", Integer.toString(setting.getNoOfGuards()));
            writeLine(writer, "numIntruders", Integer.toString(setting.getNoOfIntruders()));
            writeLine(writer, "baseSpeedIntruder", Double.toString(setting.getWalkSpeedIntruder()));
            writeLine(writer, "sprintSpeedIntruder", Double.toString(setting.getSprintSpeedIntruder()));
            writeLine(writer, "baseSpeedGuard", Double.toString(setting.getWalkSpeedGuard()));
            writeLine(writer, "sprintSpeedGuard", Double.toString(setting.getSprintSpeedGuard()));
            writeLine(writer, "timeStep", Double.toString(setting.getTimeStep()));
            writeLine(writer, "targetArea", setting.getTargetArea());
            writeLine(writer, "spawnAreaIntruders", setting.getSpawnAreaIntruders());
            writeLine(writer, "spawnAreaGuards", setting.getSpawnAreaGuards());

            ArrayList<Rectangle2D> walls = setting.getWalls();
            for(Rectangle2D wall : walls)
            {
                writeLine(writer, "wall", wall);
            }

            ArrayList<Rectangle2D> towers = setting.getTowers();
            for(Rectangle2D tower : towers)
            {
                writeLine(writer, "tower", tower);
            }


            ArrayList<Rectangle2D> portals = setting.getPortals();
            ArrayList<Point> teleportPoints = setting.getTeleportTo();
            ArrayList<Double> teleportOrientations = setting.getTeleportOrientations();
            for(int i=0; i<portals.size(); i++)
            {
                writeLineTeleport(writer, portals.get(i), teleportPoints.get(i), teleportOrientations.get(i));
            }

            ArrayList<Rectangle2D> shades = setting.getShade();
            for(Rectangle2D shade : shades)
            {
                writeLine(writer, "shaded", shade);
            }


            ArrayList<Rectangle2D> textures = setting.getTextures();
            ArrayList<Integer> textureTypes = setting.getTextureType();
            ArrayList<Integer> textureOrientations = setting.getTextureOrientations();
            for(int i=0; i<textures.size(); i++)
            {
                writeLineTexture(writer, textures.get(i), textureTypes.get(i), textureOrientations.get(i));
            }


            writer.close();
        }
        catch(Exception e)
        {
            System.out.println("An error occured while creating a map file.");
            e.printStackTrace();
        }

    }

    private void writeLineTexture(FileWriter writer, Rectangle2D rectangle, int textureType, int orientation) throws IOException
    {
        String rectangleString = Integer.toString((int)rectangle.getMinX()) + " "
                + Integer.toString((int)rectangle.getMinY()) + " "
                + Integer.toString((int)rectangle.getMaxX()) + " "
                + Integer.toString((int)rectangle.getMaxY());
        String typeAndOrientation = Integer.toString(textureType) + " " + Integer.toString(orientation);
        writer.write("texture = " + rectangleString + " " + typeAndOrientation + System.getProperty( "line.separator" ));
    }

    private void writeLineTeleport(FileWriter writer, Rectangle2D rectangle, Point point, double orientation) throws IOException
    {
        String portal = Integer.toString((int)rectangle.getMinX()) + " "
                + Integer.toString((int)rectangle.getMinY()) + " "
                + Integer.toString((int)rectangle.getMaxX()) + " "
                + Integer.toString((int)rectangle.getMaxY());
        String pointString = Integer.toString((int) point.getX()) + " " + Integer.toString((int) point.getY());
        String orient = Double.toString(orientation);
        writer.write("teleport = " + portal + " " + pointString + " " + orient + System.getProperty( "line.separator" ));
    }

    private void writeLine(FileWriter writer, String variable, Rectangle2D rectangle) throws IOException
    {
        String value = Integer.toString((int)rectangle.getMinX()) + " "
                + Integer.toString((int)rectangle.getMinY()) + " "
                + Integer.toString((int)rectangle.getMaxX()) + " "
                + Integer.toString((int)rectangle.getMaxY());
        writer.write(variable + " = " + value + System.getProperty( "line.separator" ));
    }

    private void writeLine(FileWriter writer, String variable, String value) throws IOException
    {
        writer.write(variable + " = " + value + System.getProperty( "line.separator" ));
    }

}
