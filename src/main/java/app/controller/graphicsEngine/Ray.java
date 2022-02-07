package app.controller.graphicsEngine;

import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ray
{
    public Vector u;
    public Vector v;
    private Color colour = Color.RED;
    private final double LINE_WIDTH = 1;

    public Ray(Vector u, Vector direction)
    {
        this.u = u;
        this.v = direction;
    }

    public Vector getV()
    {
        return v;
    }

    public Vector getU()
    {
        return u;
    }

    public Ray rotate(double degrees)
    {
        Vector unitVector = v.sub(u);
        Vector rotatedVector = unitVector.rotate(degrees);
        Vector newVector = u.add(rotatedVector);
        return new Ray(this.u, newVector);
    }

    public double rayLength()
    {
        double yValue = u.getY() - v.getY();
        double xValue = u.getX() - v.getX();
        return Math.sqrt(Math.pow(yValue, 2) + Math.pow(xValue, 2));
    }

    public void draw(GraphicsContext gc)
    {
        gc.setStroke(colour);
        gc.setLineWidth(LINE_WIDTH);
        gc.strokeLine(getU().getX(), getU().getY(), getV().getX(), getV().getY());
    }
}
