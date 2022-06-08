package app.controller.settings;

import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.Random;

public abstract class RandomSettingsGenerator
{
    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;
    private static final int SPAWN_HEIGHT = 20;
    private static final int SPAWN_WIDTH = 20;
    private static final int DELTA = 200; // Distance between spawn areas.
    private static final int MIN_OBSTACLES = 5;
    private static final int MAX_OBSTACLES = 20;
    private static final int NUM_SHAPES = 5;
    private static final int MIN_SIDE_LENGTH = 30;
    private static final int MAX_SIDE_LENGTH = 70;
    private static final int MAX_TIME = 10;
    private static final ArrayList<Rectangle2D> uShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> lShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> tShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> iShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> plusShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> obstacles = new ArrayList<>();
    private static Vector lineOfClarity1Start;
    private static Vector lineOfClarity1End;
    private static Vector lineOfClarity2Start;
    private static Vector lineOfClarity2End;
    private static int time = 0;

    public static Settings generateRandomSettings()
    {
        initialiseTemplates();

        Settings randomSettings = createBaseSettings();
        createRandomObstacles(randomSettings);

        return randomSettings;
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
        obstacles.add(guardSpawn); // stops obstacles from generating over the spawn area.

        Rectangle2D intruderSpawn = new Rectangle2D(WIDTH - (percentOfWidth),
                                                    HEIGHT - (percentOfHeight),
                                                    SPAWN_WIDTH, SPAWN_HEIGHT);
        basic.addFurniture(intruderSpawn, FurnitureType.INTRUDER_SPAWN);
        obstacles.add(intruderSpawn);

        lineOfClarity1Start = new Vector(guardSpawn.getMaxX(), guardSpawn.getMinY());
        lineOfClarity1End = new Vector(intruderSpawn.getMinX(), intruderSpawn.getMaxY());
        lineOfClarity2Start = new Vector(guardSpawn.getMinX(), guardSpawn.getMaxY());
        lineOfClarity2End = new Vector(intruderSpawn.getMaxX(), intruderSpawn.getMinY());

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
                    addWalls(s, uWalls, ranScalar);
                }
                case 1 -> {
                    ArrayList<Rectangle2D> lWalls = scaleL(ranScalar);
                    addWalls(s, lWalls, ranScalar);
                }
                case 2 -> {
                    ArrayList<Rectangle2D> tWalls = scaleT(ranScalar);
                    addWalls(s, tWalls, ranScalar);
                }
                case 3 -> {
                    ArrayList<Rectangle2D> iWalls = scaleI(ranScalar);
                    addWalls(s, iWalls, ranScalar);
                }
                case 4 -> {
                    ArrayList<Rectangle2D> plusWalls = scalePlus(ranScalar);
                    addWalls(s, plusWalls, ranScalar);
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

    private static void addWalls(Settings s, ArrayList<Rectangle2D> walls, int scalar)
    {
        Random rand = new Random();
        int ranX;
        int ranY;
        Rectangle2D rect;
        do
        {
            ranX = rand.nextInt(WIDTH - (MIN_SIDE_LENGTH + MAX_SIDE_LENGTH));
            ranY = rand.nextInt(HEIGHT - (MIN_SIDE_LENGTH + MAX_SIDE_LENGTH));
            rect = new Rectangle2D(ranX, ranY, scalar, scalar);
            time++;
        }
        while(checkOverlaps(rect) || timeout() || checkBlocking(rect));

        time = 0;
        obstacles.add(rect);

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

    private static boolean checkOverlaps(Rectangle2D r)
    {
        for(Rectangle2D obstacle : obstacles)
        {
            if(checkOverlap(r, obstacle))
            {
                return true;
            }
        }
        return false;
    }

    private static boolean checkOverlap(Rectangle2D r1, Rectangle2D r2)
    {
        Vector topLeft1 = new Vector(r1.getMinX(), r1.getMinY());
        Vector topRight1 = new Vector(r1.getMaxX(), r1.getMinY());
        Vector bottomRight1 = new Vector(r1.getMaxX(), r1.getMaxY());
        Vector bottomLeft1 = new Vector(r1.getMinX(), r1.getMaxY());

        Vector topLeft2 = new Vector(r2.getMinX(), r2.getMinY());
        Vector topRight2 = new Vector(r2.getMaxX(), r2.getMinY());
        Vector bottomRight2 = new Vector(r2.getMaxX(), r2.getMaxY());
        Vector bottomLeft2 = new Vector(r2.getMinX(), r2.getMaxY());

        return (Intersection.hasIntersection(topLeft1, topRight1, topLeft2, topRight2) ||
                Intersection.hasIntersection(topLeft1, topRight1, topRight2, bottomRight2) ||
                Intersection.hasIntersection(topLeft1, topRight1, bottomLeft2, bottomRight2) ||
                Intersection.hasIntersection(topLeft1, topRight1, topLeft2, bottomLeft2) ||
                Intersection.hasIntersection(topRight1, bottomRight1, topLeft2, topRight2) ||
                Intersection.hasIntersection(topRight1, bottomRight1, topRight2, bottomRight2) ||
                Intersection.hasIntersection(topRight1, bottomRight1, bottomLeft2, bottomRight2) ||
                Intersection.hasIntersection(topRight1, bottomRight1, topLeft2, bottomLeft2) ||
                Intersection.hasIntersection(bottomLeft1, bottomRight1, topLeft2, topRight2) ||
                Intersection.hasIntersection(bottomLeft1, bottomRight1, topRight2, bottomRight2) ||
                Intersection.hasIntersection(bottomLeft1, bottomRight1, bottomLeft2,
                        bottomRight2) ||
                Intersection.hasIntersection(bottomLeft1, bottomRight1, topLeft2, bottomLeft2) ||
                Intersection.hasIntersection(topLeft1, bottomLeft1, topLeft2, topRight2) ||
                Intersection.hasIntersection(topLeft1, bottomLeft1, topRight2, bottomRight2) ||
                Intersection.hasIntersection(topLeft1, bottomLeft1, bottomLeft2, bottomRight2) ||
                Intersection.hasIntersection(topLeft1, bottomLeft1, topLeft2, bottomLeft2));
    }

    private static boolean checkBlocking(Rectangle2D r)
    {
        Vector topLeft = new Vector(r.getMinX(), r.getMinY());
        Vector topRight = new Vector(r.getMaxX(), r.getMinY());
        Vector bottomRight = new Vector(r.getMaxX(), r.getMaxY());
        Vector bottomLeft = new Vector(r.getMinX(), r.getMaxY());
        return (Intersection.hasIntersection(lineOfClarity1Start, lineOfClarity1End, topLeft,
                        topRight) ||
                Intersection.hasIntersection(lineOfClarity1Start, lineOfClarity1End, topRight,
                        bottomRight) ||
                Intersection.hasIntersection(lineOfClarity1Start, lineOfClarity1End, bottomLeft,
                        bottomRight) ||
                Intersection.hasIntersection(lineOfClarity1Start, lineOfClarity1End, topLeft,
                        bottomLeft) ||
                Intersection.hasIntersection(lineOfClarity2Start, lineOfClarity2End, topLeft,
                        topRight) ||
                Intersection.hasIntersection(lineOfClarity2Start, lineOfClarity2End, topRight,
                        bottomRight) ||
                Intersection.hasIntersection(lineOfClarity2Start, lineOfClarity2End, bottomLeft,
                        bottomRight) ||
                Intersection.hasIntersection(lineOfClarity2Start, lineOfClarity2End, topLeft,
                        bottomLeft));
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

    private static boolean timeout()
    {
        return time < MAX_TIME;
    }
}
