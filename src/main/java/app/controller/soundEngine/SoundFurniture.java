package app.controller.soundEngine;

import app.model.boundary.Boundary;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public interface SoundFurniture {
    void draw(GraphicsContext gc);

    void setSoundBoundaries(ArrayList<SoundBoundary> soundBoundaries);

    ArrayList<SoundBoundary> getSoundBoundaries();

    boolean intersectsAny(SoundRay soundRay);
}
