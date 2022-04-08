package app.model.furniture;

import app.model.boundary.Boundary;
import app.model.soundBoundary.SoundBoundary;
import app.model.texture.Texture;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public interface Furniture
{
    void draw(GraphicsContext gc);

    void addBoundaries(ArrayList<Boundary> boundaries);

    void addSoundBoundaries(ArrayList<SoundBoundary> soundBoundaries);

    ArrayList<Boundary> getBoundaries();

    void setTexture(Texture texture);
}
