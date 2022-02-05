package app.controller;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Settings
{
    private int gamemode;
    private int height;
    private int width;
    private int noOfGuards;
    private int noOfIntruders;
    private double walkSpeed;
    private double sprintSpeed;
    private double timeStep;
    private double scaling;
    private ArrayList<Rectangle2D> walls;
    private ArrayList<Rectangle2D> shade;
    private ArrayList<Rectangle2D> towers;
    private ArrayList<Rectangle2D> portals; // Test map contains an extra x, y pair for the location you get teleported to and a double for orientation
    private ArrayList<Rectangle2D> textures; // Test map includes an int for texture type and a double for orientation
    private Rectangle2D.Double targetArea;
    private Rectangle2D.Double spawnAreaIntruders;
    private Rectangle2D.Double spawnAreaGuards;

    public Settings(int gamemode, int height, int width, int noOfGuards, int noOfIntruders, double walkSpeed, double sprintSpeed, double timeStep, double scaling, Rectangle2D[] walls, Rectangle2D[] shade, Rectangle2D[] towers, Rectangle2D[] portals, Rectangle2D[] textures, Rectangle2D.Double targetArea, Rectangle2D.Double spawnAreaIntruders, Rectangle2D.Double spawnAreaGuards)
    {
        this.gamemode = gamemode;
        this.height = height;
        this.width = width;
        this.noOfGuards = noOfGuards;
        this.noOfIntruders = noOfIntruders;
        this.walkSpeed = walkSpeed;
        this.sprintSpeed = sprintSpeed;
        this.timeStep = timeStep;
        this.scaling = scaling;
        this.walls = walls;
        this.shade = shade;
        this.towers = towers;
        this.portals = portals;
        this.textures = textures;
        this.targetArea = targetArea;
        this.spawnAreaIntruders = spawnAreaIntruders;
        this.spawnAreaGuards = spawnAreaGuards;
    }

    public int getGamemode()
    {
        return this.gamemode;
    }

    public int getHeight()
    {
        return this.height;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getNoOfGuards()
    {
        return this.noOfGuards;
    }

    public int getNoOfIntruders()
    {
        return this.noOfIntruders;
    }

    public double getWalkSpeed()
    {
        return this.walkSpeed;
    }

    public double getSprintSpeed()
    {
        return this.sprintSpeed;
    }

    public double getTimeStep()
    {
        return this.timeStep;
    }

    public double getScaling()
    {
        return this.scaling;
    }

    public ArrayList<Rectangle2D> getWalls()
    {
        return (ArrayList<Rectangle2D>) this.walls.clone();
    }

    public ArrayList<Rectangle2D> getShade()
    {
        return (ArrayList<Rectangle2D>) this.shade.clone();
    }

    public ArrayList<Rectangle2D> getTowers()
    {
        return (ArrayList<Rectangle2D>) this.towers.clone();
    }

    public ArrayList<Rectangle2D> getPortals()
    {
        return (ArrayList<Rectangle2D>) this.portals.clone();
    }

    public ArrayList<Rectangle2D> getTextures()
    {
        return (ArrayList<Rectangle2D>) this.textures.clone();
    }

    public Rectangle2D.Double getTargetArea()
    {
        return this.targetArea;
    }

    public Rectangle2D.Double getSpawnAreaIntruders()
    {
        return this.spawnAreaIntruders;
    }

    public Rectangle2D.Double getSpawnAreaGuards()
    {
        return this.spawnAreaGuards;
    }
}
