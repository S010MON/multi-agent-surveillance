package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class SoundBlockingBlock implements SoundFurniture{
    @Getter @Setter private ArrayList<SoundBoundary> soundBoundaries;

    public SoundBlockingBlock(Rectangle2D r){
        Vector[] corners = cornersOf(r);

        soundBoundaries = new ArrayList<>();
        soundBoundaries.add(new SoundBlockingWall(corners[0], corners[1]));
        soundBoundaries.add(new SoundBlockingWall(corners[1], corners[2]));
        soundBoundaries.add(new SoundBlockingWall(corners[2], corners[3]));
        soundBoundaries.add(new SoundBlockingWall(corners[3], corners[0]));
    }

    private Vector[] cornersOf(Rectangle2D r)
    {
        Vector[] corners = new Vector[4];
        corners[0] = new Vector(r.getMinX(), r.getMinY());      // top left corner
        corners[1] = new Vector(r.getMaxX(), r.getMinY());      // top right corner
        corners[2] = new Vector(r.getMaxX(), r.getMaxY());      // lower right corner
        corners[3] = new Vector(r.getMinX(), r.getMaxY());      // lower left corner
        return corners;
    }

    @Override
    public boolean intersectsAny(SoundRay soundRay) {
        for (SoundBoundary sb: soundBoundaries) {
            if(sb.intersection(soundRay) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(GraphicsContext gc) {

    }
}
