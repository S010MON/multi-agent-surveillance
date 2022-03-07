package app.model.furniture;

import app.controller.settings.SettingsObject;
import app.model.boundary.BoundaryFactory;
import app.model.texture.TextureFactory;
import javafx.geometry.Rectangle2D;

public abstract class FurnitureFactory
{
    public static Furniture make(SettingsObject object)
    {
        Furniture furniture = new FurnitureBase();
        furniture.addBoundaries(BoundaryFactory.make(object));
        furniture.setTexture(TextureFactory.make(object.getType(), object.getRect()));
        return furniture;
    }
}
