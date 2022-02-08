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

    public double length() {
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
        double abs_y = Math.abs(y);
        double abs_x = Math.abs(x);
        double a = 0;

        // Q1
        if(x >= 0 && y >= 0 && abs_y >= abs_x)
            a = Math.toDegrees(Math.atan(abs_x / abs_y));
        else if(x >= 0 && y >= 0 && abs_y < abs_x)
            a = 90 - Math.toDegrees(Math.atan(abs_y / abs_x));
        // Q2
        else if(x >= 0 && y < 0 && abs_y >= abs_x)
            a = 90 + Math.toDegrees(Math.atan(abs_y / abs_x));
        else if(x >= 0 && y < 0 && abs_y < abs_x)
            a = 180 - Math.toDegrees(Math.atan(abs_x / abs_y));
        // Q3
        else if(x < 0 && y < 0 && abs_y >= abs_x)
            a = 180 + Math.toDegrees(Math.atan(abs_x / abs_y));
        else if(x < 0 && y < 0 && abs_y < abs_x)
            a = 270 - Math.toDegrees(Math.atan(abs_y / abs_x));
        // Q4
        else if(x < 0 && y >= 0 && abs_x >= abs_y)
            a = 270 + Math.toDegrees(Math.atan(abs_y / abs_x));
        else if(x < 0 && y >= 0 && abs_y < abs_x)
            a = 180 - Math.toDegrees(Math.atan(abs_x / abs_y));

        return a;
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
}
