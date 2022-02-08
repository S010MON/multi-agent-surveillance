package app.model.texture;

import javafx.scene.canvas.GraphicsContext;

public interface Texture
{
    default void draw(GraphicsContext gc)
    {
        // Do nothing
    }
}
