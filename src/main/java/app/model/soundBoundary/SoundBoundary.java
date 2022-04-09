package app.model.soundBoundary;

import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundRay;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public interface SoundBoundary
{
    ArrayList<Vector> getCorners();

    void draw(GraphicsContext gc);

    Vector intersection(SoundRay soundRay);

    boolean intersects(SoundRay soundRay);

    boolean onSegment(Vector point);
}
