package app.model;

import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.model.boundary.Boundary;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Coverage
{
    @Getter private VectorSet vectors;
    private final  double sensitivity = 0.6;
    private double width;
    private double height;

    public Coverage(Map map)
    {
        System.out.println("Build coverage map ...");
        width = map.getWidth();
        height = map.getHeight();
        vectors = new VectorSet();

        for(double x = 0; x <= width; x++)
        {
            for(double y = 0; y <= height; y++)
            {
                Vector v = new Vector(x, y);
                if(furnitureAt(map, v))
                    vectors.add(v);
            }
        }
        System.out.print(" done");
    }

    public double percentSeen(VectorSet seen)
    {
        double percent = (double) seen.size() / (double) vectors.size();
        return new BigDecimal(percent).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private boolean furnitureAt(Map map, Vector vector)
    {
        for(Boundary b: map.getBoundaries())
        {
            if(b.isCrossed(vector, sensitivity))
                return true;
        }
        return false;
    }
}
