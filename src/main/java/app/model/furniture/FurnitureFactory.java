package app.model.furniture;

import app.controller.linAlg.Vector;
import app.model.boundary.BoundaryFactory;
import app.model.texture.TextureFactory;
import javafx.geometry.Rectangle2D;

public abstract class FurnitureFactory
{
    public static Furniture make(FurnitureType type, Rectangle2D rectangle)
    {
        Furniture furniture = new FurnitureBase();
        furniture.addBoundaries(BoundaryFactory.make(type, rectangle));
        furniture.setTexture(TextureFactory.make(type, rectangle));
        return furniture;
    }

    public static Furniture make(FurnitureType type, Rectangle2D rectangle, Vector teleportTo, double teleportRotation)
    {
        Portal furniture = new Portal();
        furniture.setBoundaries(BoundaryFactory.make(type, rectangle));
        furniture.setTexture(TextureFactory.make(type, rectangle));
        furniture.setTeleportTo(teleportTo);
        furniture.setTeleportRotation(teleportRotation);
        return furniture;
    }
}
