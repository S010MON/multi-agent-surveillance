package app.model.soundFurniture;

import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundRay;
import app.model.soundBoundary.SoundBoundary;
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
