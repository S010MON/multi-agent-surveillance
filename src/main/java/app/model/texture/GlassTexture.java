package app.model.texture;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class GlassTexture extends TextureImp
{
    public GlassTexture(Rectangle2D rectangle)
    {
        super(rectangle);
        this.color = Color.LIGHTBLUE;
    }
}
