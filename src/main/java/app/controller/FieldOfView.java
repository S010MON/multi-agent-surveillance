package app.controller;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;

import java.util.ArrayList;

public class FieldOfView {

    /**
     * computes which rays are between the left and right limit clockwise
     * @param rays all rays to determine if between limits, all origins have to be the same!
     * @param leftLimit leftLimit of what the Rays can reach
     * @param rightlimit rightLimit of what the Rays can reach
     * @return
     */
    public static ArrayList<Ray> limitView(ArrayList<Ray> rays, Vector leftLimit, Vector rightlimit)
    {
        //if no rays in list, return the empty list
        if(rays.size()==0)
            return rays;

        //Calculate the angles of the limits with respect to the origin's of the rays
        Ray leftRay = new Ray(rays.get(0).getU(), leftLimit);
        Ray rightRay = new Ray(rays.get(0).getU(), rightlimit);
        double leftAngle = leftRay.angle();
        double rightAngle = rightRay.angle();

        //if left and right angle are the same, than everything is between them
        if(leftAngle==rightAngle)
            return rays;


        ArrayList<Ray> acceptedRays = new ArrayList<>();
        // if rightAngle > leftAngle, return all rays with angle between them
        if(rightAngle>leftAngle)
        {
            for(Ray ray: rays)
            {
                double rayAngle = ray.angle();
                if(rayAngle>leftAngle && rayAngle<rightAngle)
                    acceptedRays.add(ray);
            }
        }
        else
        {
            for(Ray ray: rays)
            {
                double rayAngle = ray.angle();
                if(rayAngle>leftAngle || rayAngle<rightAngle)
                    acceptedRays.add(ray);
            }
        }

        return rays;
    }

}
