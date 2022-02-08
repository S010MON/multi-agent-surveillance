package app.model.texture;

import app.model.Furniture;
import javafx.geometry.Rectangle2D;

public class TextureFactory
{
    public static Texture make(Furniture f, Rectangle2D rectangle)
    {
        switch (f)
        {
            case SHADE -> {return new Shade(rectangle);}
            case WALL -> {return new Wall(rectangle);}
        }
        return null; // Redundant by design
    }
}
