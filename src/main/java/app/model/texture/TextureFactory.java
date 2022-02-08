package app.model.texture;

import app.model.Furniture;
import javafx.scene.shape.Rectangle;

public class TextureFactory
{
    public static Texture make(Furniture f, Rectangle rectangle)
    {
        switch (f)
        {
            case SHADE -> {return new Shade(rectangle);}
            case WALL -> {return new Wall(rectangle);}
        }
        return null; // Redundant by design
    }
}
