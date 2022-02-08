package app.model;

import app.model.boundary.BoundaryFactory;
import javafx.geometry.Rectangle2D;

public abstract class FurnitureFactory
{
    public static Furniture make(FurnitureType type, Rectangle2D rectangle)
    {
        Furniture furniture = new Furniture();
        furniture.setBoundaries(BoundaryFactory.make(type, rectangle));
    }
}
