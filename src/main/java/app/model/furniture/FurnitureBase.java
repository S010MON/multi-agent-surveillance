package app.model.furniture;

import app.model.boundary.Boundary;
import app.model.soundBoundary.SoundBoundary;
import app.model.texture.Texture;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class FurnitureBase implements Furniture
{
    private ArrayList<Boundary> boundaries;
    private ArrayList<SoundBoundary> soundBoundaries;
    private Texture texture;

    public FurnitureBase()
    {
        boundaries = new ArrayList<>();
        soundBoundaries = new ArrayList<>();
        texture = null;
    }

    public void draw(GraphicsContext gc)
    {
        if(texture != null)
            texture.draw(gc);

        boundaries.forEach(e -> e.draw(gc));
    }

    public void addBoundaries(ArrayList<Boundary> boundaries)
    {
        this.boundaries.addAll(boundaries);
    }

    public void addSoundBoundaries(ArrayList<SoundBoundary> soundBoundaries)
    {
        this.soundBoundaries.addAll(soundBoundaries);
    }

    public ArrayList<Boundary> getBoundaries()
    {
        return boundaries;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }
}
