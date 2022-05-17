package app.controller.linAlg;

import app.controller.graphicsEngine.Ray;
import java.util.ArrayList;
import java.util.List;

public abstract class Angle
{
    public static boolean angleInRange(double angle, double upper, double lower)
    {
        //normalize angles to be 1-360 degrees
        double N = normalizeAngle(angle);
        double a = normalizeAngle(lower);
        double b = normalizeAngle(upper);

        //check if angle is between angles
        if(a < b)
            return a <= N && N <= b;
        return a <= N || N <= b;
    }

    /**
     * function to convert angle to 1-360 degrees
     */
    public static double normalizeAngle(double angle)
    {
        double floorAngle = Math.floor(angle);

        double outAngle = (floorAngle % 360) + (angle - floorAngle); //converts angle to range -360 + 360
        if(outAngle <= 0.0)
            outAngle += 360.0;

        return outAngle;
    }

    public static List<Ray> raysWithAngle(double angle, double anglePrecision, List<Ray> rays)
    {
        List<Ray> raysWithRightAngle = new ArrayList<>();
        for (Ray r : rays)
        {
            if (Angle.angleInRange(r.angle(), angle + anglePrecision, angle - anglePrecision))
            {
                raysWithRightAngle.add(r);
            }
        }

        return raysWithRightAngle;
    }

    public static List<Ray> raysWithAngle(double angle, List<Ray> rays)
    {
        return raysWithAngle(angle, 1, rays);
    }
}
