package app.controller;

import javafx.geometry.Rectangle2D;
import java.awt.Point;
import java.util.ArrayList;

// Orientation, which is double, only applies to teleporting and adding texture, not saved as a setting, can be if needed.
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
    private ArrayList<Rectangle2D> textures;
    private ArrayList<Integer> textureType;
    private ArrayList<Integer> textureOrientations;
    private Rectangle2D targetArea;
    private Rectangle2D spawnAreaIntruders;
    private Rectangle2D spawnAreaGuards;

    private boolean scalingOn = true;

    public Settings()
    {
        walls = new ArrayList<>();
        shade = new ArrayList<>();
        glass = new ArrayList<>();
        towers = new ArrayList<>();
        portals = new ArrayList<>();
        teleportTo = new ArrayList<>();
        teleportOrientations= new ArrayList<>();
        textures = new ArrayList<>();
        textureType = new ArrayList<>();
        textureOrientations= new ArrayList<>();
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

    public void addTextureOrientation(int textureOrientation)
    {
        if(unlocked)
            this.textureOrientations.add(textureOrientation);
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

    public void addTexture(Rectangle2D texture)
    {
        if(unlocked)
            this.textures.add(texture);
    }

    public void addTextureType(int textureType)
    {
        if(unlocked)
            this.textureType.add(textureType);
    }

    public void setWalls(ArrayList<Rectangle2D> newWalls)
    {
        if(unlocked)
            this.walls = newWalls;
    }

    public void setShade(ArrayList<Rectangle2D> newShade)
    {
        if(unlocked)
            this.shade = newShade;
    }

    public void setGlass(ArrayList<Rectangle2D> newGlass)
    {
        if(unlocked)
            this.glass = newGlass;
    }

    public void setTowers(ArrayList<Rectangle2D> newTowers)
    {
        if(unlocked)
            this.towers = newTowers;
    }

    public void setPortals(ArrayList<Rectangle2D> newPortals)
    {
        if(unlocked)
            this.portals = newPortals;
    }

    public void setTeleportTo(ArrayList<Point> newTeleportTo)
    {
        if(unlocked)
            this.teleportTo = newTeleportTo;
    }

    public void setTextures(ArrayList<Rectangle2D> newTextures)
    {
        if(unlocked)
            this.textures = newTextures;
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

    public void setScalingOn(boolean scale)
    {
        this.scalingOn = scale;
    }

    public void lock()
    {
        this.unlocked = false;
    }

    public String getName()
    {
        return this.name;
    }

    public int getGamemode()
    {
        return this.gamemode;
    }

    public int getHeight()
    {
        if(scalingOn)
            return scaleInt(this.height);
        return this.height;
    }

    public int getWidth()
    {
        if(scalingOn)
            return scaleInt(this.width);
        return this.width;
    }

    public int getNoOfGuards()
    {
        return this.noOfGuards;
    }

    public int getNoOfIntruders() { return this.noOfIntruders; }

    public double getWalkSpeedGuard()
    {
        if(scalingOn)
            return this.walkSpeedGuard*scaling;
        return this.walkSpeedGuard;
    }

    public double getSprintSpeedGuard()
    {
        if(scalingOn)
            return this.sprintSpeedGuard*scaling;
        return this.sprintSpeedGuard;
    }

    public double getWalkSpeedIntruder()
    {
        if(scalingOn)
            return this.walkSpeedIntruder*scaling;
        return this.walkSpeedIntruder;
    }

    public double getSprintSpeedIntruder()
    {
        if(scalingOn)
            return this.sprintSpeedIntruder*scaling;
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

    public ArrayList<Double> getTeleportOrientations()
    {
        return this.teleportOrientations;
    }

    public ArrayList<Integer> getTextureOrientations()
    {
        return this.textureOrientations;
    }

    public ArrayList<Rectangle2D> getWalls()
    {
        if(scalingOn)
            return scaleRecList(this.walls);
        return (ArrayList<Rectangle2D>) this.walls.clone();
    }

    public ArrayList<Rectangle2D> getShade()
    {
        if(scalingOn)
            return scaleRecList(this.shade);
        return (ArrayList<Rectangle2D>) this.shade.clone();
    }

    public ArrayList<Rectangle2D> getGlass()
    {
        if(scalingOn)
            return scaleRecList(this.glass);
        return (ArrayList<Rectangle2D>) this.glass.clone();
    }

    public ArrayList<Rectangle2D> getTowers()
    {
        if(scalingOn)
            return scaleRecList(this.towers);
        return (ArrayList<Rectangle2D>) this.towers.clone();
    }

    public ArrayList<Rectangle2D> getPortals()
    {
        if(scalingOn)
            return scaleRecList(this.portals);
        return (ArrayList<Rectangle2D>) this.portals.clone();
    }

    public ArrayList<Point> getTeleportTo()
    {
        if(scalingOn)
            return scalePointList(this.teleportTo);
        return (ArrayList<Point>) this.teleportTo.clone();
    }

    public ArrayList<Rectangle2D> getTextures()
    {
        if(scalingOn)
            return scaleRecList(this.textures);
        return (ArrayList<Rectangle2D>) this.textures.clone();
    }

    public ArrayList<Integer> getTextureType()
    {
        return (ArrayList<Integer>) this.textureType.clone();
    }

    public Rectangle2D getTargetArea()
    {
        if(scalingOn)
            return scaleRec(this.targetArea);
        return this.targetArea;
    }

    public Rectangle2D getSpawnAreaIntruders()
    {
        if(scalingOn)
            return scaleRec(this.spawnAreaIntruders);
        return this.spawnAreaIntruders;
    }

    public Rectangle2D getSpawnAreaGuards()
    {
        if(scalingOn)
            return scaleRec(this.spawnAreaGuards);
        return this.spawnAreaGuards;
    }

    private int scaleInt(int x)
    {
        return (int)(x*scaling);
    }

    private double scaleDouble(double x) { return x*scaling; }

    private Rectangle2D scaleRec(Rectangle2D rec)
    {
        return new Rectangle2D(rec.getMinX() * scaling, rec.getMinY() * scaling, rec.getWidth() * scaling, rec.getHeight() * scaling);
    }

    private ArrayList<Rectangle2D> scaleRecList(ArrayList<Rectangle2D> recs)
    {
        ArrayList<Rectangle2D> newRecs = new ArrayList<>();
        for (Rectangle2D rec : recs) {
            Rectangle2D newRec = scaleRec(rec);
            newRecs.add(newRec);
        }
        return newRecs;
    }

    private ArrayList<Point> scalePointList(ArrayList<Point> points) {
        ArrayList<Point> newPoints = new ArrayList<>();
        for (Point p : points) {
            Point newP = new Point((int)(p.getX() * scaling), (int)(p.getY() * scaling));
            newPoints.add(newP);
        }
        return newPoints;
    }
}
