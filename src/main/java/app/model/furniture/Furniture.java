package app.model.furniture;

import app.model.boundary.Boundary;
import app.model.texture.Texture;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public interface Furniture
{
    void draw(GraphicsContext gc);

    void addBoundaries(ArrayList<Boundary> boundaries);

    ArrayList<Boundary> getBoundaries();

    void setTexture(Texture texture);
}
