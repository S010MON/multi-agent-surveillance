package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;

public class Flag implements Boundary
{
    @Getter protected Vector position;
    @Getter protected double radius;
    @Getter protected BoundaryType boundaryType = BoundaryType.VISIBLE_TRANSPARENT;

    public Flag(Vector position, double radius)
    {
        this.position = position;
        this.radius = radius;
    }

    @Override public void draw(GraphicsContext gc)
    {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3.0);
        gc.setFill(Color.YELLOW);

        double x = (position.getX()-(radius/2)) * Info.getInfo().zoom + Info.getInfo().offsetX;
        double y = (position.getY()-(radius/2)) * Info.getInfo().zoom + Info.getInfo().offsetY;

        gc.fillOval(x, y, radius, radius);
        gc.strokeOval(x , y, radius, radius);
    }

    @Override
    public boolean isHit(Ray ray)
    {
        return Intersection.hasIntersection(ray, position, radius);
    }

    @Override public Vector intersection(Ray ray)
    {
        return Intersection.findIntersection(ray, position, radius);
    }

    @Override public boolean isCrossed(Vector startPoint, Vector endPoint)
    {
        return false;
    }

    @Override public boolean isCrossed(Vector centre, double radius)
    {
        return false;
    }

    @Override public Vector getTeleport()
    {
        return null;
    }
}
