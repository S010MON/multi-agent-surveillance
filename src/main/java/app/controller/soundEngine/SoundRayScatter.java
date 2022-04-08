package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import java.util.ArrayList;

public class SoundRayScatter
{
    private static final int bounces = 2;
    public static ArrayList<SoundRay> angle360(Vector origin, int noOfRays, double maxDist)
    {
        ArrayList<SoundRay> rays = new ArrayList<>();
        Vector dir = new Vector(0,maxDist);
        int increment = 360/noOfRays;

        for(int i = 0; i < 360; i += increment)
        {
            rays.add(new SoundRay(origin, origin.add(dir.rotate(i)), bounces));
        }
        return rays;
    }
}
