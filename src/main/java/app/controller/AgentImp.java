package app.controller;

import app.model.Agent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class AgentImp implements Agent
{
    protected final double MAX_WALK = 10;
    protected final double MAX_RUN = 20;

    protected Vector position;
    protected Vector direction;
    protected double radius;

    public AgentImp(Vector position, Vector direction, double radius)
    {
        this.direction = direction;
        this.position = position;
        this.radius = radius;
    }

    @Override
    public void move()
    {
        // Null
    }

    @Override
    public Vector getDirection()
    {
        return direction;
    }

    @Override
    public ArrayList<Ray> getView()
    {
        return null;
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
    public Color getColor()
    {
        return Color.BLACK;
    }

    @Override
    public boolean isHit(Vector v)
    {
        return position.dist(v) <= radius;
    }

    @Override
    public Vector intersection(Vector v)
    {
        return null;
    }
}
