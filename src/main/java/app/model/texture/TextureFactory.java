package app.model.texture;

import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;

public class TextureFactory
{
    public static Texture make(FurnitureType f, Rectangle2D rectangle)
    {
        switch (f)
        {
            case SHADE -> {return new Shade(rectangle);}
            case WALL -> {return new Wall(rectangle);}
            case GLASS -> {return new Glass(rectangle);}
            case PORTAL -> {return new Portal(rectangle);}
        }
        return null; // Redundant by design
    }
}
