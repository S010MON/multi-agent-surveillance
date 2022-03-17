package app.model;

import app.controller.linAlg.Vector;
import app.model.boundary.Boundary;
import lombok.Getter;

public class CoverageMap
{
    private final double pixelSize = 1d;
    private final  double sensitivity = 0.6;
    @Getter private boolean[][] pixelMap;
    private double realWidth;
    private double realHeight;
    private int pxWidth;
    private int pxHeight;

    public CoverageMap(Map map)
    {
        realWidth = map.getWidth();
        realHeight = map.getHeight();

        pxWidth =  (int) (realWidth / pixelSize);
        pxHeight = (int) (realHeight / pixelSize);
        pixelMap = new boolean[pxWidth][pxHeight];

        for(int i = 1; i <= pxWidth; i++)
        {
            double x = (i * pixelSize) - (pixelSize / 2);

            for(int j = 1; j <= pxHeight; j++)
            {
                double y = (j * pixelSize) - (pixelSize / 2);

                Vector v = new Vector(x, y);
                pixelMap[i -1][j -1] = furnitureAt(map, v);
            }
        }
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
