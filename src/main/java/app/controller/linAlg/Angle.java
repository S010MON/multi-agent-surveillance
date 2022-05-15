package app.controller.linAlg;

import app.controller.graphicsEngine.Ray;
import app.model.Type;

import java.util.ArrayList;
import java.util.List;

public abstract class Angle
{
    public static boolean angleInRange(double angle, double upper, double lower)
    {
        //check if angle is between angles
        double N = normalizeAngle(angle); //normalize angles to be 1-360 degrees
        double a = normalizeAngle(lower);
        double b = normalizeAngle(upper);

        if(a < b)
            return a <= N && N <= b;
        return a <= N || N <= b;
    }

    public static double normalizeAngle(double angle)
    {
        //function to convert angle to 1-360 degrees
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
            if (Angle.angleInRange(r.angle(), angle+anglePrecision, angle-anglePrecision))
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
