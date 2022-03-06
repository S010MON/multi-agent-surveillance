package testing;

import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundBlockingBlock;
import app.controller.soundEngine.SoundFurniture;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SoundFurnitureTest {
    @Test public void isInsideTest(){
        SoundFurniture soundFurniture = new SoundBlockingBlock(new Rectangle2D(0,0,10,10));

        Vector p1 = new Vector(1,1);
        Vector p2 = new Vector(2,5);
        Vector p3 = new Vector(5,5);

        assertTrue(soundFurniture.isInside(p1));
        assertTrue(soundFurniture.isInside(p2));
        assertTrue(soundFurniture.isInside(p3));
    }

    @Test public void notIsInsideTest(){
        SoundFurniture soundFurniture = new SoundBlockingBlock(new Rectangle2D(0,0,10,10));

        Vector p1 = new Vector(11,1);
        Vector p2 = new Vector(-1,1);
        Vector p3 = new Vector(1,-1);

        assertFalse(soundFurniture.isInside(p1));
        assertFalse(soundFurniture.isInside(p2));
        assertFalse(soundFurniture.isInside(p3));
    }

    @Test public void onOutlineTest(){
        SoundFurniture soundFurniture = new SoundBlockingBlock(new Rectangle2D(0,0,10,10));

        Vector p1 = new Vector(0,1);
        Vector p2 = new Vector(1,0);

        assertTrue(soundFurniture.onOutline(p1));
        assertTrue(soundFurniture.onOutline(p2));


        Vector p3 = new Vector(1,1);
        Vector p4 = new Vector(1,-1);

        assertFalse(soundFurniture.onOutline(p3));
        assertFalse(soundFurniture.onOutline(p4));
    }

    @Test public void isInsideOutlineTest(){
        SoundFurniture soundFurniture = new SoundBlockingBlock(new Rectangle2D(0,0,10,10));

        Vector p1 = new Vector(0,1);
        Vector p2 = new Vector(1,0);
        Vector p3 = new Vector(10,1);
        Vector p4 = new Vector(1,10);
        Vector p5 = new Vector(0,0);
        Vector p6 = new Vector(10,10);

        assertFalse(soundFurniture.isInside(p1));
        assertFalse(soundFurniture.isInside(p2));
        assertFalse(soundFurniture.isInside(p3));
        assertFalse(soundFurniture.isInside(p4));
        assertFalse(soundFurniture.isInside(p5));
        assertFalse(soundFurniture.isInside(p6));
    }
}
