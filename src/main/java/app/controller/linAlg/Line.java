package app.controller.linAlg;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Line
{
    public Vector a;
    public Vector b;

    public boolean liesOn(Vector v)
    {
        return between(a.getX(), b.getX(), v.getX()) && between(a.getY(), b.getY(), v.getY());
    }

    public double angle()
    {
        Vector v = b.sub(a);
        return v.getAngle();
    }


    /**
     * @return true if x lies between a and b
     */
    private boolean between(double a, double b, double x)
    {
        return  (a <= x && x <= b) || (b <= x && x <= a);
    }
}
