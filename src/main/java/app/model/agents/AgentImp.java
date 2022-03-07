package app.model.agents;

import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.controller.graphicsEngine.Ray;
import app.model.map.Move;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class AgentImp implements Agent
{
    @Getter @Setter protected double maxWalk = 5;
    @Getter @Setter protected double maxSprint = 10;
    @Getter protected Vector position;
    @Getter protected Vector direction;
    protected double radius;
    protected ArrayList<Ray> view;

    public AgentImp(Vector position, Vector direction, double radius)
    {
        this.direction = direction;
        this.position = position;
        this.radius = radius;
        view = new ArrayList<>();
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
    public ArrayList<Ray> getView()
    {
        return view;
    }

    @Override
    public void updateView(ArrayList<Ray> view)
    {
        this.view = view;
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
    public boolean validMove(Vector startPoint, Vector endPoint) {
        return false;
    }
}
