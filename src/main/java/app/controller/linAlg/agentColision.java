package app.controller.linAlg;

import static java.lang.Math.*;

public abstract class agentColision {


    // startPoint/endPoint  is the start/end point of agent
    // c  is stable agent
    public static boolean hasColisionRecCir(Vector startPoint, Vector endPoint, double r_1, Vector c, double r_2) {

        boolean overlapRec,overlapCir;

        // checking rect with circle
        double theta = findAngle(startPoint, endPoint);
        Vector transCircle = transCircle(theta, startPoint, endPoint, c);
        Vector recBL_origin = findPointOnCircle(startPoint, r_1, theta - 90);
        Vector recTR_origin = findPointOnCircle(endPoint, r_1, theta + 90);
        Vector recCenter = new Vector(endPoint.getX() - startPoint.getX(), endPoint.getY() - startPoint.getY());

        Vector rectBL_rotated = rotateCorner(recCenter,recBL_origin,theta);
        Vector rectTR_rotated = rotateCorner(recCenter,recTR_origin,theta);
        overlapRec = hasOverlapCircleRectangle(r_2, transCircle,rectBL_rotated,rectTR_rotated);


        // checking circle collision
        double distStart = sqrt(pow(c.getX() - startPoint.getX(),2) + pow(c.getY()- startPoint.getY(),2));
        double distEnd = sqrt(pow(c.getX() - endPoint.getX(),2) + pow(c.getY()- endPoint.getY(),2));
        if (distStart <= r_1 + r_2 || distEnd <= r_1 + r_2){
            overlapCir = true;
        }else {
            overlapCir = false;
        }

        return overlapRec && overlapCir;

    }

    //    rotate the corner point of rectangle
    // c = center of the square coordinates
    // p = corner point
    // theta is the angle of rotation

    public static Vector rotateCorner(Vector c, Vector p,double theta) {
        // translate point to origin
        double tempX = p.getX() - c.getX();
        double tempY = p.getY() - c.getY();

        // now apply rotation
        double rotatedX = tempX*cos(theta) - tempY*sin(theta);
        double rotatedY = tempX*sin(theta) + tempY*cos(theta);

        // translate back
        return new Vector(rotatedX + c.getX(),rotatedY + c.getY());
    }



    //    calculate the rectangle points
    public static double findAngle(Vector a, Vector b) {
        double delta_x = b.getX() - a.getX();
        double delta_y = b.getY() - a.getY();
        double inRads = atan2(delta_y, delta_x);

        return toDegrees(inRads);
    }

    public static Vector findPointOnCircle(Vector c, double r, double theta) {
        return new Vector(c.getX() + r * sin(theta), c.getY() + r * cos(theta));
    }

    // rotate the other circle base on the rotation of rectangle
    // startPoint/endPoint  is the start/end point of agent

    public static Vector transCircle(double theta, Vector startPoint, Vector endPoint, Vector circle) {

        double originX = endPoint.getX() - startPoint.getX();
        double originY = endPoint.getY() - startPoint.getY();

        double x = cos(theta) * (circle.getX() - originX) - sin(theta) * (circle.getY() - originY) + originX;
        double y = sin(theta) * (circle.getX() - originX) - cos(theta) * (circle.getY() - originY) + originY;

        return new Vector(x, y);
    }



    // vector a = first diagonal point rectangle BL/TR
    // vector b = second diagonal point rectangle

    public static boolean hasOverlapCircleRectangle(double r, Vector c, Vector a, Vector b) {
        // find the nearest point to rec
        double Xn = max(a.getX(), min(c.getX(), b.getX()));
        double Yn = max(a.getY(), min(c.getY(), b.getY()));

        double Dx = Xn - c.getX();
        double Dy = Yn - c.getY();
        return (Dx * Dx + Dy * Dy) <= r * r;
    }



    // refrence
//    https://www.geeksforgeeks.org/check-if-any-point-overlaps-the-given-circle-and-rectangle/#:~:text=Case%201%3A%20The%20side%20of,that%20both%20the%20shapes%20intersect.
// https://gamedev.stackexchange.com/questions/86755/how-to-calculate-corner-positions-marks-of-a-rotated-tilted-rectangle
}
