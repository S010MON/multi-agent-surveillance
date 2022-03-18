package app.model.agents;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.model.Move;
import app.view.agentView.AgentView;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashSet;
import lombok.Getter;
import lombok.Setter;

public class AgentImp implements Agent
{
    @Getter @Setter protected double maxWalk = 5;
    @Getter @Setter protected double maxSprint = 10;
    @Getter protected Vector position;
    @Getter @Setter protected Vector direction;
    @Getter protected double radius;
    @Getter protected ArrayList<Ray> view;
    @Getter protected VectorSet seen;
    protected AgentView agentViewWindow;

    public AgentImp(Vector position, Vector direction, double radius)
    {
        this.direction = direction;
        this.position = position;
        this.radius = radius;
        view = new ArrayList<>();
        seen = new VectorSet();
    }

    @Override
    public void updateLocation(Vector endPoint)
    {
        position = endPoint;
    }

    @Override
    public Move move()
    {
        double x = Math.random() * maxWalk;
        if(Math.random() > 0.5)
            x = x * -1;
        double y = Math.random() * maxWalk;
        if(Math.random() > 0.5)
            y = y * -1;

        return new Move(new Vector(), new Vector(x, y));
    }

    @Override
    public void updateView(ArrayList<Ray> view)
    {
        this.view = view;

        if(agentViewWindow != null)
            agentViewWindow.update();
    }

    @Override
    public double getHearing()
    {
        return 0;
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        gc.setFill(Color.BLACK);
        gc.fillOval((position.getX()-(radius/2)) * Info.getInfo().zoom + Info.getInfo().offsetX,
                    (position.getY()-(radius/2)) * Info.getInfo().zoom + Info.getInfo().offsetY,
                       radius,
                       radius);
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
