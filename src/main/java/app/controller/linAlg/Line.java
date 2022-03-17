package app.controller.linAlg;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Line
{
    private Vector a;
    private Vector b;

    public boolean liesOn(Vector v)
    {
        return between(a.getX(), b.getX(), v.getX()) && between(a.getY(), b.getY(), v.getY());
    }


    /**
     * @return true if x lies between a and b
     */
    private boolean between(double a, double b, double x)
    {
        return  (a <= x && x <= b) || (b <= x && x <= a);
    }
}
