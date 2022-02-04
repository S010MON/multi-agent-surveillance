package app.model;

import app.controller.Ray;
import app.controller.Vector;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Wall implements Placeable
{
    private Vector a;
    private Vector b;
    public Wall(Vector a, Vector b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public Color getColor()
    {
        return Color.BLACK;
    }

    @Override
    public boolean isHit(Ray ray)
    {
        double x_1 = a.getX();
        double y_1 = a.getY();
        double x_2 = b.getX();
        double y_2 = b.getY();
        double x_3 = ray.getOrigin().getX();
        double y_3 = ray.getOrigin().getY();
        double x_4 = ray.getDirection().getX();
        double y_4 = ray.getDirection().getY();

        // Source: https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
        double denominator = (x_1 - x_2)*(y_3 - y_4) - (y_1 - y_2)*(x_3 - x_4);

        if(denominator == 0)
            return false;

        double t = ((x_1 - x_3)*(y_3 - y_4) - (y_1 - y_3)*(x_3 - x_4)) / denominator;
        double u = (-(x_1 - x_3)*(y_1 - y_2) - (y_1 - y_3)*(x_1 - x_2)) / denominator;

        return 0 < t && t < 1 && u > 0;
    }

    @Override
    public Vector intersection(Ray ray)
    {
        double x_1 = a.getX();
        double y_1 = a.getY();
        double x_2 = b.getX();
        double y_2 = b.getY();
        double x_3 = ray.getOrigin().getX();
        double y_3 = ray.getOrigin().getY();
        double x_4 = ray.getDirection().getX();
        double y_4 = ray.getDirection().getY();

        double denominator = (x_1 - x_2)*(y_3 - y_4) - (y_1 - y_2)*(x_3 - x_4);
        double t = ((x_1 - x_3)*(y_3 - y_4) - (y_1 - y_3)*(x_3 - x_4)) / denominator;
        double x = x_1 + (t * (x_2 - x_1));
        double y = y_1 + (t * (y_2 - y_1));
        return new Vector(x, y);
    }
}
