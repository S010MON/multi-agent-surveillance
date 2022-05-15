package app.model.agents;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.controller.soundEngine.SoundVector;
import app.model.Move;
import app.model.Type;
import app.view.agentView.AgentView;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;


public class AgentImp implements Agent
{
    @Getter @Setter protected double maxWalk = 10;
    @Getter @Setter protected double maxSprint = 30;
    @Getter @Setter protected Vector direction;
    @Getter @Setter protected boolean moveFailed;
    @Getter @Setter protected Vector tgtDirection;
    @Getter protected Type type;
    @Getter protected Vector position;
    @Getter protected double radius;
    @Getter protected ArrayList<Ray> view;
    @Getter protected ArrayList<SoundVector> heard;
    @Getter protected VectorSet seen;
    @Getter protected AgentView agentViewWindow;
    protected final boolean DRAW_HEARD = false;

    @Getter @Setter protected World world;

    public AgentImp(Vector position, Vector direction, double radius, Type type)
    {
        this.direction = direction;
        this.position = position;
        this.radius = radius;
        this.type = type;
        this.tgtDirection = null;
        view = new ArrayList<>();
        seen = new VectorSet();
        heard = new ArrayList<>();
    }

    public AgentImp(Vector position, Vector direction, double radius, Type type, Vector tgtDirection)
    {
        this.direction = direction;
        this.position = position;
        this.radius = radius;
        this.type = type;
        this.tgtDirection = tgtDirection;
        view = new ArrayList<>();
        seen = new VectorSet();
        heard = new ArrayList<>();
    }

    public AgentImp(Agent other)
    {
        this.direction = other.getDirection();
        this.position = other.getPosition();
        this.radius = other.getRadius();
        this.type = other.getType();
        this.tgtDirection = other.getTgtDirection();
        this.view = other.getView();
        this.seen = other.getSeen();
        this.maxSprint = other.getMaxSprint();
        this.maxWalk = other.getMaxWalk();
        this.agentViewWindow = other.getAgentViewWindow();
        // TODO add hearing after SoundEngine refactor merged
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
    }

    @Override
    public Move move()
    {
        int theta = (int) (Math.random() * 360);
        direction = new Vector(0, 1).rotate(theta);
        Vector mov = new Vector(maxWalk, maxWalk).rotate(theta);

        return new Move(direction, mov);
    }

    @Override
    public void updateView(ArrayList<Ray> view)
    {
        this.view = view;

        if(agentViewWindow != null)
            agentViewWindow.update();
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        if(DRAW_HEARD)
            heard.forEach(h -> h.draw(gc, this.position));

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3.0);
        if(type == Type.GUARD)
            gc.setFill(Color.BLUE);
        else
            gc.setFill(Color.RED);

        double x = (position.getX()-(radius/2)) * Info.getInfo().zoom + Info.getInfo().offsetX;
        double y = (position.getY()-(radius/2)) * Info.getInfo().zoom + Info.getInfo().offsetY;

        gc.fillOval(x, y, radius, radius);
        gc.strokeOval(x , y, radius, radius);
    }

    @Override
    public void clearHeard()
    {
        heard = new ArrayList<>();
    }

    @Override
    public void addHeard(ArrayList<SoundVector> soundVectors)
    {
        heard.addAll(soundVectors);
    }

    @Override
    public boolean isHit(Ray ray)
    {
        return Intersection.hasIntersection(ray, position, radius);
    }

    @Override
    public Vector intersection(Ray ray)
    {
        return Intersection.findIntersection(ray, position, radius);
    }

    @Override
    public boolean isCrossed(Vector startPoint, Vector endPoint)
    {
        return false;
    }

    @Override
    public boolean isCrossed(Vector centre, double radius)
    {
        return position.dist(centre) <= radius;
    }

    @Override
    public Vector getTeleport()
    {
        return null;
    }

    @Override
    public void updateSeen(Vector vector)
    {
        this.seen.add(vector);
    }

    @Override
    public void addViewWindow(AgentView agentView)
    {
        agentViewWindow = agentView;
    }

    @Override
    public Agent nextState()
    {
        return this;
    }

    @Override
    public boolean isTypeSeen(Type type)
    {
        for(Ray r : view)
        {
            if(r.getType() == type)
                return true;
        }
        return false;
    }
}
