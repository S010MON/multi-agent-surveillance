package app.model.agents;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.controller.soundEngine.SoundRay;
import app.model.Move;
import app.view.agentView.AgentView;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;


public class AgentImp implements Agent
{
    @Getter @Setter protected double maxWalk = 5;
    @Getter @Setter protected double maxSprint = 10;
    @Getter @Setter protected Vector direction;
    @Getter @Setter protected boolean moveFailed;
    @Getter protected Team team;
    @Getter protected Vector position;
    @Getter protected double radius;
    @Getter protected ArrayList<Ray> view;
    @Getter protected ArrayList<SoundRay> hearing;
    @Getter protected VectorSet seen;
    protected AgentView agentViewWindow;

    public AgentImp(Vector position, Vector direction, double radius, Team team)
    {
        this.direction = direction;
        this.position = position;
        this.radius = radius;
        this.team = team;
        view = new ArrayList<>();
        seen = new VectorSet();
        hearing = new ArrayList<>();
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
    public void updateHeard(ArrayList<SoundRay> hearing)
    {
        this.hearing = hearing;
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3.0);
        if(team == Team.GUARD)
            gc.setFill(Color.BLUE);
        else
            gc.setFill(Color.RED);

        double x = (position.getX()-(radius/2)) * Info.getInfo().zoom + Info.getInfo().offsetX;
        double y = (position.getY()-(radius/2)) * Info.getInfo().zoom + Info.getInfo().offsetY;

        gc.fillOval(x, y, radius, radius);
        gc.strokeOval(x , y, radius, radius);
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
    public boolean isCrossed(Vector startPoint, Vector endPoint) {
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
}
