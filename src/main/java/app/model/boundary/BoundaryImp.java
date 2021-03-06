package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Line;
import app.controller.linAlg.Vector;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

public class BoundaryImp implements Boundary
{
    @Getter protected Vector a;
    @Getter protected Vector b;
    protected Color colour;

    public BoundaryImp(Vector a, Vector b)
    {
        this.a = a;
        this.b = b;
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        if(colour == null)
            return;

        gc.setStroke(colour);
        gc.strokeLine(a.getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                a.getY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                b.getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                b.getY() * Info.getInfo().zoom + Info.getInfo().offsetY);
    }

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
