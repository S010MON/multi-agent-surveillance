package app.model;

import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.controller.settings.SettingsObject;
import app.model.agents.ACO.AcoAgent360Vision;
import app.model.agents.*;
import app.model.agents.ACO.AcoAgentLimitedVision;
import app.model.boundary.Boundary;
import app.model.furniture.*;
import app.model.soundFurniture.SoundFurniture;
import app.model.soundSource.SoundSource;
import app.model.soundFurniture.SoundFurnitureFactory;
import app.model.soundSource.SoundSourceFactory;
import app.model.soundSource.SoundSourceType;
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
        Rectangle2D rect = new Rectangle2D(0, 0, settings.getWidth(), settings.getHeight());
        SettingsObject border = new SettingsObject(rect, FurnitureType.BORDER);
        addFurniture(border);
        settings.getFurniture().forEach(e -> addFurniture(e));

        /* Make sound furniture */
        soundFurniture = new ArrayList<>();
        settings.getSoundFurniture().forEach(e -> addSoundFurniture(e));

        /* Make some sound sources */
        soundSources = new ArrayList<>();
        settings.getSoundSources().forEach(e -> addSoundSource(e));

        agents = new ArrayList<>();

        // On creation add the right number of guards
        for(int i = 0; i < settings.getNoOfGuards(); i++)
        {
            Vector srt = randPosition(guardSpawn);
            Vector dir = new Vector(0, 1);
            AcoAgentLimitedVision guard = new AcoAgentLimitedVision(srt, dir, 10);

            guard.setMaxWalk(settings.getWalkSpeedGuard());
            guard.setMaxSprint(settings.getSprintSpeedGuard());
            agents.add(guard);
        }

        // On creation add the right number of infiltrators
        for(int i = 0; i < settings.getNoOfIntruders(); i++)
        {
            Vector srt = randPosition(intruderSpawn);
            Vector dir = randDirection();
            WallFollowAgent intruder = new WallFollowAgent(srt, dir, 10);
            intruder.setMaxWalk(settings.getWalkSpeedIntruder());
            intruder.setMaxSprint(settings.getSprintSpeedIntruder());
            agents.add(intruder);
        }

        Vector humanStart = randPosition(intruderSpawn);
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

    public void addSoundFurniture(SettingsObject obj)
    {
        switch (obj.getType())
        {
            case GUARD_SPAWN -> guardSpawn = obj.getRect();
            case INTRUDER_SPAWN -> intruderSpawn = obj.getRect();
            default -> this.soundFurniture.add(SoundFurnitureFactory.make(obj));
        }
    }

    public void addSoundSource(SettingsObject obj)
    {
        // TODO for now?
        this.soundSources.add(SoundSourceFactory.make(SoundSourceType.SIREN, Vector.from(obj.getRect()), obj.getAmplitude()));
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

    private Vector randPosition(Rectangle2D r)
    {
        Vector v;
        do {
            double x = r.getMinX() + (Math.random() * (r.getMaxX() - r.getMinX()));
            double y = r.getMinY() + (Math.random() * (r.getMaxY() - r.getMinY()));
            v = new Vector(x, y);
        } while (!clearSpot(v));

        return v;
    }

    private boolean clearSpot(Vector v)
    {
        for(Agent agent: agents)
        {
            double dist = agent.getPosition().dist(v);
            if(dist < 2*agent.getRadius())
                return false;
        }
        return true;
    }
}
