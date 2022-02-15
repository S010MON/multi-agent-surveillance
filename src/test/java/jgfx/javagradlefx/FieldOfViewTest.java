package jgfx.javagradlefx;

import app.controller.FieldOfView;
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

}
