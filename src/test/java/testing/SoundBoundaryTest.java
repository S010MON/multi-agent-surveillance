package testing;

import app.controller.linAlg.Vector;
import app.model.sound.SoundBoundaryImp;
import app.model.sound.SoundBoundary;
import app.controller.soundEngine.SoundRay;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SoundBoundaryTest {
    @Test void intersectionHitTest(){
        SoundBoundary sb = new SoundBoundaryImp(new Vector(0,0), new Vector(100, 0));
        SoundRay sr = new SoundRay(new Vector(50, -10), new Vector(50, 10));

        Vector intersectionPoint = sb.intersection(sr);

        assertEquals(50, intersectionPoint.getX());
        assertEquals(0, intersectionPoint.getY());
    }

    @Test void intersectionParallelTest(){
        SoundBoundary sb = new SoundBoundaryImp(new Vector(0,0), new Vector(100, 0));
        SoundRay sr = new SoundRay(new Vector(0, 1), new Vector(100, 1));

        Vector intersectionPoint = sb.intersection(sr);

        assertNull(intersectionPoint);
    }

    @Test void intersectionOutSideSegmentTest(){
        SoundBoundary sb = new SoundBoundaryImp(new Vector(0,0), new Vector(100, 0));
        SoundRay sr = new SoundRay(new Vector(200, -10), new Vector(200, 10));

        Vector intersectionPoint = sb.intersection(sr);

        assertNull(intersectionPoint);
    }

    @Test void onBoundaryHorizontalTest(){
        SoundBoundary sb = new SoundBoundaryImp(new Vector(0,0), new Vector(100, 0));

        Vector p1 = new Vector(50,0);
        Vector p2 = new Vector(0,0);
        Vector p3 = new Vector(100,0);
        Vector p4 = new Vector(50,1);
        Vector p5 = new Vector(-1,0);

        // p1,p2,p3 should be on the segment
        assertTrue(sb.onSegment(p1));
        assertTrue(sb.onSegment(p2));
        assertTrue(sb.onSegment(p3));

        // p4, p5 should be off the segment
        assertFalse(sb.onSegment(p4));
        assertFalse(sb.onSegment(p5));
    }

    @Test void onBoundaryVerticalTest(){
        SoundBoundary sb = new SoundBoundaryImp(new Vector(0,0), new Vector(0, 100));

        Vector p1 = new Vector(0,50);
        Vector p2 = new Vector(0,0);
        Vector p3 = new Vector(0,100);
        Vector p4 = new Vector(1,50);
        Vector p5 = new Vector(0,-1);

        // p1,p2,p3 should be on the segment
        assertTrue(sb.onSegment(p1));
        assertTrue(sb.onSegment(p2));
        assertTrue(sb.onSegment(p3));

        // p4, p5 should be off the segment
        assertFalse(sb.onSegment(p4));
        assertFalse(sb.onSegment(p5));
    }

    @Test void onBoundaryTiltedTest(){
        SoundBoundary sb = new SoundBoundaryImp(new Vector(0,0), new Vector(100, 100));

        Vector p1 = new Vector(50,50);
        Vector p2 = new Vector(0,0);
        Vector p3 = new Vector(100,100);
        Vector p4 = new Vector(50,1);
        Vector p5 = new Vector(-1,-1);

        // p1,p2,p3 should be on the segment
        assertTrue(sb.onSegment(p1));
        assertTrue(sb.onSegment(p2));
        assertTrue(sb.onSegment(p3));

        // p4, p5 should be off the segment
        assertFalse(sb.onSegment(p4));
        assertFalse(sb.onSegment(p5));
    }
}
