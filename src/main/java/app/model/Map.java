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

        // On creation add the right number of guards
        for(int i = 0; i < settings.getNoOfGuards(); i++)
        {
            Vector srt = new Vector(randX(guardSpawn), randY(guardSpawn));
            Vector dir = randDirection();
            WallFollowAgent guard = new WallFollowAgent(srt, dir, 10);
            guard.setMaxWalk(settings.getWalkSpeedGuard());
            guard.setMaxSprint(settings.getSprintSpeedGuard());
            agents.add(guard);
        }

        // On creation add the right number of infiltrators
        for(int i = 0; i < settings.getNoOfIntruders(); i++)
        {
            Vector srt = new Vector(randX(intruderSpawn), randY(intruderSpawn));
            Vector dir = randDirection();
            WallFollowAgent intruder = new WallFollowAgent(srt, dir, 10);
            intruder.setMaxWalk(settings.getWalkSpeedIntruder());
            intruder.setMaxSprint(settings.getSprintSpeedIntruder());
            agents.add(intruder);
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
            default -> this.furniture.add(FurnitureFactory.make(obj));
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

    private Vector randDirection()
    {
        double r = Math.random();
        if(r < 0.25)
            return new Vector(1,0);
        else if( r < 0.5)
            return new Vector(0,1);
        else if( r < 0.75)
            return new Vector(-1,0);
        else
            return new Vector(0,-1);
    }
}
