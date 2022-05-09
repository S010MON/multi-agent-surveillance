package app.model.furniture;

import app.controller.linAlg.Vector;
import app.model.boundary.Boundary;
import app.model.boundary.SoundBoundary;
import app.model.texture.Texture;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import lombok.Getter;

public class FurnitureBase implements Furniture
{
    @Getter private ArrayList<Boundary> boundaries;
    @Getter private ArrayList<SoundBoundary> soundBoundaries;
    @Getter protected FurnitureType type;
    private Texture texture;

    public FurnitureBase(FurnitureType type)
    {
        this.type = type;
        boundaries = new ArrayList<>();
        soundBoundaries = new ArrayList<>();
        texture = null;
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        if(texture != null)
            texture.draw(gc);

        boundaries.forEach(e -> e.draw(gc));
        soundBoundaries.forEach(e -> e.draw(gc));
    }

    @Override
    public void addBoundaries(ArrayList<Boundary> boundaries)
    {
        this.boundaries.addAll(boundaries);
    }

    @Override
    public void addSoundBoundaries(ArrayList<SoundBoundary> soundBoundaries)
    {
        this.soundBoundaries.addAll(soundBoundaries);
    }

    @Override
    public boolean contains(Vector v)
    {
        return texture.contains(v);
    }

    @Override
    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }
}
