package app.controller;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

// Orientation, which is double, only applies to teleporting and adding texture, not saved as a setting, can be if needed.
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
    private ArrayList<Rectangle2D> portals;
    private ArrayList<Point> teleportTo;
    private ArrayList<Rectangle2D> textures;
    private ArrayList<Integer> textureType;
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
            this.shade.add(shade);
    }

    public void setTowers()
    {
        if(unlocked)
            this.towers = new ArrayList<>();
    }

    public void addTower(Rectangle2D tower)
    {
        if(unlocked)
            this.towers.add(tower);
    }

    public void setPortals()
    {
        if(unlocked)
            this.portals = new ArrayList<>();
    }

    public void addPortal(Rectangle2D portal)
    {
        if(unlocked)
            this.portals.add(portal);
    }

    public void setTeleportTo()
    {
        if(unlocked)
            this.teleportTo = new ArrayList<>();
    }

    public void addTeleportTo(Point p)
    {
        if(unlocked)
            this.teleportTo.add(p);
    }

    public void setTextures()
    {
        if(unlocked)
            this.textures = new ArrayList<>();
    }

    public void addTexture(Rectangle2D texture)
    {
        if(unlocked)
            this.textures.add(texture);
    }

    public void setTextureType()
    {
        if(unlocked)
            this.textureType = new ArrayList<>();
    }

    public void addTextureType(int textureType)
    {
        if(unlocked)
            this.textureType.add(textureType);
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

    public ArrayList<Point> getTeleportTo()
    {
        return (ArrayList<Point>) this.teleportTo.clone();
    }

    public ArrayList<Rectangle2D> getTextures()
    {
        return (ArrayList<Rectangle2D>) this.textures.clone();
    }

    public ArrayList<Integer> getTextureType()
    {
        return (ArrayList<Integer>) this.textureType.clone();
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
