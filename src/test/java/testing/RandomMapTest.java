package testing;

import app.controller.linAlg.Line;
import app.controller.linAlg.Vector;
import app.controller.settings.RandomSettingsGenerator;
import javafx.geometry.Rectangle2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomMapTest
{
    @Test void testOverlapTrue()
    {
        Rectangle2D r1 = new Rectangle2D(150, 150, 100, 100);
        Rectangle2D r2 = new Rectangle2D(200, 200, 100, 100);

        assertTrue(RandomSettingsGenerator.checkOverlap(r1, r2));

        r1 = new Rectangle2D(100, 100, 100, 100);
        r2 = new Rectangle2D(75, 125, 50, 50);

        assertTrue(RandomSettingsGenerator.checkOverlap(r1, r2));

        r1 = new Rectangle2D(100, 100, 100, 100);
        r2 = new Rectangle2D(100, 80, 20, 22);

        assertTrue(RandomSettingsGenerator.checkOverlap(r1, r2));
    }

    @Test void testOverlapFalse()
    {
        Rectangle2D r1 = new Rectangle2D(200, 200, 20, 20);
        Rectangle2D r2 = new Rectangle2D(150, 150, 20, 20);

        assertFalse(RandomSettingsGenerator.checkOverlap(r1, r2));
    }

    @Test void testWithinTrue()
    {
        Rectangle2D r1 = new Rectangle2D(100, 100, 100, 100);
        Rectangle2D r2 = new Rectangle2D(125, 125, 50, 50);

        assertTrue(RandomSettingsGenerator.checkWithin(r1, r2));
    }

    @Test void testWithinFalse()
    {
        Rectangle2D r1 = new Rectangle2D(100, 100, 100, 100);
        Rectangle2D r2 = new Rectangle2D(0, 0, 50, 50);

        assertFalse(RandomSettingsGenerator.checkWithin(r1, r2));
    }

    @Test void testBlockingTrue()
    {
        Rectangle2D r1 = new Rectangle2D(100, 100, 100, 100);

        RandomSettingsGenerator.setLineOfClarity1(new Line(new Vector(0, 0), new Vector(120, 120)));
        RandomSettingsGenerator.setLineOfClarity2(new Line(new Vector(0, 0), new Vector(20, 20)));

        assertTrue(RandomSettingsGenerator.checkBlocking(r1));

        RandomSettingsGenerator.setLineOfClarity1(new Line(new Vector(0, 0), new Vector(20, 20)));
        RandomSettingsGenerator.setLineOfClarity2(new Line(new Vector(0, 0), new Vector(120, 120)));

        assertTrue(RandomSettingsGenerator.checkBlocking(r1));

        RandomSettingsGenerator.setLineOfClarity1(new Line(new Vector(150, 150), new Vector(300, 300)));
        RandomSettingsGenerator.setLineOfClarity2(new Line(new Vector(0, 0), new Vector(120, 120)));

        assertTrue(RandomSettingsGenerator.checkBlocking(r1));
    }

    @Test void testBlockingFalse()
    {
        Rectangle2D r1 = new Rectangle2D(100, 100, 100, 100);

        RandomSettingsGenerator.setLineOfClarity1(new Line(new Vector(20, 20), new Vector(40, 40)));
        RandomSettingsGenerator.setLineOfClarity2(new Line(new Vector(0, 0), new Vector(20, 20)));

        assertFalse(RandomSettingsGenerator.checkBlocking(r1));
    }
}
