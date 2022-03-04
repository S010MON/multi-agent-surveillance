package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public interface SoundBoundary {
    ArrayList<Vector> getCorners();

    // for testing
    void draw(GraphicsContext gc);

    Vector intersection(SoundRay soundRay);

    boolean onSegment(Vector point);

    // should be the projection (the closest point to line) of a point onto the boundary (segment)
    Vector projectOnto(Vector point);
}
