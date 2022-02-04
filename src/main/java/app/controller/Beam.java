package app.controller;

public class Beam
{
    private Vector position;
    private Vector direction;

    public Beam(Vector position, Vector direction)
    {
        this.position = position;
        this.direction = direction;
    }

    public Vector getPosition()
    {
        return position;
    }

    public Vector getDirection()
    {
        return direction;
    }
}
