package app.model;

import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.controller.settings.SettingsObject;
import app.controller.soundEngine.SoundFurniture;
import app.controller.soundEngine.SoundSource;
import app.model.agents.Agent;
import app.model.agents.Human;
import app.model.agents.WallFollowAgent;
import app.model.boundary.Boundary;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureFactory;
import app.view.simulation.Info;
import java.util.ArrayList;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

public class Map
{
    @Getter private ArrayList<Furniture> furniture;
    @Getter private ArrayList<SoundFurniture> soundFurniture;
    @Getter private ArrayList<Agent> agents;
    @Getter private ArrayList<SoundSource> soundSources;
    @Getter private Settings settings;
    private Rectangle2D guardSpawn;
    private Rectangle2D intruderSpawn;
    private Human human;

    public Map(Settings settings)
    {
        System.out.print("Loading settings ... ");
        this.settings = settings;

        /* Make furniture */
        furniture = new ArrayList<>();
        settings.getFurniture().forEach(e -> addFurniture(e));

        /* Make sound furniture */
        soundFurniture = new ArrayList<>();

        /* Make some sound sources */
        soundSources = settings.getSoundSources();

        agents = new ArrayList<>();

        /* Adds temporary WallFollowAgent */
        Vector srt = new Vector(randX(intruderSpawn), randY(intruderSpawn));
        Vector dir = new Vector(-1,0);
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

    /**
     * Only for testing, delete later
     */
    public Map(Agent agent, ArrayList<Furniture> obstacles)
    {
        agents = new ArrayList<>();
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

    public void addFurniture(SettingsObject obj)
    {
        switch (obj.getType())
        {
            case GUARD_SPAWN -> guardSpawn = obj.getRect();
            case INTRUDER_SPAWN -> intruderSpawn = obj.getRect();
            default -> this.furniture.add(FurnitureFactory.make(obj.getType(), obj.getRect()));
        }
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
        gc.strokeRect(guardSpawn.getMinX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                      guardSpawn.getMinY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                      guardSpawn.getHeight() * Info.getInfo().zoom,
                      guardSpawn.getHeight() * Info.getInfo().zoom);
    }

    public void drawIntruderSpawn(GraphicsContext gc)
    {
        gc.setStroke(Color.RED);
        gc.strokeRect(intruderSpawn.getMinX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                      intruderSpawn.getMinY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                      intruderSpawn.getHeight() * Info.getInfo().zoom,
                      intruderSpawn.getHeight() * Info.getInfo().zoom);
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
