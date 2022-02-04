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

//    @Override
//    public boolean isHit(Vector v)
//    {
//        return  v.getX() >= rectangle.getX() &&
//                v.getX() <= rectangle.getX() + rectangle.getWidth() &&
//                v.getY() >= rectangle.getY() &&
//                v.getY() <= rectangle.getY() + rectangle.getHeight();
//    }

//    @Override
//    public Vector intersection(Vector v)
//    {
//        // Find the closest line
//        Vector a = null;
//        Vector b = null;
//        double da = 1E11;
//        double db = 1E11;
//
//        for(Vector c: corners)
//        {
//            double dist = c.dist(v);
//
//            if(dist < da)       // Check if first closest
//            {
//                a = c;
//                da = dist;
//            }
//            else if (dist < db) // Check if second closest
//            {
//                b = c;
//                db = dist;
//            }
//        }
//        // Line x -> y
//        Vector v_1 = new Vector().sub(a);
//        Vector v_2 = b.sub(a);
//        double dx = a.getX() - b.getX();
//        double dy = a.getY() - b.getY();
//        Vector v_3 = new Vector(dx, dy);
//
//        double pt_1 = v_2.cross(v_1) / v_2.dot(v_3);
//        double pt_2 = v_1.dot(v_3)   / v_2.dot(v_3);
//
//        return new Vector(pt_1, pt_2);
//    }
}
