package app.controller;

import java.awt.geom.Rectangle2D;

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
    private Rectangle2D[] walls;
    private Rectangle2D[] shade;
    private Rectangle2D[] towers;
    private Rectangle2D[] portals; // Test map contains an extra x, y pair for the location you get teleported to and a double for orientation
    private Rectangle2D[] textures; // Test map includes an int for texture type and a double for orientation
    private Rectangle2D.Double targetArea;
    private Rectangle2D.Double spawnAreaIntruders;
    private Rectangle2D.Double spawnAreaGuards;

    public Settings()
    {
        
    }
}
