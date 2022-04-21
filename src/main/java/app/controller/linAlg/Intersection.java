package app.controller.linAlg;

import app.controller.graphicsEngine.Ray;
import app.controller.soundEngine.SoundRay;
import app.model.boundary.Boundary;

import static java.lang.Math.atan2;
import static java.lang.Math.toDegrees;

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
        if (denominator == 0)
            return null;

        double t = ((x_1 - x_3) * (y_3 - y_4) - (y_1 - y_3) * (x_3 - x_4)) / denominator;
        double u = ((x_1 - x_3) * (y_1 - y_2) - (y_1 - y_3) * (x_1 - x_2)) / denominator;

        // lines intersect but not within the segments
        if (t > 1 || t < 0 || u > 1 || u < 0)
            return null;

        double x = x_1 + (t * (x_2 - x_1));
        double y = y_1 + (t * (y_2 - y_1));
        return new Vector(x, y);
    }

    public static Vector findIntersection(Vector a, Vector b, Ray r)
    {
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
        if (denominator == 0)
            return null;

        double t = ((x_1 - x_3) * (y_3 - y_4) - (y_1 - y_3) * (x_3 - x_4)) / denominator;
        double u = ((x_1 - x_3) * (y_1 - y_2) - (y_1 - y_3) * (x_1 - x_2)) / denominator;

        // lines intersect but not within the segments
        if (t > 1 || t < 0 || u < 0)
            return null;

        double x = x_1 + (t * (x_2 - x_1));
        double y = y_1 + (t * (y_2 - y_1));
        return new Vector(x, y);
    }

    /**
     * Finds the intersection of a line A-B and circle centre, radius.  If two intersections exist,
     * then it returns the closest intersection point to point A.
     * @param A  2d vector start point of line
     * @param B  2d vector end point of line
     * @param center 2d vector centre of circle
     * @param radius double radius of circle
     * @return 2d vector, the closest point of intersection.
     * @throws RuntimeException if no point exists; use (has intersection to check before calling)
     *
     * Source: https://stackoverflow.com/questions/13053061/circle-line-intersection-points
     */
    public static Vector findIntersection(Vector A, Vector B, Vector center, double radius)
    {
        double baX = B.getX() - A.getX();
        double baY = B.getY() - A.getY();
        double caX = center.getX() - A.getX();
        double caY = center.getY() - A.getY();

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;

        double pBy2 = bBy2 / a;
        double q = c / a;
        double disc = pBy2 * pBy2 - q;

        if (disc < 0)
            throw new RuntimeException("Negative discriminant: no intersection exists.  Use `hasIntersection` method to check");

        double tmpSqrt = Math.sqrt(disc);
        double abScalingFactor1 = -pBy2 + tmpSqrt;
        double abScalingFactor2 = -pBy2 - tmpSqrt;

        Vector p1 = new Vector(A.getX() - baX * abScalingFactor1, A.getY() - baY * abScalingFactor1);
        if (disc == 0) // if the discriminant == 0 -> only one intersection exists.
            return p1;

        Vector p2 = new Vector(A.getX() - baX * abScalingFactor2, A.getY() - baY * abScalingFactor2);

        if(p1.dist(A) < p2.dist(A))
            return p1;
        else
            return p2;
    }

    public static Vector findIntersection(Ray ray, Vector centre, double radius)
    {
        return findIntersection(ray.getU(), ray.getV(), centre, radius);
    }

    public static Vector findIntersection(SoundRay r1, SoundRay r2)
    {
        // segment-segment intersection
        return findIntersection(r1.getU(), r1.getV(), r2.getU(), r2.getV());
    }

    public static boolean hasIntersection(Vector A, Vector B, Vector center, double radius)
    {
        double baX = B.getX() - A.getX();
        double baY = B.getY() - A.getY();
        double caX = center.getX() - A.getX();
        double caY = center.getY() - A.getY();

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;

        return disc >= 0;
    }

    public static boolean hasIntersection(Ray ray, Vector center, double radius)
    {
        return hasIntersection(ray.getU(), ray.getV(), center, radius);
    }

    public static boolean hasIntersection(Vector p_1, Vector p_2, Vector p_3, Vector p_4)
    {
        return findIntersection(p_1, p_2, p_3, p_4) != null;
    }

    public static boolean hasIntersection(Vector a, Vector b, Ray r)
    {
        return findIntersection(a, b, r) != null;
    }

    public static boolean hasIntersection(SoundRay r1, SoundRay r2)
    {
        return findIntersection(r1, r2) != null;
    }

    public static boolean hasLimitedIntersection(Vector A, Vector B, Vector center, double radius)
    {
        double baX = B.getX() - A.getX();
        double baY = B.getY() - A.getY();
        double caX = center.getX() - A.getX();
        double caY = center.getY() - A.getY();

        double a = baX * baX + baY * baY;
        double bBy2 = baX * caX + baY * caY;
        double c = caX * caX + caY * caY - radius * radius;

        double pBy2 = bBy2 / a;
        double q = c / a;

        double disc = pBy2 * pBy2 - q;

        if(disc >= 0)
        {
            Line line = new Line(A, B);
            Vector intersection = findIntersection(A, B, center, radius);
            return line.liesOn(intersection);
        }
        else
            return false;
    }

    public static boolean hasLimitedIntersection(Ray ray, Vector center, double radius)
    {
        return hasLimitedIntersection(ray.getU(), ray.getV(), center, radius);
    }

    public static boolean hasDirectionIntersect(Vector start,
                                                Vector end,
                                                double radius,
                                                Vector positionOther,
                                                double radiusOther)
    {
        Vector recCenter = start.add(end).scale(0.5);
        // rotate all the positions to the degree 0
        Vector start_rotate = recCenter.findPointOnCircle(start.dist(recCenter),180);
        Vector end_rotate = recCenter.findPointOnCircle(end.dist(recCenter), 0);

        Vector recBL_rotate = start_rotate.findPointOnCircle(radius,270);
        Vector recTR_rotate = end_rotate.findPointOnCircle(radius,90);

        Vector otherRotate = recCenter.findPointOnCircle(positionOther.dist(recCenter),0);

        // check if circle is in the rectangle but has no intersect
        if(otherRotate.getX() >= recBL_rotate.getX() &&
                otherRotate.getX() <= recTR_rotate.getX() &&
                otherRotate.getY() >= recBL_rotate.getY() &&
                otherRotate.getY() <= recTR_rotate.getY()){
            return true;
        }

        // find the nearest point to rec
        double Xn = Math.max(recBL_rotate.getX(), Math.min(otherRotate.getX(), recTR_rotate.getX()));
        double Yn = Math.max(recBL_rotate.getY(), Math.min(otherRotate.getY(), recTR_rotate.getY()));
        double Dx = Xn - otherRotate.getX();
        double Dy = Yn - otherRotate.getY();

        return (Dx * Dx + Dy * Dy) <= radiusOther * radiusOther;
    }


}
