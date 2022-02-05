package app.controller;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Settings
{
    private boolean unlocked = true;
    private int gamemode;
    private int height;
    private int width;
    private int noOfGuards;
    private int noOfIntruders;
    private double walkSpeedGuard;
    private double sprintSpeedGuard;
    private double walkSpeedIntruder;
    private double sprintSpeedIntruder;
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

    public void setGamemode(int gamemode)
    {
        if(unlocked)
            this.gamemode = gamemode;
    }

    public void setHeight(int height)
    {
        if(unlocked)
            this.height = height;
    }

    public void setWidth(int width)
    {
        if(unlocked)
            this.width = width;
    }

    public void setNoOfGuards(int noOfGuards)
    {
        if(unlocked)
            this.noOfGuards = noOfGuards;
    }

    public void setNoOfIntruders(int noOfIntruders)
    {
        if(unlocked)
            this.noOfIntruders = noOfIntruders;
    }

    public void setWalkSpeedGuard(double walkSpeedGuard)
    {
        if(unlocked)
            this.walkSpeedGuard = walkSpeedGuard;
    }

    public void setSprintSpeedGuard(double sprintSpeedGuard)
    {
        if(unlocked)
            this.sprintSpeedGuard = sprintSpeedGuard;
    }

    public void setWalkSpeedIntruder(double walkSpeedIntruder)
    {
        if(unlocked)
            this.walkSpeedIntruder = walkSpeedIntruder;
    }

    public void setSprintSpeedIntruder(double sprintSpeedIntruder)
    {
        if(unlocked)
            this.sprintSpeedIntruder = sprintSpeedIntruder;
    }

    public void setTimeStep(double timeStep)
    {
        if(unlocked)
            this.timeStep = timeStep;
    }

    public void setScaling(double scaling)
    {
        if(unlocked)
            this.scaling = scaling;
    }

    public void setWalls()
    {
        if(unlocked)
            this.walls = new ArrayList<>();
    }

    public void addWall(Rectangle2D wall)
    {
        if(unlocked)
            this.walls.add(wall);
    }

    public void setShade()
    {
        if(unlocked)
            this.shade = new ArrayList<>();
    }

    public void addShade(Rectangle2D shade)
    {
        if(unlocked)
            this.walls.add(shade);
    }

    public void setTowers()
    {
        if(unlocked)
            this.towers = new ArrayList<>();
    }

    public void addTower(Rectangle2D tower)
    {
        if(unlocked)
            this.walls.add(tower);
    }

    public void setPortals()
    {
        if(unlocked)
            this.portals = new ArrayList<>();
    }

    public void addPortal(Rectangle2D portal)
    {
        if(unlocked)
            this.walls.add(portal);
    }

    public void setTextures()
    {
        if(unlocked)
            this.textures = new ArrayList<>();
    }

    public void addTexture(Rectangle2D texture)
    {
        if(unlocked)
            this.walls.add(texture);
    }

    public void setTargetArea(Rectangle2D.Double targetArea)
    {
        if(unlocked)
            this.targetArea = targetArea;
    }

    public void setSpawnAreaIntruders(Rectangle2D.Double spawnAreaIntruders)
    {
        if(unlocked)
            this.spawnAreaIntruders = spawnAreaIntruders;
    }

    public void setSpawnAreaGuards(Rectangle2D.Double spawnAreaGuards)
    {
        if(unlocked)
            this.spawnAreaGuards = spawnAreaGuards;
    }

    public void lock()
    {
        this.unlocked = false;
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

    public double getWalkSpeedGuard()
    {
        return this.walkSpeedGuard;
    }

    public double getSprintSpeedGuard()
    {
        return this.sprintSpeedGuard;
    }

    public double getWalkSpeedIntruder()
    {
        return this.walkSpeedIntruder;
    }

    public double getSprintSpeedIntruder()
    {
        return this.sprintSpeedIntruder;
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
