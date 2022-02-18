package app.controller;

import javafx.geometry.Rectangle2D;
import lombok.Getter;

import java.awt.Point;
import java.util.ArrayList;

// Orientation, which is double, only applies to teleporting and adding texture, not saved as a setting, can be if needed.
@Getter
public class Settings
{
    private boolean unlocked = true;
    private String name;
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
    private ArrayList<Rectangle2D> glass;
    private ArrayList<Rectangle2D> towers;
    private ArrayList<Rectangle2D> portals;
    private ArrayList<Point> teleportTo;
    private ArrayList<Double> teleportOrientations;
    private Rectangle2D targetArea;
    private Rectangle2D spawnAreaIntruders;
    private Rectangle2D spawnAreaGuards;

    public Settings()
    {
        walls = new ArrayList<>();
        shade = new ArrayList<>();
        glass = new ArrayList<>();
        towers = new ArrayList<>();
        portals = new ArrayList<>();
        teleportTo = new ArrayList<>();
        teleportOrientations= new ArrayList<>();
    }

    public void setName(String name)
    {
        if(unlocked)
            this.name=name;
    }

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

    public void addTeleportOrientation(double teleportOrientation)
    {
        if(unlocked)
            this.teleportOrientations.add(teleportOrientation);
    }

    public void addWall(Rectangle2D wall)
    {
        if(unlocked)
            this.walls.add(wall);
    }

    public void addShade(Rectangle2D shade)
    {
        if(unlocked)
            this.shade.add(shade);
    }

    public void addGlass(Rectangle2D glass)
    {
        if(unlocked)
            this.glass.add(glass);
    }

    public void addTower(Rectangle2D tower)
    {
        if(unlocked)
            this.towers.add(tower);
    }

    public void addPortal(Rectangle2D portal)
    {
        if(unlocked)
            this.portals.add(portal);
    }

    public void addTeleportTo(Point p)
    {
        if(unlocked)
            this.teleportTo.add(p);
    }

    public void setTargetArea(Rectangle2D targetArea)
    {
        if(unlocked)
            this.targetArea = targetArea;
    }

    public void setSpawnAreaIntruders(Rectangle2D spawnAreaIntruders)
    {
        if(unlocked)
            this.spawnAreaIntruders = spawnAreaIntruders;
    }

    public void setSpawnAreaGuards(Rectangle2D spawnAreaGuards)
    {
        if(unlocked)
            this.spawnAreaGuards = spawnAreaGuards;
    }

    public void lock()
    {
        this.unlocked = false;
    }
}
