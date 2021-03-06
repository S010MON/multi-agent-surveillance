package app.model;

import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.controller.settings.Settings;
import app.controller.settings.SettingsObject;
import app.controller.soundEngine.SoundSource;
import app.model.agents.*;
import app.model.agents.Cells.GraphCell;
import app.model.boundary.Boundary;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureFactory;
import app.model.furniture.FurnitureType;
import app.view.simulation.Info;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Stack;

public class Map
{
    private Boolean humanActive = false;
    @Getter private ArrayList<Furniture> furniture;
    @Getter private ArrayList<Agent> agents;
    @Getter private ArrayList<SoundSource> soundSources;
    @Getter private PriorityQueue<Trail> trails;
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
        this.settings = settings;
        this.width = settings.getWidth();
        this.height = settings.getHeight();
        this.deletion = new Stack<>();
        this.trails = new PriorityQueue<>();

        /* Make furniture */
        furniture = new ArrayList<>();
        Rectangle2D rect = new Rectangle2D(0, 0, settings.getWidth(), settings.getHeight());
        SettingsObject border = new SettingsObject(rect, FurnitureType.BORDER);
        addFurniture(border);
        settings.getFurniture().forEach(e -> addFurniture(e));

        /* Make a test sound source */
        soundSources = new ArrayList<>();
        soundSources.add(new SoundSource(new Vector(300, 300), 200, 1000));

        agents = new ArrayList<>();
        guardsSeen = new VectorSet();
        intrudersSeen = new VectorSet();

        /* On creation add the right number of guards */
        for(int i = 0; i < settings.getNoOfGuards(); i++)
        {
            Vector srt = randPosition(guardSpawn);
            Vector dir = randDirection();
            Agent guard = AgentType.agentOf(settings.getGuardType(), srt, dir, 10, Type.GUARD);
            guard.setMaxWalk(settings.getWalkSpeedGuard());
            guard.setMaxSprint(settings.getSprintSpeedGuard());
            agents.add(guard);
        }

        /* On creation add the right number of infiltrators */
        for(int i = 0; i < settings.getNoOfIntruders(); i++)
        {
            Vector srt = randPosition(intruderSpawn);
            Vector dir = new Vector(-1, 1); // TODO Return to random
            Universe.setMap(this);
            Agent intruder = AgentType.agentOf(settings.getIntruderType(), srt, dir, 10, Type.INTRUDER);
            intruder.setMaxWalk(settings.getWalkSpeedIntruder());
            intruder.setMaxSprint(settings.getSprintSpeedIntruder());
            agents.add(intruder);
        }

        if (humanActive)
            activateHuman();

