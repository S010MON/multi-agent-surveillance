package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Line;
import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;

public class BoundaryImp implements Boundary
{
    protected Vector a;
    protected Vector b;
    @Getter protected BoundaryType boundaryType;

    public BoundaryImp(Vector a, Vector b)
    {
        this.a = a;
        this.b = b;
        this.boundaryType = BoundaryType.VISIBLE_SOLID;
    }

    public BoundaryImp(Vector a, Vector b, BoundaryType boundaryType)
    {
        this.a = a;
        this.b = b;
        this.boundaryType = boundaryType;
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
}
