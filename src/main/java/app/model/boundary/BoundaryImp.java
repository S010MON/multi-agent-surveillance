package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Line;
import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;

public class BoundaryImp implements Boundary
{
    protected Vector a;
    protected Vector b;
    protected BoundaryType type;

    public BoundaryImp(Vector a, Vector b)
    {
        this.a = a;
        this.b = b;
        this.type = BoundaryType.VISIBLE_SOLID;
    }

    public BoundaryImp(Vector a, Vector b, BoundaryType type)
    {
        this.a = a;
        this.b = b;
        this.type = type;
    }

    @Override
    public void draw(GraphicsContext gc) {}

    @Override
    public boolean isHit(Ray ray)
    {
        return Intersection.hasIntersection(a,b, ray);
    }

    @Override
    public Vector intersection(Ray ray)
    {
        return Intersection.findIntersection(a,b, ray);
    }

    @Override
    public boolean isCrossed(Vector startPoint, Vector endPoint)
    {
        return Intersection.hasIntersection(startPoint,endPoint,a,b);
    }

    @Override
    public boolean isCrossed(Vector centre, double radius)
    {
        if(!Intersection.hasIntersection(a, b, centre, radius))
            return false;

        Line line = new Line(a, b);
        Vector intersection = Intersection.findIntersection(a, b, centre, radius);
        return line.liesOn(intersection);
    }

    @Override
    public Vector getTeleport()
    {
        return null;
    }

    public boolean isSolid()
    {
        return BoundaryType.isSolid(type);
    }

    public boolean isVisible()
    {
        return BoundaryType.isVisible(type);
    }

    public boolean isTransparent()
    {
        return BoundaryType.isTransparent(type);
    }
}
