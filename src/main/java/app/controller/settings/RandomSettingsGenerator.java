package app.controller.settings;

import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.Random;

public abstract class RandomSettingsGenerator
{
    /**
     * Method for base settings always required.
     *  - 1 guard, 1 intruder.
     *  - Height + Width. (600 x 800) default.
     *  - Spawn area locations. 20 x 20 with a distance 'delta' between them.
     *  - Target area behind outer boundaries.
     *
     * Calculate the area where obstacles cannot be placed.
     *
     * Template shapes: U, L, T, I, + etc.
     *
     * Generate the random points on the map where these objects can be placed. Create the random boxes from the point
     * as top left with random height + width.
     *
     * Check for overlaps between objects.
     */

    // TODO 1: Random with and height of boxes. CHECK
    //      2: Random orientation of shapes within the boxes. CHECK
    //      3: Check no overlapping shapes with the specified margin.
    //      4: Check no shape in the corridor of clarity.

    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;
    private static final int SPAWN_HEIGHT = 20;
    private static final int SPAWN_WIDTH = 20;
    private static final int DELTA = 200; // Distance between spawn areas.
    private static final int MIN_OBSTACLES = 5;
    private static final int MAX_OBSTACLES = 2;
    private static final int NUM_SHAPES = 5;
    private static final int MIN_SIDE_LENGTH = 30;
    private static final int MAX_SIDE_LENGTH = 70;
    private static final ArrayList<Rectangle2D> uShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> lShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> tShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> iShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> plusShape = new ArrayList<>();

    public static Settings generateRandomSettings()
    {
        initialiseTemplates();

        Settings randomSettings = createBaseSettings();
        createRandomObstacles(randomSettings);
        //testRandom(randomSettings);

        return randomSettings;
    }

    private static void testRandom(Settings s)
    {
        ArrayList<Rectangle2D> uWalls = scaleU(70);
        ArrayList<Rectangle2D> rotated = rotateShape(uWalls, 90);
        for(Rectangle2D wall : rotated)
        {
            s.addFurniture(new Rectangle2D(wall.getMinX() + 600, wall.getMinY() + 450,
                    wall.getWidth(), wall.getHeight()), FurnitureType.WALL);
        }
    }

    private static void initialiseTemplates()
    {
        // U
        uShape.add(new Rectangle2D(0, 0, 10, 30)); // left
        uShape.add(new Rectangle2D(20, 0, 10, 30)); // right
        uShape.add(new Rectangle2D(0, 20, 30, 10)); // bottom

        // L
        lShape.add(new Rectangle2D(0, 20, 30, 10)); // bottom
        lShape.add(new Rectangle2D(0, 0, 10, 30)); // left

        // T
        tShape.add(new Rectangle2D(10, 0, 10, 30)); // middle
        tShape.add(new Rectangle2D(0, 0, 30, 10)); // top

        // I
        iShape.add(new Rectangle2D(10, 0, 10, 30)); // middle

        // +
        plusShape.add(new Rectangle2D(0, 10, 30, 10)); // middle horizontal
        plusShape.add(new Rectangle2D(10, 0, 10, 30)); // middle vertical
    }

    private static Settings createBaseSettings()
    {
        Settings basic = new Settings();
        basic.setName("RandomMap_1");
        basic.setGameMode(-1);
        basic.setWidth(WIDTH);
        basic.setHeight(HEIGHT);
        basic.setNoOfGuards(1);
        basic.setNoOfIntruders(1);
        basic.setWalkSpeedGuard(20);
        basic.setWalkSpeedIntruder(20);
        basic.setSprintSpeedGuard(30);
        basic.setSprintSpeedIntruder(30);

        double percentOfDelta = findPercentageOfLine();
        double percentOfWidth = WIDTH*(percentOfDelta/2);
        double percentOfHeight = HEIGHT*(percentOfDelta/2);

        Rectangle2D target = new Rectangle2D(-100, -100, 1, 1);
        basic.addFurniture(target, FurnitureType.TARGET);

        Rectangle2D guardSpawn = new Rectangle2D(percentOfWidth, percentOfHeight,
                                                 SPAWN_WIDTH, SPAWN_HEIGHT);
        basic.addFurniture(guardSpawn, FurnitureType.GUARD_SPAWN);

        Rectangle2D intruderSpawn = new Rectangle2D(WIDTH - (percentOfWidth),
                                                    HEIGHT - (percentOfHeight),
                                                    SPAWN_WIDTH, SPAWN_HEIGHT);
        basic.addFurniture(intruderSpawn, FurnitureType.INTRUDER_SPAWN);

        return basic;
    }

    private static double findPercentageOfLine()
    {
        Vector start = new Vector(0, 0);
        Vector end = new Vector(WIDTH, HEIGHT);
        double dist = start.dist(end);
        return 1 - (DELTA/dist);
    }

    private static void createRandomObstacles(Settings s)
    {
        Random rand = new Random();
        int numOfObstacles = rand.nextInt(MAX_OBSTACLES) + MIN_OBSTACLES;

        for(int i = 0; i < numOfObstacles; i++)
        {
            int shapeType = rand.nextInt(NUM_SHAPES);
            int ranScalar = rand.nextInt(MAX_SIDE_LENGTH) + MIN_SIDE_LENGTH;

            switch(shapeType)
            {
                case 0 -> {
                    ArrayList<Rectangle2D> uWalls = scaleU(ranScalar);
                    addWalls(s, uWalls);
                }
                case 1 -> {
                    ArrayList<Rectangle2D> lWalls = scaleL(ranScalar);
                    addWalls(s, lWalls);
                }
                case 2 -> {
                    ArrayList<Rectangle2D> tWalls = scaleT(ranScalar);
                    addWalls(s, tWalls);
                }
                case 3 -> {
                    ArrayList<Rectangle2D> iWalls = scaleI(ranScalar);
                    addWalls(s, iWalls);
                }
                case 4 -> {
                    ArrayList<Rectangle2D> plusWalls = scalePlus(ranScalar);
                    addWalls(s, plusWalls);
                }
            }
        }
    }

