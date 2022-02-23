package testing;

import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundBlockingWall;
import app.controller.soundEngine.SoundBoundary;
import app.controller.soundEngine.SoundRay;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SoundBoundaryTest {
    @Test void intersectionHitTest(){
        SoundBoundary sb = new SoundBlockingWall(new Vector(0,0), new Vector(100, 0));
        SoundRay sr = new SoundRay(new Vector(50, -10), new Vector(50, 10));

        Vector intersectionPoint = sb.intersection(sr);

        assertEquals(50, intersectionPoint.getX());
        assertEquals(0, intersectionPoint.getY());
    }

    @Test void intersectionParallelTest(){
        SoundBoundary sb = new SoundBlockingWall(new Vector(0,0), new Vector(100, 0));
        SoundRay sr = new SoundRay(new Vector(0, 1), new Vector(100, 1));

        Vector intersectionPoint = sb.intersection(sr);

        assertNull(intersectionPoint);
    }

    @Test void intersectionOutSideSegmentTest(){
        SoundBoundary sb = new SoundBlockingWall(new Vector(0,0), new Vector(100, 0));
        SoundRay sr = new SoundRay(new Vector(200, -10), new Vector(200, 10));

        Vector intersectionPoint = sb.intersection(sr);

        assertNull(intersectionPoint);
    }
}
