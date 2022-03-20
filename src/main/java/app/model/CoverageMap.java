package app.model;

import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.model.boundary.Boundary;
import lombok.Getter;

public class CoverageMap
{
    @Getter private VectorSet vectors;
    private final  double sensitivity = 0.6;
    private double width;
    private double height;

    public CoverageMap(Map map)
    {
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
    }

    public double percentSeen(VectorSet seen)
    {
        return (double) seen.size() / (double) vectors.size();
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
