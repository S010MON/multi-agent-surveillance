package app.model.soundBoundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundRay;
import javafx.scene.canvas.GraphicsContext;

public class TransmittingBoundary extends SoundBoundaryImp{
    public TransmittingBoundary(Vector a, Vector b)
    {
        super(a, b);
    }

    @Override
    public void draw(GraphicsContext gc){

    }
    
    public Vector intersection(SoundRay soundRay){ return null; }

    public boolean intersects(SoundRay soundRay){ return false; }

    public boolean onSegment(Vector point){ return false; }
}
