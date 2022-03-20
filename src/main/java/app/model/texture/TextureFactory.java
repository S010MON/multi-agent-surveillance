package app.model.texture;

import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;

public class TextureFactory
{
    public static Texture make(FurnitureType f, Rectangle2D rectangle)
    {
        switch (f)
        {
            case SHADE -> {return new ShadeTexture(rectangle);}
            case WALL -> {return new WallTexture(rectangle);}
            case GLASS -> {return new GlassTexture(rectangle);}
            case PORTAL -> {return new PortalTexture(rectangle);}
            case SIREN -> {return new SoundSourceTexture(rectangle);}
        }
        return null; // Redundant by design
    }
}