    private static ArrayList<Rectangle2D> scaleU(int scalar)
    {
        ArrayList<Rectangle2D> walls = new ArrayList<>();
        for(Rectangle2D wall : uShape)
        {
            Rectangle2D rect;
            if(wall.getWidth() == 10)
            {
                if(wall.getMinX() == 0)
                    rect = new Rectangle2D(wall.getMinX(), wall.getMinY(), 10, scalar);
                else
                    rect = new Rectangle2D(scalar-10, wall.getMinY(), 10, scalar);
            }
            else
            {
                rect = new Rectangle2D(wall.getMinX(), scalar-10, scalar, 10);
            }
            walls.add(rect);
        }
        return walls;
    }

    private static ArrayList<Rectangle2D> scaleL(int scalar)
    {
        ArrayList<Rectangle2D> walls = new ArrayList<>();
        for(Rectangle2D wall : lShape)
        {
            Rectangle2D rect;
            if(wall.getWidth() == 10)
                rect = new Rectangle2D(wall.getMinX(), wall.getMinY(), 10, scalar);
            else
                rect = new Rectangle2D(wall.getMinX(), scalar-10, scalar, 10);
            walls.add(rect);
        }
        return walls;
    }

    private static ArrayList<Rectangle2D> scaleT(int scalar)
    {
        ArrayList<Rectangle2D> walls = new ArrayList<>();
        for(Rectangle2D wall : tShape)
        {
            Rectangle2D rect;
            if(wall.getWidth() == 10)
            {
                int minX = (scalar/2) - 5;
                rect = new Rectangle2D(minX, wall.getMinY(), 10, scalar);
            }
            else
                rect = new Rectangle2D(wall.getMinX(), wall.getMinY(), scalar, 10);
            walls.add(rect);
        }
        return walls;
    }

    private static ArrayList<Rectangle2D> scaleI(int scalar)
    {
        ArrayList<Rectangle2D> walls = new ArrayList<>();
        int minX = (scalar/2) - 5;
        Rectangle2D rect = new Rectangle2D(minX, iShape.get(0).getMinY(), 10, scalar);
        walls.add(rect);
        return walls;
    }

    private static ArrayList<Rectangle2D> scalePlus(int scalar)
    {
        ArrayList<Rectangle2D> walls = new ArrayList<>();
        for(Rectangle2D wall : plusShape)
        {
            Rectangle2D rect;
            int scalarHalf = (scalar/2) - 5;
            if(wall.getWidth() == 10)
                rect = new Rectangle2D(scalarHalf, wall.getMinY(), 10, scalar);
            else
                rect = new Rectangle2D(wall.getMinX(), scalarHalf, scalar, 10);
            walls.add(rect);
        }
        return walls;
    }

    private static void addWalls(Settings s, ArrayList<Rectangle2D> walls)
    {
        Random rand = new Random();
        int ranX = rand.nextInt(WIDTH - (MIN_SIDE_LENGTH + MAX_SIDE_LENGTH)*2) +
                                (MIN_SIDE_LENGTH + MAX_SIDE_LENGTH);
        int ranY = rand.nextInt(HEIGHT - (MIN_SIDE_LENGTH + MAX_SIDE_LENGTH)*2) +
                                (MIN_SIDE_LENGTH + MAX_SIDE_LENGTH);


        ArrayList<Rectangle2D> newWalls;
        double ran = rand.nextDouble();
        if(ran < 0.25)
            newWalls = rotateShape(walls, 90);
        else if(ran < 0.5)
            newWalls = rotateShape(walls, 180);
        else if(ran < 0.75)
            newWalls = rotateShape(walls, 270);
        else
            newWalls = walls;

        for(Rectangle2D r : newWalls)
        {
            Rectangle2D wall = new Rectangle2D(r.getMinX() + ranX, r.getMinY() + ranY,
                    r.getWidth(), r.getHeight());
            s.addFurniture(wall, FurnitureType.WALL);
        }
    }

    private static ArrayList<Rectangle2D> rotateShape(ArrayList<Rectangle2D> walls, int d)
    {
        ArrayList<Rectangle2D> rotatedWalls = new ArrayList<>();
        for(Rectangle2D wall : walls)
        {
            Rectangle2D rect;
            if(d == 90)
                rect = new Rectangle2D(wall.getWidth()-wall.getMinY(), wall.getMinX(),
                                        wall.getHeight(), wall.getWidth());
            else if(d == 180)
                rect = new Rectangle2D(wall.getHeight()-wall.getMinX(),
                                    wall.getWidth()-wall.getMinY(),
                                         wall.getWidth(), wall.getHeight());
            else
                rect = new Rectangle2D(wall.getMinY(), wall.getHeight()-wall.getMinX(),
                                       wall.getHeight(), wall.getWidth());
            rotatedWalls.add(rect);
        }
        return rotatedWalls;
    }
}