        this.coverage = new Coverage(this);
    }

    public Map(Agent agent, ArrayList<Furniture> obstacles)
    {
        agents = new ArrayList<>();
        agents.add(agent);

        furniture = new ArrayList<>();
        furniture.addAll(obstacles);
    }

    public Map(ArrayList<Agent> agents, ArrayList<Furniture> obstacles)
    {
        this.agents = new ArrayList<>();
        this.agents.addAll(agents);

        furniture = new ArrayList<>();
        furniture.addAll(obstacles);
    }

    public void addFurniture(SettingsObject obj)
    {
        switch(obj.getType())
        {
            case GUARD_SPAWN -> guardSpawn = obj.getRect();
            case INTRUDER_SPAWN -> intruderSpawn = obj.getRect();
            case TARGET -> {
                target = obj.getRect();
                furniture.add(FurnitureFactory.make(obj));
            }
            default -> this.furniture.add(FurnitureFactory.make(obj));
        }
    }

    public MemoryGraph createFullGraph()
    {
        int moveLength = 20;
        if (!agents.isEmpty())
        {
            moveLength = (int) agents.get(0).getMoveLength();
        }
        MemoryGraph<GraphCell, DefaultWeightedEdge> fullGraph = new MemoryGraph<>(moveLength);
        for (int i=10; i<height; i+=moveLength)
        {
            for(int j = 10; j < width; j += moveLength)
            {
                GraphCell vertex = fullGraph.addNewVertex(new Vector(j, i));
                vertex.setOccupied(false);
                for(Furniture f : furniture)
                {
                    if(f.getType().name() == "WALL")
                    {
                        if(f.contains(new Vector(i, j)))
                        {
                            vertex.setObstacle(true);
                        }
                    }
                }
            }
        }
        return fullGraph;
    }

    public void addAgent(Agent agent)
    {
        if(agent.getType() == Type.INTRUDER)
                agent.setPosition(randPosition(intruderSpawn));
        else if(agent.getType() == Type.GUARD)
            agent.setPosition(randPosition(guardSpawn));

        agent.setDirection(new Vector(-1,1));
        agents.add(agent);
    }


    public void updateAllSeen(Agent agent)
    {
        if(agent.getType() == Type.GUARD)
            guardsSeen.addAll(agent.getSeen());
        else
            intrudersSeen.addAll(agent.getSeen());
    }

    public ArrayList<Boundary> getBoundaries()
    {
        ArrayList<Furniture> filteredFurniture = (ArrayList<Furniture>) furniture.clone();
        filterFurnitureType(filteredFurniture, FurnitureType.TARGET);

        ArrayList<Boundary> boundaries = new ArrayList<>();
        filteredFurniture.forEach(e -> boundaries.addAll(e.getBoundaries()));
        return boundaries;
    }

    private void filterFurnitureType(ArrayList<Furniture> furn, FurnitureType type)
    {
        furn.removeIf(e -> e.getType() == type);
    }

    public void drawIndicatorBoxes(GraphicsContext gc)
    {
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


    public double percentageComplete(Type type)
    {
        if(type == Type.GUARD)
            return coverage.percentSeen(guardsSeen);
        else
            return coverage.percentSeen(intrudersSeen);
    }


    public void checkForCapture(Agent currentAgent)
    {
        if(currentAgent.getType() != Type.GUARD)
            return;

        for(Agent otherAgent : agents)
        {
            if(otherAgent.getType() != currentAgent.getType())
            {
                double dist = currentAgent.getPosition().dist(otherAgent.getPosition());
                if(dist <= (currentAgent.getRadius() + otherAgent.getRadius() + 7))
                {
                    deleteAgent(otherAgent);
                }
            }
        }
    }

    public void setHumanActive(boolean active)
    {
        if(!humanActive && active)
        {
            activateHuman();
        }
        else if(humanActive && !active)
        {
            for(Agent a : agents)
            {
                if(a instanceof Human)
                {
                    agents.remove(a);
                }
            }
        }
    }

    public void activateHuman()
    {
        Vector humanStart = new Vector(100,100);    // Do not change this, it will break tests!
        human = new Human(humanStart, new Vector(1, 0), 10, Type.INTRUDER);
        human.setMaxWalk(settings.getWalkSpeedGuard());
        human.setMaxSprint(settings.getSprintSpeedGuard());
        agents.add(human);
    }

    public boolean goalReached()
    {
        for(Agent a: agents)
        {
            if(a.getType() == Type.INTRUDER && target.contains(a.getPosition().getX(), a.getPosition().getY()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Method determines if there is an intruder within a Guard's visual field.
     * @return True if an intruder is within a Guard's visual field. Else False.
     */
    public boolean intruderVisual()
    {
        for(Agent a: agents)
        {
            if(a.getType() == Type.GUARD && a.isTypeSeen(Type.INTRUDER))
            {
                return true;
            }
        }
        return false;
    }


    public int agentsRemaining(Type type)
    {
        int count = 0;
        for(Agent a: agents)
        {
            if(a.getType() == type)
                count++;
        }
        return count;
    }


    public void updateStates()
    {
        ArrayList<Agent> new_states = new ArrayList<>();
        for(Agent a: agents)
        {
            new_states.add(a.nextState());
        }
        agents = new_states;
    }


    public void deleteAgent(Agent agent)
    {
        deletion.push(agent);
    }


    /**
     * @param v: a vector in R^2 on the map
     * @return Type: an enum of the team of the agent or furniture at Vector v,
     * returns null if nothing is found.
     */
    public Type objectAt(Vector v)
    {
        for(Agent a: agents)
        {
            if(a.getPosition().dist(v) <= a.getRadius())
                return a.getType();
        }

        for(Furniture f: furniture)
        {
            if(f.contains(v) && f.getType() != FurnitureType.BORDER)
                return Type.of(f.getType());
        }
        return null;
    }


    public void addSoundSource(Vector position, Type team)
    {
        if(team == Type.GUARD)
            soundSources.add(new SoundSource(position, 200, 1000));
        else if(team == Type.INTRUDER)
            soundSources.add(new SoundSource(position, 200, 2000));
    }


    public void addTrail(Trail t)
    {
        trails.add(t);
    }


    public void garbageCollection()
    {
        for(Agent a: deletion)
        {
            agents.remove(a);
        }

        ArrayList<SoundSource> delete = new ArrayList<>();
        for(SoundSource soundSource: soundSources)
        {
            if(soundSource.getAmplitude() < 0.1)
                delete.add(soundSource);
        }
        soundSources.removeAll(delete);
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
