package app.controller.linAlg;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;

/**
 * VectorSet provides a hash set of vectors where vectors are mapped to the
 * closest 1 decimal point.
 *
 * E.g. the vector v = [0.12, 0.467] will map to [0.1, 0.5]
 */
public class VectorSet extends HashSet<Vector>
{
    public VectorSet()
    {
        super();
    }

    public boolean add(Vector v)
    {
        double x  = new BigDecimal(v.getX()).setScale(1, RoundingMode.HALF_UP).doubleValue();
        double y  = new BigDecimal(v.getY()).setScale(1, RoundingMode.HALF_UP).doubleValue();
        return super.add(new Vector(x, y));
    }
}
