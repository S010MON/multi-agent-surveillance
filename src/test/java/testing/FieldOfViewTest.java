package testing;

import app.controller.graphicsEngine.FieldOfView;
import app.controller.graphicsEngine.Ray;
import app.controller.graphicsEngine.RayScatter;
import app.controller.linAlg.Vector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldOfViewTest {

    @Test
    void leftSameAsRightLimit()
    {
        Vector origin = new Vector(5, 20);
        ArrayList<Ray> rays = RayScatter.angle360(origin);
        Vector lefLimit = new Vector(0, 2);
        Vector rightLimit = new Vector(0, 2);

        ArrayList<Ray> acceptedRays = FieldOfView.limitView(rays, lefLimit, rightLimit);
        assertEquals(rays.size(), acceptedRays.size());
    }

    @Test
    void emptyList()
    {
        ArrayList<Ray> rays = new ArrayList<>();
        Vector lefLimit = new Vector(0, 2);
        Vector rightLimit = new Vector(0, 5);

        ArrayList<Ray> acceptedRays = FieldOfView.limitView(rays, lefLimit, rightLimit);
        assertEquals(0, acceptedRays.size());

    }

    @Test
    void leftSmallerThanRightLimit1()
    {
        Vector origin = new Vector(0,0);
        Vector lefLimit = new Vector(1, 0);
        Vector rightLimit = new Vector(0, -1);

        ArrayList<Ray> rays = new ArrayList<>();
        rays.add(new Ray(origin, new Vector(-1, 1)));
        rays.add(new Ray(origin, new Vector(-1, -1)));
        rays.add(new Ray(origin, new Vector(1, 1)));
        rays.add(new Ray(origin, new Vector(1, -1)));

        ArrayList<Ray> acceptedRays = FieldOfView.limitView(rays, lefLimit, rightLimit);
        assertEquals(1, acceptedRays.size());
        Ray acceptedRay = acceptedRays.get(0);
        assertEquals(1, acceptedRay.getV().getX());
        assertEquals(-1, acceptedRay.getV().getY());
    }

    @Test
    void leftSmallerThanRightLimit2()
    {
        Vector origin = new Vector(5, 20);
        Vector lefLimit = new Vector(1, 0).add(origin);
        Vector rightLimit = new Vector(0, -1).add(origin);

        ArrayList<Ray> rays = new ArrayList<>();
        rays.add(new Ray(origin, new Vector(-1, 1).add(origin)));
        rays.add(new Ray(origin, new Vector(-1, -1).add(origin)));
        rays.add(new Ray(origin, new Vector(1, 1).add(origin)));
        rays.add(new Ray(origin, new Vector(1, -1).add(origin)));

        ArrayList<Ray> acceptedRays = FieldOfView.limitView(rays, lefLimit, rightLimit);
        assertEquals(1, acceptedRays.size());
        Ray acceptedRay = acceptedRays.get(0);
        assertEquals(1+5, acceptedRay.getV().getX());
        assertEquals(-1+20, acceptedRay.getV().getY());
    }

    @Test
    void leftBiggerThanRightLimit1()
    {
        Vector origin = new Vector(0, 0);
        Vector lefLimit = new Vector(1, -1);
        Vector rightLimit = new Vector(-1, -1);

        ArrayList<Ray> rays = new ArrayList<>();
        rays.add(new Ray(origin, new Vector(-1, 0)));
        rays.add(new Ray(origin, new Vector(1, 0)));
        rays.add(new Ray(origin, new Vector(0, 1)));
        rays.add(new Ray(origin, new Vector(0, -1)));

        ArrayList<Ray> acceptedRays = FieldOfView.limitView(rays, lefLimit, rightLimit);
        assertEquals(1, acceptedRays.size());
        Ray acceptedRay = acceptedRays.get(0);
        assertEquals(0, acceptedRay.getV().getX());
        assertEquals(-1, acceptedRay.getV().getY());
    }

    @Test
    void leftBiggerThanRightLimit2()
    {
        Vector origin = new Vector(-7, 38);
        Vector lefLimit = new Vector(1, -1).add(origin);
        Vector rightLimit = new Vector(-1, -1).add(origin);

        ArrayList<Ray> rays = new ArrayList<>();
        rays.add(new Ray(origin, new Vector(-1, 0).add(origin)));
        rays.add(new Ray(origin, new Vector(1, 0).add(origin)));
        rays.add(new Ray(origin, new Vector(0, 1).add(origin)));
        rays.add(new Ray(origin, new Vector(0, -1).add(origin)));

        ArrayList<Ray> acceptedRays = FieldOfView.limitView(rays, lefLimit, rightLimit);
        assertEquals(1, acceptedRays.size());
        Ray acceptedRay = acceptedRays.get(0);
        assertEquals(-7, acceptedRay.getV().getX());
        assertEquals(-1+38, acceptedRay.getV().getY());
    }

}
