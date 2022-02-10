package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class InvisibleBoundary implements Boundary {
    protected Vector a;
    protected Vector b;

    public InvisibleBoundary(Vector a, Vector b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    @Override
    public boolean isHit(Ray ray) {
        double x_1 = a.getX();
        double y_1 = a.getY();
        double x_2 = b.getX();
        double y_2 = b.getY();
        double x_3 = ray.getU().getX();
        double y_3 = ray.getU().getY();
        double x_4 = ray.getV().getX();
        double y_4 = ray.getV().getY();

        // Source: https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
        double denominator = (x_1 - x_2) * (y_3 - y_4) - (y_1 - y_2) * (x_3 - x_4);

        if (denominator == 0)
            return false;

        double t = ((x_1 - x_3) * (y_3 - y_4) - (y_1 - y_3) * (x_3 - x_4)) / denominator;
        double u = ((x_1 - x_3) * (y_1 - y_2) - (y_1 - y_3) * (x_1 - x_2)) / denominator;

        return 0 < t && t < 1 && u > 0;
    }

    @Override
    public Vector intersection(Ray ray) {
        double x_1 = a.getX();
        double y_1 = a.getY();
        double x_2 = b.getX();
        double y_2 = b.getY();
        double x_3 = ray.getU().getX();
        double y_3 = ray.getU().getY();
        double x_4 = ray.getV().getX();
        double y_4 = ray.getV().getY();

        double denominator = (x_1 - x_2) * (y_3 - y_4) - (y_1 - y_2) * (x_3 - x_4);
        double t = ((x_1 - x_3) * (y_3 - y_4) - (y_1 - y_3) * (x_3 - x_4)) / denominator;
        double x = x_1 + (t * (x_2 - x_1));
        double y = y_1 + (t * (y_2 - y_1));
        return new Vector(x, y);
    }

    @Override
    public boolean validMove(Vector startPoint, Vector endPoint)
    {
        Vector q1 = startPoint;
        Vector p1 = endPoint;
        Vector p2 = a;
        Vector q2 = b;
        if(hasIntersect(p1, q1, p2, q2)) {
            return true;
        }else{
            return false;}
    }


    // set linear function for two point
    // return and array of int [cx,cy,con] which in here m is the slope of line , cx is coefficient x (slope of the line)
    // b = is coefficient of y
    // con = constant of linear function
//    public ArrayList<Double> linearFunction(Vector a, Vector b) {
//        ArrayList<Double> linearFunction = new ArrayList<Double>(3);
//        double xa = a.getX();
//        double ya = a.getY();
//        double xb = b.getX();
//        double yb = b.getY();
//
//        Double cx = (yb - ya) / (xb - xa);
//        Double con = ya - (cx * xa);
//        Double cy = -ya;
//
//        linearFunction.add(cx);
//        linearFunction.add(cy);
//        linearFunction.add(con);
//
//        return linearFunction;
//
//    }


    // Given three collinear points p, q, r, the function checks if
    // point q lies on line segment 'pr'
    private  static boolean onSegment(Vector p, Vector q, Vector r) {
        if (q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX()) &&
                q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY()))
            return true;

        return false;
    }

    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are collinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    private int orientation(Vector p, Vector q, Vector r) {
        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
        // for details of below formula.
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) -
                (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0) return 0; // collinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    public boolean hasIntersect(Vector p1, Vector q1, Vector p2, Vector q2) {
        // Find the four orientations needed for general and
        // special cases
        int o2 = orientation(p1, q1, q2);
        int o1 = orientation(p1, q1, p2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // p1, q1 and p2 are collinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;

        // p1, q1 and q2 are collinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;

        // p2, q2 and p1 are collinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;

        // p2, q2 and q1 are collinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;

        return false; // Doesn't fall in any of the above cases
    }


}
