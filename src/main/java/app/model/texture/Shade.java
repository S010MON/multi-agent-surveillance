package app.model.texture;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class Shade extends TextureImp
{
    public Shade(Rectangle2D rectangle)
    {
        super(rectangle);
        this.color = Color.LIGHTGRAY;
    }
}
