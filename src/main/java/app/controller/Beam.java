package app.controller;

public class Beam
{
    private Vector u;
    private Vector v;

    public Beam(Vector u, Vector v)
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

    public Beam rotate(double degrees)
    {
        Vector unitVector = v.sub(u);
        Vector rotatedVector = unitVector.rotate(degrees);
        Vector newVector = u.add(rotatedVector);
        return new Beam(u, newVector);
    }
}
