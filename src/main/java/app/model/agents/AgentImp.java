package app.model.agents;

import app.controller.Beam;
import app.controller.Vector;
import app.controller.graphicsEngine.Ray;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class AgentImp implements Agent
{
    protected final double MAX_WALK = 10;
    protected final double MAX_RUN = 20;

    protected Vector position;
    protected Vector direction;
    protected double radius;
    protected ArrayList<Beam> view;

    public AgentImp(Vector position, Vector direction, double radius)
    {
        this.direction = direction;
        this.position = position;
        this.radius = radius;
        view = new ArrayList<>();
    }

    @Override
    public void move()
    {
        double x = Math.random() * MAX_WALK;
        if(Math.random() > 0.5)
            x = x * -1;
        double y = Math.random() * MAX_WALK;
        if(Math.random() > 0.5)
            y = y * -1;

        position = position.add(new Vector(x, y));
    }

    @Override
    public Vector getDirection()
    {
        return direction;
    }

    @Override
    public ArrayList<Beam> getView()
    {
        return view;
    }

    @Override
    public void updateView(ArrayList<Beam> view)
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
}
