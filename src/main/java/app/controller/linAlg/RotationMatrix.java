package app.controller.linAlg;

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
    public RotationMatrix(double theta)
    {
        double cosA = Math.cos(Math.toRadians(theta));
        double sinA = Math.sin(Math.toRadians(theta));
        this.a1 = cosA;
        this.a2 = -sinA;
        this.b1 = sinA;
        this.b2 = cosA;
    }

    public Vector dot(Vector v)
    {
        double x = (a2 * v.getY()) + (a1 * v.getX());
        double y = (b2 * v.getY()) + (b1 * v.getX());
        return new Vector(x, y);
    }
}
