package app.model.texture;

import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;

public interface Texture
{
    void draw(GraphicsContext gc);

    boolean contains(Vector v);
}
