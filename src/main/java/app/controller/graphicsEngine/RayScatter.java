package app.controller.graphicsEngine;

import app.controller.linAlg.Vector;
import java.util.ArrayList;

public abstract class RayScatter
{
    public static ArrayList<Ray> angle360(Vector origin)
    {
        ArrayList<Ray> rays = new ArrayList<>();
        Vector dir = new Vector(0,100);
        for(int i = 0; i < 360; i++)
        {
            rays.add(new Ray(origin, origin.add(dir.rotate(i))));
        }
        return rays;
    }

    public static ArrayList<Ray> angle(Vector origin, Vector direction, double angle)
    {
        ArrayList<Ray> rays = new ArrayList<>();
        for(int i = 0; i < angle; i++)
        {
            rays.add(new Ray(origin, origin.add(direction.rotate(i))));
        }
        for(int i = 0; i < angle; i++)
        {
            rays.add(new Ray(origin, origin.add(direction.rotate(-i))));
        }
        return rays;
    }
}
