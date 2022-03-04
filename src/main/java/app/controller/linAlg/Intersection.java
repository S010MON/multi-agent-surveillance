package app.controller.linAlg;

import app.controller.graphicsEngine.Ray;
import app.controller.soundEngine.SoundRay;

public abstract class Intersection
{
    public static Vector findIntersection(Vector p_1, Vector p_2, Vector p_3, Vector p_4)
    {
        // segment-segment intersection
        double x_1 = p_1.getX();
        double y_1 = p_1.getY();
        double x_2 = p_2.getX();
        double y_2 = p_2.getY();
        double x_3 = p_3.getX();
        double y_3 = p_3.getY();
        double x_4 = p_4.getX();
        double y_4 = p_4.getY();

        double denominator = (x_1 - x_2) * (y_3 - y_4) - (y_1 - y_2) * (x_3 - x_4);

        // lines are parallel
        if (denominator == 0){
            return null;
        }

        double t = ((x_1 - x_3) * (y_3 - y_4) - (y_1 - y_3) * (x_3 - x_4)) / denominator;
        double u = ((x_1 - x_3) * (y_1 - y_2) - (y_1 - y_3) * (x_1 - x_2)) / denominator;

        // lines intersect but not within the segments
        if (t > 1 || t < 0 || u > 1 || u < 0){
            return null;
        }

        double x = x_1 + (t * (x_2 - x_1));
        double y = y_1 + (t * (y_2 - y_1));
        return new Vector(x, y);
    }

    public static Vector findIntersection(Vector a, Vector b, Ray r){
        // ray-segment intersection (ray is a line from "b" on and a segment up to "a" [a,b) )
        double x_1 = a.getX();
        double y_1 = a.getY();
        double x_2 = b.getX();
        double y_2 = b.getY();
        double x_3 = r.getU().getX();
        double y_3 = r.getU().getY();
        double x_4 = r.getV().getX();
        double y_4 = r.getV().getY();

        double denominator = (x_1 - x_2) * (y_3 - y_4) - (y_1 - y_2) * (x_3 - x_4);

        // lines are parallel
        if (denominator == 0){
            return null;
        }

        double t = ((x_1 - x_3) * (y_3 - y_4) - (y_1 - y_3) * (x_3 - x_4)) / denominator;
        double u = ((x_1 - x_3) * (y_1 - y_2) - (y_1 - y_3) * (x_1 - x_2)) / denominator;

        // lines intersect but not within the segments
        if (t > 1 || t < 0 || u < 0){
            return null;
        }

        double x = x_1 + (t * (x_2 - x_1));
        double y = y_1 + (t * (y_2 - y_1));
        return new Vector(x, y);
    }

    public static Vector findIntersection(SoundRay r1, SoundRay r2){
        // segment-segment intersection
        return findIntersection(r1.getStart(), r1.getEnd(), r2.getStart(), r2.getEnd());
    }

    public static boolean hasIntersection(Vector p_1, Vector p_2, Vector p_3, Vector p_4)
    {
        return findIntersection(p_1, p_2, p_3, p_4) != null;
    }

    public static boolean hasIntersection(Vector a, Vector b, Ray r){
        return findIntersection(a, b, r) != null;
    }

    public static boolean hasIntersection(SoundRay r1, SoundRay r2){
        return findIntersection(r1, r2) != null;
    }

}
