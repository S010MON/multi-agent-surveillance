package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import java.util.Stack;

public class SoundRayScatter
{
    public static Stack<SoundRay> angle360(Vector origin, int noOfRays, double maxDist, int bounces)
    {
        Stack<SoundRay> rays = new Stack<>();
        Vector dir = new Vector(0,maxDist);
        int increment = 360/noOfRays;

        for(int i = 0; i < 360; i += increment)
        {
            rays.add(new SoundRay(origin, origin.add(dir.rotate(i)), bounces-1));
        }
        return rays;
    }
}
