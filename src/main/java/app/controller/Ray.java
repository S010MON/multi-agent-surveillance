package app.controller;

public class Ray
{
    private Vector u;
    private Vector v;

    public Ray(Vector u, Vector v)
    {
        this.u = u;
        this.v = v;
    }

    public Vector getU()
    {
        return u;
    }

    public Vector getV()
    {
        return v;
    }

    public double length()
    {
        double a = Math.pow((u.getX() + v.getX()),2);
        double b = Math.pow((u.getY() + v.getY()),2);
        return Math.sqrt(a + b);
    }

    // Needs work!
    public Ray rotate(double degrees)
    {
        Vector unitVector = v.sub(u);
        double cosA = Math.cos(Math.toRadians(degrees));
        double sinA = Math.sin(Math.toRadians(degrees));
        RotationMatrix M = new RotationMatrix(cosA, -sinA, sinA, cosA);
        Vector rotatedVector = M.dot(unitVector);
        Vector newV = u.add(rotatedVector);
        return new Ray(u, newV);
    }
}
