package app.controller.graphicsEngine;

import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ray
{
    public Vector u;
    public Vector v;
    private double angle;
    private Color colour = Color.RED;
    private final double LINE_WIDTH = 1;

    public Ray(Vector u, Vector v)
    {
        this.u = u;
        this.v = v;
        this.angle = angle();
    }

    public Vector getV()
    {
        return v;
    }

    public Vector getU()
    {
        return u;
    }

    public double angle()
    {
        return v.sub(u).getAngle();
    }

    public Ray rotate(double degrees)
    {
        Vector a = v.sub(u);
        Vector rotatedVector = a.rotate(degrees);
        Vector b = u.add(rotatedVector);
        return new Ray(this.u, b);
    }

    public void draw(GraphicsContext gc)
    {
        gc.setStroke(colour);
        gc.setLineWidth(LINE_WIDTH);
        gc.strokeLine(getU().getX(), getU().getY(), getV().getX(), getV().getY());
    }
}
