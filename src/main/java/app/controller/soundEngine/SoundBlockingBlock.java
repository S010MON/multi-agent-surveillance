package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class SoundBlockingBlock implements SoundFurniture{
    @Getter @Setter private ArrayList<SoundBoundary> soundBoundaries;
    private ArrayList<Vector> corners;

    public SoundBlockingBlock(Rectangle2D r){
        corners = new ArrayList<>();

        corners.add(new Vector(r.getMinX(), r.getMinY()));      // top left corner
        corners.add(new Vector(r.getMaxX(), r.getMinY()));      // top right corner
        corners.add(new Vector(r.getMaxX(), r.getMaxY()));      // lower right corner
        corners.add(new Vector(r.getMinX(), r.getMaxY()));      // lower left corner

        soundBoundaries = new ArrayList<>();
        soundBoundaries.add(new SoundBlockingWall(corners.get(0), corners.get(1)));
        soundBoundaries.add(new SoundBlockingWall(corners.get(1), corners.get(2)));
        soundBoundaries.add(new SoundBlockingWall(corners.get(2), corners.get(3)));
        soundBoundaries.add(new SoundBlockingWall(corners.get(3), corners.get(0)));
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
    public boolean hitsCorner(SoundRay soundRay){
       if(!corners.contains(soundRay.getEnd())) {
           return false;
       }

        for (SoundBoundary sb: soundBoundaries) {
            if(sb.getCorners().contains(soundRay.getEnd())){
                for (SoundBoundary remaining : soundBoundaries) {
                    if(remaining.getCorners().contains(soundRay.getEnd())){
                        continue;
                    }
                    if(remaining.intersection(soundRay) != null){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean isCorner(Vector pos) {
        return corners.contains(pos);
    }

    @Override
    public void draw(GraphicsContext gc) {

    }
}
