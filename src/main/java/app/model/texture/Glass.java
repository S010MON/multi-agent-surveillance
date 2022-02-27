package app.model.texture;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class Glass extends TextureImp
{
    public Glass(Rectangle2D rectangle)
    {
        super(rectangle);
        this.color = Color.LIGHTBLUE;
    }
}
