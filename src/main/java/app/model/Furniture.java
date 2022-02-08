package app.model;

import app.model.boundary.Boundary;
import app.model.boundary.InvisibleBoundary;
import app.model.texture.Texture;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Furniture
{
    private ArrayList<InvisibleBoundary> boundaries;
    private Texture texture;

    public Furniture()
    {
        boundaries = new ArrayList<>();
        texture = null;
    }

    public void draw(GraphicsContext gc)
    {
        if(texture != null)
            texture.draw(gc);

        boundaries.stream()
                .forEach(e -> e.draw(gc));
    }

    public void setBoundaries(ArrayList<Boundary> boundaries)
    {
        boundaries.addAll(boundaries);
    }

    public ArrayList<InvisibleBoundary> getBoundaries()
    {
        return boundaries;
    }

    public void setTexture(Texture texture)
    {
        this.texture = texture;
    }
}
