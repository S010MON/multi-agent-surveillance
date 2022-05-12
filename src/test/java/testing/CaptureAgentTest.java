package testing;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.model.Type;
import app.model.agents.Capture.Capture;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CaptureAgentTest
{
    Vector agent1Pos = new Vector(10, 10);
    Vector agent1Dir = new Vector(1, 0);

    Capture agent1 = new Capture(agent1Pos, agent1Dir, 1, Type.GUARD);

    @Test void testSeeingIntruder()
    {
        ArrayList<Ray> testView = new ArrayList<>();
        testView.add(new Ray(new Vector(), new Vector(), Type.INTRUDER));
        agent1.setView(testView);
        assertTrue(agent1.isIntruderSeen());
    }

    @Test void testNotSeeingIntruder()
    {
        ArrayList<Ray> testView = new ArrayList<>();
        testView.add(new Ray(new Vector(), new Vector(), null));
        agent1.setView(testView);
        assertFalse(agent1.isIntruderSeen());
    }

    @Test void testUpdatingBeliefs()
    {
        VectorSet bSet = new VectorSet();
        bSet.add(new Vector(50, 50));
        agent1.setBeliefSet(bSet);
        agent1.updateBeliefSet();
        VectorSet updatedBSet = agent1.getBeliefSet();

        ArrayList<Vector> list = new ArrayList<>(updatedBSet);

        assertTrue(list.contains(new Vector(50, 60)));
        assertTrue(list.contains(new Vector(40, 50)));
        assertTrue(list.contains(new Vector(60, 50)));
        assertTrue(list.contains(new Vector(50, 40)));
    }

    @Test void testRemovingVisibleLocations()
    {
        VectorSet bSet = new VectorSet();
        bSet.add(agent1Pos);
        agent1.setBeliefSet(bSet);

        ArrayList<Ray> testView = new ArrayList<>();
        testView.add(new Ray(agent1Pos, new Vector(agent1Pos.getX(), agent1Pos.getY() + 20), null));
        agent1.setView(testView);

        agent1.updateBeliefSet();
        VectorSet updatedBSet = agent1.getBeliefSet();
        ArrayList<Vector> list = new ArrayList<>(updatedBSet);

        assertTrue(list.contains(new Vector(20, 10)));
        assertTrue(list.contains(new Vector(0, 10)));
        assertTrue(list.contains(new Vector(10, 0)));
    }

    @Test void testFindingTarget()
    {
        VectorSet bSet = new VectorSet();
        bSet.add(new Vector(50, 50));
        agent1.setBeliefSet(bSet);
        Vector target = agent1.findTarget();

        assertEquals(target, new Vector(50, 50));

        bSet.clear();
        bSet.add(new Vector(10, 10));
        bSet.add(new Vector(10, 20));
        bSet.add(new Vector(20, 10));
        bSet.add(new Vector(20, 20));
        bSet.add(new Vector(13, 13));
        agent1.setBeliefSet(bSet);
        Vector target2 = agent1.findTarget();
        assertEquals(target2, new Vector(13, 13));
    }
}