package app.controller.linAlg;

public abstract class Angle
{
    public static boolean angleInRange(double angle, double upper, double lower){
        //check if angle is between angles
        double N = normalizeAngle(angle); //normalize angles to be 1-360 degrees
        double a = normalizeAngle(lower);
        double b = normalizeAngle(upper);

        if (a < b)
            return a <= N && N <= b;
        throw new RuntimeException("Upper bound needs to be strictly above the lower bound");
    }

    public static double normalizeAngle(double angle){
        //function to convert angle to 1-360 degrees
        double floorAngle = Math.floor(angle);

        double outAngle = (floorAngle % 360) + (angle-floorAngle); //converts angle to range -360 + 360
        if(outAngle <= 0.0)
            outAngle += 360.0;

        return outAngle;
    }
}
