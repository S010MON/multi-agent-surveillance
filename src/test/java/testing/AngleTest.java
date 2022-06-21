package testing;

import app.controller.linAlg.Angle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AngleTest
{
    @Test void angleNormalizationTest()
    {
        double a1 = -1;
        double a2 = -10;
        double a3 = 370;
        double a4 = 50;
        double a5 = 0;

        assertEquals(359.0, Angle.normalizeAngle(a1));
        assertEquals(350.0, Angle.normalizeAngle(a2));
        assertEquals(10.0, Angle.normalizeAngle(a3));
        assertEquals(50.0, Angle.normalizeAngle(a4));
        assertEquals(360.0, Angle.normalizeAngle(a5));
    }

    @Test void angleInRangeSimpleTest()
    {
        double u = 100;
        double l = 50;
        double v1 = 60;
        double v2 = 40;


        assertTrue(Angle.angleInRange(v1, u, l));
        assertFalse(Angle.angleInRange(v2, u, l));
    }

    @Test void angleInRangeHarderTest(){
        double u = -260;
        double l = 410;
        double v1 = 60;
        double v2 = 40;


        assertTrue(Angle.angleInRange(v1, u, l));
        assertFalse(Angle.angleInRange(v2, u, l));
    }

    @Test void angle0Case()
    {
        double u = 2;
        double l = -2;
        double v1 = 0;
        double v2 = 357;
        assertTrue(Angle.angleInRange(v1, u, l));
        assertFalse(Angle.angleInRange(v2, u, l));
    }
}
