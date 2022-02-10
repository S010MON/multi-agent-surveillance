package app.model.agents;

import app.controller.linAlg.Vector;
import app.controller.graphicsEngine.Ray;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class AgentImp implements Agent
{
    protected final double MAX_WALK = 5;
    protected final double MAX_RUN = 10;

    protected Vector position;
    protected Vector direction;
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
    public Vector move()
    {
        double x = Math.random() * MAX_WALK;
        if(Math.random() > 0.5)
            x = x * -1;
        double y = Math.random() * MAX_WALK;
        if(Math.random() > 0.5)
            y = y * -1;

        return new Vector(x, y);
    }

    @Override
    public Vector getDirection()
    {
        return direction;
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
    public Vector getPosition()
    {
        return position;
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        gc.setFill(Color.BLACK);
        gc.fillOval(position.getX()-(radius/2), position.getY()-(radius/2), radius, radius);
    }

    @Override
    public boolean isHit(Ray ray)
    {
        return false;
    }

    @Override
    public Vector intersection(Ray ray)
    {
        return null;
    }

    @Override
    public boolean validMove(Vector startPoint, Vector endPoint) {
        return false;
    }
}
