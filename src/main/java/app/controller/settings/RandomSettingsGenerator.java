package app.controller.settings;

import app.controller.linAlg.Intersection;
import app.controller.linAlg.Line;
import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Random;

public abstract class RandomSettingsGenerator
{
    private static final boolean RANDOM_NUM = true;
    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;
    private static final int SPAWN_HEIGHT = 20;
    private static final int SPAWN_WIDTH = 20;
    private static final int DELTA = 200; // Distance between spawn areas.
    private static final int MIN_OBSTACLES = 5;
    private static final int MAX_OBSTACLES = 50;
    private static final int NUM_SHAPES = 5;
    private static final int MIN_SIDE_LENGTH = 30;
    private static final int MAX_SIDE_LENGTH = 70;
    private static final ArrayList<Rectangle2D> uShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> lShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> tShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> iShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> plusShape = new ArrayList<>();
    private static final ArrayList<Rectangle2D> obstacles = new ArrayList<>();
    @Setter private static Line lineOfClarity1;
    @Setter private static Line lineOfClarity2;

    public static Settings generateRandomSettings()
    {
        clearRandomGenerator();
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

        lineOfClarity1 = new Line(new Vector(guardSpawn.getMaxX(), guardSpawn.getMinY()),
                                new Vector(intruderSpawn.getMinX(), intruderSpawn.getMaxY()));
        lineOfClarity2 = new Line(new Vector(guardSpawn.getMinX(), guardSpawn.getMaxY()),
                                new Vector(intruderSpawn.getMaxX(), intruderSpawn.getMinY()));

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
        int numOfObstacles;
        if(RANDOM_NUM)
        {
            numOfObstacles = rand.nextInt(MAX_OBSTACLES) + MIN_OBSTACLES;
        }
        else
        {
            numOfObstacles = MAX_OBSTACLES;
        }

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
        int ranX = rand.nextInt(WIDTH - (MIN_SIDE_LENGTH + MAX_SIDE_LENGTH));
        int ranY = rand.nextInt(HEIGHT - (MIN_SIDE_LENGTH + MAX_SIDE_LENGTH));
        Rectangle2D rect = new Rectangle2D(ranX, ranY, scalar, scalar);

        if(!checkOverlaps(rect) && !checkBlocking(rect))
        {
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
    }

    private static boolean checkOverlaps(Rectangle2D r)
    {
        for(Rectangle2D obstacle : obstacles)
        {
            if(checkOverlap(obstacle, r) || checkWithin(obstacle, r))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean checkOverlap(Rectangle2D r1, Rectangle2D r2)
    {
        Line north1 = new Line( new Vector(r1.getMinX(), r1.getMinY()),
                                new Vector(r1.getMaxX(), r1.getMinY()));
        Line east1 = new Line(  new Vector(r1.getMaxX(), r1.getMinY()),
                                new Vector(r1.getMaxX(), r1.getMaxY()));
        Line south1 = new Line( new Vector(r1.getMinX(), r1.getMaxY()),
                                new Vector(r1.getMaxX(), r1.getMaxY()));
        Line west1 = new Line(  new Vector(r1.getMinX(), r1.getMinY()),
                                new Vector(r1.getMinX(), r1.getMaxY()));

        Line north2 = new Line( new Vector(r2.getMinX(), r2.getMinY()),
                                new Vector(r2.getMaxX(), r2.getMinY()));
        Line east2 = new Line(  new Vector(r2.getMaxX(), r2.getMinY()),
                                new Vector(r2.getMaxX(), r2.getMaxY()));
        Line south2 = new Line( new Vector(r2.getMinX(), r2.getMaxY()),
                                new Vector(r2.getMaxX(), r2.getMaxY()));
        Line west2 = new Line(  new Vector(r2.getMinX(), r2.getMinY()),
                                new Vector(r2.getMinX(), r2.getMaxY()));

        return (Intersection.hasIntersection(north1, north2) ||
                Intersection.hasIntersection(north1, east2) ||
                Intersection.hasIntersection(north1, south2) ||
                Intersection.hasIntersection(north1, west2) ||
                Intersection.hasIntersection(east1, north2) ||
                Intersection.hasIntersection(east1, east2) ||
                Intersection.hasIntersection(east1, south2) ||
                Intersection.hasIntersection(east1, west2) ||
                Intersection.hasIntersection(south1, north2) ||
                Intersection.hasIntersection(south1, east2) ||
                Intersection.hasIntersection(south1, south2) ||
                Intersection.hasIntersection(south1, west2) ||
                Intersection.hasIntersection(west1, north2) ||
                Intersection.hasIntersection(west1, east2) ||
                Intersection.hasIntersection(west1, south2) ||
                Intersection.hasIntersection(west1, west2));
    }

    public static boolean checkWithin(Rectangle2D r1, Rectangle2D r2)
    {
        // Check if rectangle 1 is within 2
        if( r1.getMinY() > r2.getMinY() && r1.getMinX() > r2.getMinX() &&
            r1.getMaxY() < r2.getMaxY() && r1.getMaxX() < r2.getMaxX())
        {
            return true;
        }
        // Check if rectangle 2 is within 1
        if( r2.getMinY() > r1.getMinY() && r2.getMinX() > r1.getMinX() &&
            r2.getMaxY() < r1.getMaxY() && r2.getMaxX() < r1.getMaxX())
        {
            return true;
        }
        // Neither rectangle is within the other.
        return false;
    }

    public static boolean checkBlocking(Rectangle2D r)
    {
        Line north = new Line( new Vector(r.getMinX(), r.getMinY()),
                new Vector(r.getMaxX(), r.getMinY()));
        Line east = new Line(  new Vector(r.getMaxX(), r.getMinY()),
                new Vector(r.getMaxX(), r.getMaxY()));
        Line south = new Line( new Vector(r.getMinX(), r.getMaxY()),
                new Vector(r.getMaxX(), r.getMaxY()));
        Line west = new Line(  new Vector(r.getMinX(), r.getMinY()),
                new Vector(r.getMinX(), r.getMaxY()));

        return (Intersection.hasIntersection(lineOfClarity1, north) ||
                Intersection.hasIntersection(lineOfClarity1, east) ||
                Intersection.hasIntersection(lineOfClarity1, south) ||
                Intersection.hasIntersection(lineOfClarity1, west) ||
                Intersection.hasIntersection(lineOfClarity2, north) ||
                Intersection.hasIntersection(lineOfClarity2, east) ||
                Intersection.hasIntersection(lineOfClarity2, south) ||
                Intersection.hasIntersection(lineOfClarity2, west));
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

    public static void clearRandomGenerator()
    {
        obstacles.clear();
        lShape.clear();
        uShape.clear();
        iShape.clear();
        tShape.clear();
        plusShape.clear();
    }
}
