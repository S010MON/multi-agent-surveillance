package app.model.texture;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class WallTexture extends TextureImp
{
    public WallTexture(Rectangle2D rectangle)
    {
        super(rectangle);
        this.color = Color.SANDYBROWN;
    }
}
