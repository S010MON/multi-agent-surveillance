package app.controller.graphicsEngine;

import app.controller.linAlg.Vector;
import app.model.Type;
import app.model.boundary.BoundaryType;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

public class Ray
{
    @Getter private Vector u;
    @Getter private Vector v;
    @Getter private Type type = null;
    protected Color colour = Color.rgb(255,191,0, 0.5);
    protected final double LINE_WIDTH = 1;

    public Ray(Vector u, Vector v)
    {
        this.u = u;
        this.v = v;
    }

    public Ray(Vector u, Vector v, Type type)
    {
        this.u = u;
        this.v = v;
        this.type = type;
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

    public double length()
    {
        double yValue = u.getY() - v.getY();
        double xValue = u.getX() - v.getX();
        return Math.sqrt(Math.pow(yValue, 2) + Math.pow(xValue, 2));
    }

    public Vector direction()
    {
        return v.sub(u).normalise();
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Ray r)
        {
            return this.u.equals(r.getU()) && this.v.equals(r.getV());
        }
        return false;
    }

    public void draw(GraphicsContext gc)
    {
        gc.setStroke(colour);
        gc.setLineWidth(LINE_WIDTH);
        gc.strokeLine(getU().getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                      getU().getY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                      getV().getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                      getV().getY() * Info.getInfo().zoom + Info.getInfo().offsetY);
    }

    @Override
    public String toString()
    {
        return "Ray: "+ u.toString() +", " + v.toString();
    }

    public BoundaryType getBoundaryType()
    {
        return BoundaryType.of(type);
    }
}
