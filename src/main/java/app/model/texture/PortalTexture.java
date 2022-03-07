package app.model.texture;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class PortalTexture extends TextureImp
{
    public PortalTexture(Rectangle2D rectangle)
    {
        super(rectangle);
        this.color = Color.PURPLE;
    }
}
