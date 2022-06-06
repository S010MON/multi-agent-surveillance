package testing;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Angle;
import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.Map;
import app.model.agents.Agent;
import app.model.boundary.Boundary;
import app.model.boundary.BoundaryImp;
import app.model.boundary.VisibleTransparentNonSolidBoundary;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphicsEngineTest
{
    @Test void hitTest_center()
    {
        Vector a = new Vector(10,20);
        Vector b = new Vector(20,20);
        Boundary bdy = new BoundaryImp(a,b);

        Vector u = new Vector(15,0);
        Vector v = new Vector(15,16);
        Ray ray = new Ray(u, v);

        assertTrue(bdy.isHit(ray));

        Vector exp = new Vector(15,20);
        Vector act = bdy.intersection(ray);
        assertEquals(exp.getX(), act.getX(), 0.001);
        assertEquals(exp.getY(), act.getY(), 0.001);
    }

    @Test void hitTest_endpoint()
    {
        Vector a = new Vector(10,20);
        Vector b = new Vector(20,20);
        Boundary bdy = new BoundaryImp(a,b);

        Vector u = new Vector(20,19);
        Vector v = new Vector(0,100);
        Ray ray = new Ray(u, v);

        assertTrue(bdy.isHit(ray));

        Vector exp = new Vector(20,20);
        Vector act = bdy.intersection(ray);
        assertEquals(exp.getX(), act.getX(), 0.3);
        assertEquals(exp.getY(), act.getY(), 0.3);
    }

    @Test void seeVisibleTransparentBoundary()
    {
        Vector a = new Vector(10,20);
        Vector b = new Vector(20,20);
        Boundary bdy = new VisibleTransparentNonSolidBoundary(a,b, FurnitureType.TARGET);

        Vector u = new Vector(20,19);
        Vector v = new Vector(0,100);
        Ray ray = new Ray(u, v);

        assertTrue(bdy.isHit(ray));

        Vector exp = new Vector(20,20);
        Vector act = bdy.intersection(ray);
        assertEquals(exp.getX(), act.getX(), 0.3);
        assertEquals(exp.getY(), act.getY(), 0.3);
    }

    @Test void seeMultipleVisibleTransparentBoundaries()
    {
        GraphicsEngine graphicsEngine = new GraphicsEngine();
        Settings settings = new Settings();
        Rectangle2D target1 = new Rectangle2D(10, 15, 10, 5);
        Rectangle2D target2 = new Rectangle2D(0, 95, 10, 10);
        Rectangle2D guardSpawn = new Rectangle2D(0, 0, 50, 50);
        settings.addFurniture(target1, FurnitureType.TARGET);
        settings.addFurniture(target2, FurnitureType.TARGET);
        settings.addFurniture(guardSpawn, FurnitureType.GUARD_SPAWN);
        settings.setNoOfGuards(1);
        settings.setNoOfIntruders(0);
        Map map = new Map(settings);


        Vector u = new Vector(20,19);
        Vector v = new Vector(0,100);
        Ray ray = new Ray(u, v);

        Agent agent = map.getAgents().get(0);
        agent.setDirection(ray.direction().scale(100).rotate(1));
        agent.updateLocation(u);

        agent.updateView(graphicsEngine.compute(map, agent));
        ArrayList<Ray> vision = agent.getView();
        List<Ray> raysWithRightAngle = Angle.raysWithAngle(ray.angle(), 0.01, vision);


        //TODO: fix it so that don't see yourself (already checks that in the seeing agent parts?
        //sees 2 boundaries of target1, so 3 total
        assertEquals(3, raysWithRightAngle.size());
    }
}
