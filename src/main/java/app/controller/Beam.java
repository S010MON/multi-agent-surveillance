package app.controller;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Beam
{
    private final double LINE_WIDTH = 1;

    private Vector u;
    private Vector v;
    private Color colour = Color.RED;


    public Beam(Vector u, Vector v)
    {
        this.u = u;
        this.v = v;
    }

    public Vector getU()
    {
        return u;
    }

    public Vector getV()
    {
        return v;
    }

    public Beam rotate(double degrees)
    {
        Vector unitVector = v.sub(u);
        Vector rotatedVector = unitVector.rotate(degrees);
        Vector newVector = u.add(rotatedVector);
        return new Beam(u, newVector);
    }

    public void draw(GraphicsContext gc)
    {
        gc.setStroke(colour);
        gc.setLineWidth(LINE_WIDTH);
        gc.strokeLine(getU().getX(), getU().getY(), getV().getX(), getV().getY());
    }
}
