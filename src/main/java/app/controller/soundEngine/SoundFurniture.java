package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public interface SoundFurniture {
    void draw(GraphicsContext gc);

    void setSoundBoundaries(ArrayList<SoundBoundary> soundBoundaries);

    ArrayList<SoundBoundary> getSoundBoundaries();

    boolean intersects(SoundRay soundRay);

    boolean hitsCorner(SoundRay soundRay);

    boolean onOutline(Vector pos);

    boolean isInside(Vector pos);
}
