package app.view.mapBuilder;

import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Setter;
import lombok.Getter;

@Setter
public class MbObject
{
    @Getter private FurnitureType type;
    @Getter private Vector vector;
    @Getter private Rectangle2D rect;
    @Getter private Double rotation;

    public MbObject(Rectangle2D rect, FurnitureType type)
    {
        this.type = type;
        this.rect = rect;
    }

    public void draw(GraphicsContext gc)
    {
        if(isFilled())
        {
            gc.setFill(getTypeColour());
            gc.fillRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
        }

        if(hasOutline())
        {
            gc.setStroke(getTypeOutline());
            gc.strokeRect(rect.getMinX(), rect.getMinY(), rect.getWidth(), rect.getHeight());
        }
    }

    private Color getTypeColour()
    {
        switch(type)
        {
            case WALL -> {return Color.SANDYBROWN;}
            case SHADE -> {return Color.LIGHTGRAY;}
            case GLASS -> {return Color.LIGHTBLUE;}
            case TOWER -> {return Color.OLIVE;}
            case PORTAL -> {return Color.OLIVEDRAB;}
            case GUARD_SPAWN -> {return Color.BLUE;}
            case INTRUDER_SPAWN -> {return Color.RED;}
            case TARGET -> {return Color.GOLD;}
        }
        return null;
    }

    private Color getTypeOutline()
    {
        switch(type)
        {
            case WALL, SHADE, TOWER -> {return Color.BLACK;}
            case PORTAL, GLASS -> {return Color.LIGHTGRAY;}
            case GUARD_SPAWN -> {return Color.BLUE;}
            case INTRUDER_SPAWN -> {return Color.RED;}
            case TARGET -> {return Color.GOLD;}
        }
        return null;
    }

    private boolean isFilled()
    {
        switch(type)
        {
            case WALL, SHADE, GLASS, TOWER, PORTAL -> {return true;}
            case GUARD_SPAWN, INTRUDER_SPAWN, TARGET -> {return false;}
        }
        return false;
    }

    private boolean hasOutline()
    {
        switch(type)
        {
            case WALL, INTRUDER_SPAWN, TARGET, TOWER, PORTAL, GUARD_SPAWN -> {return true;}
            case SHADE, GLASS -> {return false;}
        }
        return false;
    }
}
