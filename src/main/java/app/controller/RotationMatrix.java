package app.controller;

public class RotationMatrix
{
    private double a1;
    private double a2;
    private double b1;
    private double b2;

    /**
     * a matrix in the form:
     * [ a1 a2 ]
     * [ b1 b2 ]
     */
    public RotationMatrix(double a1, double a2, double b1, double b2)
    {
        this.a1 = a1;
        this.a2 = a2;
        this.b1 = b1;
        this.b2 = b2;
    }

    public Vector dot(Vector v)
    {
        double x = (a2 * v.getY()) + (a1 * v.getX());
        double y = (b2 * v.getY()) + (b1 * v.getX());
        return new Vector(x, y);
    }
}
