package app.model.agents;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Angle;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.controller.soundEngine.SoundVector;
import app.model.Move;
import app.model.Type;
import app.model.agents.Capture.CaptureAgent;
import app.model.agents.Evasion.EvasionDirected;
import app.model.agents.Evasion.EvasionDistanceMax;
import app.model.agents.Evasion.EvasionRandom;
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
    @Getter @Setter protected double moveLength = 20;
    @Getter @Setter protected Vector direction;
    @Getter @Setter protected boolean moveFailed;
    @Getter @Setter protected Vector typePosition;
    @Getter @Setter protected Vector tgtDirection;
    @Getter @Setter protected ArrayList<Ray> view;
    @Getter protected Type type;
    @Getter protected Vector position;
    @Getter protected double radius;
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

    public AgentImp(Agent other)
    {
        this.direction = other.getDirection();
        this.position = other.getPosition();
        this.radius = other.getRadius();
        this.type = other.getType();
        copyOver(other);
    }

    protected void copyOver(Agent other)
    {
        this.tgtDirection = other.getTgtDirection();
        this.view = other.getView();
        this.seen = other.getSeen();
        this.maxSprint = other.getMaxSprint();
        this.maxWalk = other.getMaxWalk();
        this.agentViewWindow = other.getAgentViewWindow();
        this.heard = other.getHeard();
        this.world = other.getWorld();
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

    public boolean typeDetected(Type type)
    {
        for(Ray r : view)
        {
            if(r.getType() == type)
            {
                return true;
            }
        }
        return false;
    }

    public Vector closestTypePos(Type type)
    {
        Vector closestAgentPos = null;
        double closestAgentDist = 10^5;

        for(Ray r : view)
        {
            if(r.getType()==type)
            {
                if(closestAgentPos == null)
                {
                    closestAgentPos = r.getV();
                    closestAgentDist = r.length();
                }
                else if(r.length() < closestAgentDist)
                {
                    closestAgentPos = r.getV();
                    closestAgentDist = r.length();
                }
            }
        }
        return closestAgentPos;
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
        if(vector !=null)
            this.seen.add(vector);
    }

    @Override
    public void addViewWindow(AgentView agentView)
    {
        agentViewWindow = agentView;
    }

    @Override
    public boolean isTypeSeen(Type type)
    {
        for(Ray r : view)
        {
            if(r.getType() == type)
            {
                typePosition = r.getV();
                return true;
            }
        }
        return false;
    }

    /**
     * Method for checking for walls/obstacles for getting next move in the wall following algorithm.
     * Walls/obstacles are checked in the direction of the given rayAngle by checking if that ray detects
     * an obstacle within the moveLength distance range.
     * @param rayAngle angle of the direction to be checked.
     * @param moveLength max dist to obstacle to still return true
     * @param anglePrecision how much difference there can be between the visionRay and the rayAngle
     * @return true if no obstacle detected; false if obstacle detected
     */
    public boolean noWallDetected(double rayAngle, double moveLength, double anglePrecision)
    {
        for (Ray r : view)
        {
            if (Angle.angleInRange(r.angle(),rayAngle+anglePrecision, rayAngle-anglePrecision))
            {
                if (r.length() <= moveLength)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean noWallDetected(double rayAngle, double moveLength)
    {
        return noWallDetected(rayAngle, moveLength, 1);
    }

    public boolean noWallDetected(Vector vector, double moveLength, double anglePrecision)
    {
        return noWallDetected(vector.getAngle(), moveLength, 1);
    }

    public boolean noWallDetected(Vector vector, double moveLength)
    {
        return noWallDetected(vector.getAngle(), moveLength, 1);
    }

    /**
     * Encodes state changes for all types of agent, and returns either the current class or
     * creates a new class of the requisite type for the current state change
     * @return
     */
    @Override
    public Agent nextState()
    {
        // State GUARD
        if(this.type == Type.GUARD)
        {
            if(isTypeSeen(Type.INTRUDER))
                return new CaptureAgent(this);

        }

        // State INTRUDER
        else if(this.type == Type.INTRUDER)
        {
            if(isTypeSeen(Type.GUARD))
                return new EvasionDistanceMax(this);

            if(isTypeSeen(Type.TARGET))
                return new TargetAgent(this);

            return this;
        }

        return this;
    }
}