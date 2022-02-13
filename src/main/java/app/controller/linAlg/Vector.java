package app.controller.linAlg;

public class Vector
{
    private double x;
    private double y;

    public Vector()
    {
        this.x = 0d;
        this.y = 0d;
    }

    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public Vector add(Vector other)
    {
        return new Vector(this.x + other.getX(), this.y + other.getY());
    }

    public Vector sub(Vector other)
    {
        return new Vector(this.x - other.getX(), this.y - other.getY());
    }

    public double cross(Vector other)
    {
        double a = (this.getY() * other.getX());
        double b = (this.getX() * other.getY());
        return a - b;
    }

    public double dot(Vector other)
    {
        return (this.getX() * other.getX()) + (this.getY() * other.getY());
    }

    public Vector scale(double scalar)
    {
        return new Vector(this.getX() * scalar, this.getY() * scalar);
    }

    public double dist(Vector vector)
    {
        double a = Math.pow((this.getX() - vector.getX()),2);
        double b = Math.pow((this.getY() - vector.getY()),2);
        return Math.sqrt(a + b);
    }

    public Vector normalise()
    {
        Vector v = new Vector(x, y);
        double length = v.length();
        Vector normV = v.scale(1/length);
        return normV;
    }

    public double length() 
    {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public Vector rotate(double degrees)
    {
        RotationMatrix M = new RotationMatrix(degrees);
        return M.dot(this);
    }


    /** Returns the clockwise angle from [0,1] direction */
    public double getAngle()
    {
        // + 90 to set the angle to north, negate angle to make it clockwise
        return (-Math.toDegrees(Math.atan2(y,x)) + 360.0 + 90.0) % 360.0;
    }

    public Vector copy()
    {
        return new Vector(x, y);
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Vector)
        {
            Vector v = (Vector) other;
            return this.getX() == v.getX() && this.getY() == v.getY();
        }
        return false;
    }

    public Integer vectorHashCode()
    {
        return this.hashCode();
    }
}
