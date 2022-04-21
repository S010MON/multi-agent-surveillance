package app.model;

import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.controller.settings.Settings;
import app.controller.settings.SettingsObject;
import app.model.agents.*;
import app.model.agents.ACO.AcoAgent;
import app.model.agents.WallFollow.WallFollowAgent;
import app.model.boundary.Boundary;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureFactory;
import app.model.furniture.FurnitureType;
import app.model.soundFurniture.SoundFurniture;
import app.model.soundFurniture.SoundFurnitureFactory;
import app.model.soundSource.SoundSource;
import app.model.soundSource.SoundSourceFactory;
import app.model.soundSource.SoundSourceType;
import app.view.simulation.Info;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import java.util.ArrayList;
import java.util.Stack;

public class Map
{
    private final Boolean HUMAN_ACTIVE = true;
    @Getter private ArrayList<Furniture> furniture;
    @Getter private ArrayList<SoundFurniture> soundFurniture;
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
    private Stack<Agent> deletion;


    public Map(Settings settings)
    {
        System.out.print("Loading settings ... ");
        this.settings = settings;
        this.width = settings.getWidth();
        this.height = settings.getHeight();
        this.deletion = new Stack<>();

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
        guardsSeen = new VectorSet();
        intrudersSeen = new VectorSet();

        /* On creation add the right number of guards */
        for(int i = 0; i < settings.getNoOfGuards(); i++)
        {
            Vector srt = randPosition(guardSpawn);
            //Vector dir = randDirection();
            Vector dir = new Vector(1, 0);
            Agent guard = new AcoAgent(srt, dir, 10, Team.GUARD);
            guard.setMaxWalk(settings.getWalkSpeedGuard());
            guard.setMaxSprint(settings.getSprintSpeedGuard());
            agents.add(guard);
        }

        /* On creation add the right number of infiltrators */
        for(int i = 0; i < settings.getNoOfIntruders(); i++)
        {
            Vector srt = randPosition(intruderSpawn);
            Vector dir = randDirection();
            Agent intruder = new WallFollowAgent(srt, dir, 10, Team.INTRUDER);
            intruder.setMaxWalk(settings.getWalkSpeedIntruder());
            intruder.setMaxSprint(settings.getSprintSpeedIntruder());
            agents.add(intruder);
        }

        if (HUMAN_ACTIVE)
        {
            Vector humanStart = randPosition(intruderSpawn);
            human = new Human(humanStart, new Vector(1, 0), 10, Team.INTRUDER);
            human.setMaxWalk(settings.getWalkSpeedGuard());
            human.setMaxSprint(settings.getSprintSpeedGuard());
            agents.add(human);
        }

        this.coverage = new Coverage(this);
        System.out.print(" done");
    }

    public Map(Agent agent, ArrayList<Furniture> obstacles)
    {
        agents = new ArrayList<>();
        agents.add(agent);

        furniture = new ArrayList<>();
        furniture.addAll(obstacles);
    }

    public void addFurniture(SettingsObject obj)
    {
        switch(obj.getType())
        {
            case GUARD_SPAWN -> guardSpawn = obj.getRect();
            case INTRUDER_SPAWN -> intruderSpawn = obj.getRect();
            case TARGET -> target = obj.getRect();
            default -> this.furniture.add(FurnitureFactory.make(obj));
        }
    }

    public void addSoundFurniture(SettingsObject obj)
    {
        switch(obj.getType())
        {
            case GUARD_SPAWN -> guardSpawn = obj.getRect();
            case INTRUDER_SPAWN -> intruderSpawn = obj.getRect();
            default -> this.soundFurniture.add(SoundFurnitureFactory.make(obj));
        }
    }

    public void addSoundSource(SettingsObject obj)
    {
        this.soundSources.add(SoundSourceFactory.make(SoundSourceType.SIREN, Vector.from(obj.getRect()), obj.getAmplitude()));
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
        if(target != null)
        {
            gc.setStroke(Color.GOLD);
            gc.strokeRect(target.getMinX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                    target.getMinY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                    target.getHeight() * Info.getInfo().zoom,
                    target.getHeight() * Info.getInfo().zoom);
        }
        if(guardSpawn != null)
        {
            gc.setStroke(Color.BLUE);
            gc.strokeRect(guardSpawn.getMinX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                    guardSpawn.getMinY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                    guardSpawn.getHeight() * Info.getInfo().zoom,
                    guardSpawn.getHeight() * Info.getInfo().zoom);
        }
        if(intruderSpawn != null)
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

    public void checkForCapture(Agent currentAgent)
    {
        if(currentAgent.getTeam() != Team.GUARD)
            return;

        for(Agent otherAgent : agents)
        {
            if(otherAgent.getTeam() != currentAgent.getTeam())
            {
                double dist = currentAgent.getPosition().dist(otherAgent.getPosition());
                if(dist <= (currentAgent.getRadius() + otherAgent.getRadius() + 3))
                {
                    deleteAgent(otherAgent);
                }
            }
        }
    }

    public void deleteAgent(Agent agent)
    {
        deletion.push(agent);
    }

    public void garbageCollection()
    {
        for(Agent a: deletion)
        {
            agents.remove(a);
        }
    }

    private Vector randDirection()
    {
        double r = Math.random();
        if(r < 0.25)
            return new Vector(1, 0);
        else if(r < 0.5)
            return new Vector(0, 1);
        else if(r < 0.75)
            return new Vector(-1, 0);
        else
            return new Vector(0, -1);
    }

    private Vector randPosition(Rectangle2D r)
    {
        Vector v;
        do
        {
            double x = r.getMinX() + (Math.random() * (r.getMaxX() - r.getMinX()));
            double y = r.getMinY() + (Math.random() * (r.getMaxY() - r.getMinY()));
            v = new Vector(x, y);
        } while(!clearSpot(v));
        return v;
    }

    private boolean clearSpot(Vector v)
    {
        for(Agent agent: agents)
        {
            double dist = agent.getPosition().dist(v);
            if(dist < 2 * agent.getRadius())
                return false;
        }
        return true;
    }

    private Vector targetDirection(Vector vector)
    {
        Vector targetCentre = new Vector(target.getMinX() + (target.getWidth()/2),
                                        target.getMinY() + (target.getHeight()/2));
        double dX = targetCentre.getX() - vector.getX();
        double dY = targetCentre.getY() - vector.getY();
        Vector direction = new Vector(dX, dY);
        return direction.normalise();
    }
}
