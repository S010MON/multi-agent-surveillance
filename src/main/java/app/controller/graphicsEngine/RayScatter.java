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
}
