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
    public boolean intersects(SoundRay soundRay) {

        // first check if any of the endpoints are inside the shape
        if(isInside(soundRay.getStart()) || isInside(soundRay.getEnd())) {
            return true;
        }

        // second, check if both points are on the outline

        if(onOutline(soundRay.getStart()) && onOutline(soundRay.getEnd())){
            // if both points are on the same line segment, it's fine, no intersection
            for (SoundBoundary sb: soundBoundaries) {
                if(sb.onSegment(soundRay.getStart()) && sb.onSegment(soundRay.getEnd())){
                    return false;
                }
            }
            // being here means the ray passes through the shape
            return true;
        }

        // the start could be on the outline
        if(onOutline(soundRay.getStart())){
            // ignore all the segments that contain the start point and check intersections with the remaining
            for (SoundBoundary sb: soundBoundaries) {
                if(sb.intersects(soundRay) && !sb.onSegment(soundRay.getStart())){
                    return true;
                }
            }
            return false;
        }

        // same idea for the end point
        if(onOutline(soundRay.getEnd())){
            for (SoundBoundary sb: soundBoundaries) {
                if(sb.intersects(soundRay) && !sb.onSegment(soundRay.getEnd())){
                    return true;
                }
            }
            return false;
        }

        // and here we check for intersections that pass through both sides of the shape
        for (SoundBoundary sb: soundBoundaries) {
            if(sb.intersects(soundRay)){
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
