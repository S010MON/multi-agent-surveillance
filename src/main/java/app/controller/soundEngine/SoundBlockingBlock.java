package app.controller.soundEngine;

import app.controller.graphicsEngine.Ray;
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
    public boolean isBlocked(SoundRay soundRay) {
        // TODO distinguish 6 cases for sound rays passing thru furniture
        // ray start on outside part of the furniture boundary OK
        // ray ends on outside part of the furniture boundary OK
        // ray does not intersect furniture at all and does not start and end within the boundaries OK
        // ray starts and ends within furniture without intersecting BAD
        // ray intersects with boundary and ends within furniture BAD
        // ray intersects with boundary and passes through the other side BAD

        // TODO use new projection as a way of checking whether a ray is valid or not

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

    public boolean onOutline(Vector pos){
        for (SoundBoundary sb: soundBoundaries) {
            if(sb.onSegment(pos)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isInside(Vector pos) {
        if(onOutline(pos)){
            return false;
        }
        // https://www.geeksforgeeks.org/how-to-check-if-a-given-point-lies-inside-a-polygon/
        // checks if point is inside the bounds of the polygon, excluding(!!) the borders
        // horizontal ray to the right
        // notice that there is an assumption here that the map won't be larger than 10000 (hope that will not be significant)
        SoundRay comparisonRay = new SoundRay(pos, pos.add(new Vector(10000,0)));

        int count = 0;

        for (SoundBoundary sb: soundBoundaries) {
            if(sb.intersects(comparisonRay)) {
                count++;
            }
        }

        return count % 2 == 1;

    }
}
