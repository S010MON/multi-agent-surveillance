package jgfx.javagradlefx;

import app.controller.graphicsEngine.Ray;
import app.controller.graphicsEngine.RayScatter;
import app.controller.linAlg.Vector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RayScatterTest
{
    @Test void testAngle360()
    {
        ArrayList<Double> exp = new ArrayList<>();
        for(int i = 0; i < 360; i++)
        {
            exp.add(Double.valueOf(i));
        }

        ArrayList<Ray> act = RayScatter.angle360(new Vector());

        for(int i = 0; i < act.size(); i++)
        {
            assertEquals(exp.get(i), act.get(i).angle());
        }
    }
}
