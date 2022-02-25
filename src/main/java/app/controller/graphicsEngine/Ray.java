package app.controller.graphicsEngine;

import app.controller.linAlg.Vector;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

public class Ray
{
    @Getter private Vector u;
    @Getter private Vector v;
    private Color colour = Color.RED;
    private final double LINE_WIDTH = 1;

    public Ray(Vector u, Vector v)
    {
        this.u = u;
        this.v = v;
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
        gc.strokeLine(getU().getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                      getU().getY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                      getV().getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                      getV().getY() * Info.getInfo().zoom + Info.getInfo().offsetY);
    }
}
