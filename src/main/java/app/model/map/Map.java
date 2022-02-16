package app.model.map;

import app.controller.Settings;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.agents.Human;
import app.model.agents.WallFollowAgent;
import app.model.boundary.Boundary;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureFactory;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Map
{
    private ArrayList<Furniture> furniture;
    private ArrayList<Agent> agents;
    private Rectangle2D guardSpawn;
    private Rectangle2D intruderSpawn;
    private Human human;
    private Settings settings;

    public Map(Settings settings)
    {
        System.out.print("Loading settings ... ");
        this.settings = settings;
        this.guardSpawn = settings.getSpawnAreaGuards();
        this.intruderSpawn = settings.getSpawnAreaIntruders();

        /* Make furniture */
        furniture = new ArrayList<>();
        settings.getWalls().forEach(e -> addFurniture(FurnitureType.WALL, e));
        settings.getShade().forEach(e -> addFurniture(FurnitureType.SHADE, e));
        settings.getGlass().forEach(e -> addFurniture(FurnitureType.GLASS, e));

        agents = new ArrayList<>();

        /* Adds temporary WallFollowAgent */
        Vector srt = new Vector(randX(intruderSpawn), randY(intruderSpawn));
        Vector dir = new Vector(1,0);
        WallFollowAgent wallAgent = new WallFollowAgent(srt, dir, 10);
        //Assumes the wallFollowAgent is a guard
        wallAgent.setMaxWalk(settings.getWalkSpeedGuard());
        wallAgent.setMaxSprint(settings.getSprintSpeedGuard());
        agents.add(wallAgent);

        // On creation add the right number of guards
        for(int i = 0; i < settings.getNoOfGuards(); i++)
        {
            Vector guardStart = new Vector(randX(guardSpawn), randY(guardSpawn));
            /* TODO Add guard agents here!!! */
        }

        // On creation add the right number of infiltrators
        for(int i = 0; i < settings.getNoOfIntruders(); i++)
        {
            Vector intruderStart = new Vector(randX(intruderSpawn), randY(intruderSpawn));
            /* TODO intruder agents here!!! */
        }

        Vector humanStart = new Vector(randX(guardSpawn), randY(guardSpawn));
        human = new Human(humanStart, new Vector(1,0), 10);
        //Assumes the human is a guard
        human.setMaxWalk(settings.getWalkSpeedGuard());
        human.setMaxSprint(settings.getSprintSpeedGuard());
        agents.add(human);

        System.out.println("done.");
    }

    public Map(Agent agent, ArrayList<Furniture> obstacles)
    {
        agents = new ArrayList<>();
        agent.setMaxWalk(settings.getWalkSpeedGuard());
        agent.setMaxSprint(settings.getSprintSpeedGuard());
        agents.add(agent);

        furniture = new ArrayList<>();
        furniture.addAll(obstacles);
    }

    public void walk(Vector v)
    {
        human.walk(v);
    }

    public void sprint(Vector v)
    {
        human.sprint(v);
    }

    public Settings getSetting(){return settings;}

    public ArrayList<Agent> getAgents()
    {
        return agents;
    }

    public void addFurniture(FurnitureType type, Rectangle2D rectangle)
    {
        this.furniture.add(FurnitureFactory.make(type, rectangle));
    }

    public ArrayList<Furniture> getFurniture()
    {
        return furniture;
    }

    public ArrayList<Boundary> getBoundaries()
    {
        ArrayList<Boundary> boundaries = new ArrayList<>();
        furniture.forEach(e -> boundaries.addAll(e.getBoundaries()));
        return boundaries;
    }

    public void drawGuardSpawn(GraphicsContext gc)
    {
        gc.setStroke(Color.BLUE);
        gc.strokeRect(guardSpawn.getMinX(),
                      guardSpawn.getMinY(),
                      guardSpawn.getHeight(),
                      guardSpawn.getHeight());
    }

    public void drawIntruderSpawn(GraphicsContext gc)
    {
        gc.setStroke(Color.RED);
        gc.strokeRect(intruderSpawn.getMinX(),
                      intruderSpawn.getMinY(),
                      intruderSpawn.getHeight(),
                      intruderSpawn.getHeight());
    }

    private double randX(Rectangle2D r)
    {
        return r.getMinX() + (Math.random() * (r.getMaxX() - r.getMinX()));
    }

    private double randY(Rectangle2D r)
    {
        return r.getMinY() + (Math.random() * (r.getMaxY() - r.getMinY()));
    }
}
