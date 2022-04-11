package app.model;

import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.controller.settings.Settings;
import app.controller.settings.SettingsObject;
import app.model.agents.*;
import app.model.agents.ACO.AcoAgentLimitedVision;
import app.model.agents.WallFollow.WallFollowAgent;
import app.model.boundary.Boundary;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureFactory;
import app.model.furniture.FurnitureType;
import app.model.soundSource.SoundSource;
import app.model.soundSource.SoundSourceBase;
import app.model.soundSource.SoundSourceFactory;
import app.model.soundSource.SoundSourceType;
import app.view.simulation.Info;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.ArrayList;

public class Map
{
    private final Boolean HUMAN_ACTIVE = true;
    @Getter private ArrayList<Furniture> furniture;
    @Getter private ArrayList<Agent> agents;
    @Getter private ArrayList<SoundSource> soundSources;
    @Getter private VectorSet guardsSeen;
    @Getter private VectorSet intrudersSeen;
    @Getter private Settings settings;
    @Getter private double width;
    @Getter private double height;
    @Getter private Human human;
    private Coverage coverage;
    private Rectangle2D guardSpawn;
    private Rectangle2D intruderSpawn;
    private Rectangle2D target;


    public Map(Settings settings)
    {
        System.out.print("Loading settings ... ");
        this.settings = settings;
        this.width = settings.getWidth();
        this.height = settings.getHeight();

        /* Make furniture */
        furniture = new ArrayList<>();
        Rectangle2D rect = new Rectangle2D(0, 0, settings.getWidth(), settings.getHeight());
        SettingsObject border = new SettingsObject(rect, FurnitureType.BORDER);
        addFurniture(border);
        settings.getFurniture().forEach(e -> addFurniture(e));

        /* Make a test sound source */
        soundSources = new ArrayList<>();
        soundSources.add(new SoundSourceBase(new Vector(900, 500)));

        agents = new ArrayList<>();
        guardsSeen = new VectorSet();
        intrudersSeen = new VectorSet();

        // On creation add the right number of guards
        for(int i = 0; i < settings.getNoOfGuards(); i++)
        {
            Vector srt = randPosition(guardSpawn);
            if (srt != null)
            {
                Vector dir = randDirection();
                AcoAgentLimitedVision guard = new AcoAgentLimitedVision(srt, dir, 10, Team.GUARD);
                guard.setMaxWalk(settings.getWalkSpeedGuard());
                guard.setMaxSprint(settings.getSprintSpeedGuard());
                agents.add(guard);
            }
        }

        // On creation add the right number of infiltrators
        for(int i = 0; i < settings.getNoOfIntruders(); i++)
        {
            Vector srt = randPosition(intruderSpawn);
            if (srt != null)
            {
                Vector dir = randDirection();
                Agent intruder = new WallFollowAgent(srt, dir, 10, Team.INTRUDER);
                intruder.setMaxWalk(settings.getWalkSpeedIntruder());
                intruder.setMaxSprint(settings.getSprintSpeedIntruder());
                agents.add(intruder);
            } else
            {
                i = settings.getNoOfIntruders();
            }
        }

        if (HUMAN_ACTIVE && intruderSpawn != null)
        {
            Vector humanStart = randPosition(intruderSpawn);
            if (humanStart != null)
            {
                human = new Human(humanStart, new Vector(1, 0), 10, Team.INTRUDER);
                //Assumes the human is a guard
                human.setMaxWalk(settings.getWalkSpeedGuard());
                human.setMaxSprint(settings.getSprintSpeedGuard());
                agents.add(human);
            }
        }

        this.coverage = new Coverage(this);
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

    public void addFurniture(SettingsObject obj)
    {
        switch (obj.getType())
        {
            case GUARD_SPAWN -> guardSpawn = obj.getRect();
            case INTRUDER_SPAWN -> intruderSpawn = obj.getRect();
            case TARGET -> target = obj.getRect();
            default -> this.furniture.add(FurnitureFactory.make(obj));
        }
    }

    public void updateAllSeen(Agent agent)
    {
        if(agent.getTeam() == Team.GUARD)
            guardsSeen.addAll(agent.getSeen());
        else
            intrudersSeen.addAll(agent.getSeen());
    }

    public ArrayList<Boundary> getBoundaries()
    {
        ArrayList<Boundary> boundaries = new ArrayList<>();
        furniture.forEach(e -> boundaries.addAll(e.getBoundaries()));
        return boundaries;
    }

    public void drawIndicatorBoxes(GraphicsContext gc)
    {
        if (target != null)
        {
            gc.setStroke(Color.GOLD);
            gc.strokeRect(target.getMinX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                    target.getMinY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                    target.getHeight() * Info.getInfo().zoom,
                    target.getHeight() * Info.getInfo().zoom);
        }
        if (guardSpawn != null)
        {
            gc.setStroke(Color.BLUE);
            gc.strokeRect(guardSpawn.getMinX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                    guardSpawn.getMinY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                    guardSpawn.getHeight() * Info.getInfo().zoom,
                    guardSpawn.getHeight() * Info.getInfo().zoom);
        }
        if (intruderSpawn != null)
        {
            gc.setStroke(Color.RED);
            gc.strokeRect(intruderSpawn.getMinX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                    intruderSpawn.getMinY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                    intruderSpawn.getHeight() * Info.getInfo().zoom,
                    intruderSpawn.getHeight() * Info.getInfo().zoom);
        }
    }

    public double percentageComplete(Team team)
    {
        if(team == Team.GUARD)
            return coverage.percentSeen(guardsSeen);
        else
            return coverage.percentSeen(intrudersSeen);
    }

    private Vector randDirection()
    {
        double r = Math.random();
        if (r < 0.25)
            return new Vector(1, 0);
        else if (r < 0.5)
            return new Vector(0, 1);
        else if (r < 0.75)
            return new Vector(-1, 0);
        else
            return new Vector(0, -1);
    }

    private Vector randPosition(Rectangle2D r)
    {
        Vector v;
        int tries = 0;
        try
        {
            do
            {
                tries++;
                double x = r.getMinX() + (Math.random() * (r.getMaxX() - r.getMinX()));
                double y = r.getMinY() + (Math.random() * (r.getMaxY() - r.getMinY()));
                v = new Vector(x, y);
                if (tries > 500)
                {
                    throw new RuntimeException("SpawnArea not big enough for number of agents");
                }
            } while (!clearSpot(v));

            return v;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
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
