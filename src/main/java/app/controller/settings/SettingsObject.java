package app.controller.settings;

import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;
import javafx.geometry.Rectangle2D;
import lombok.Getter;

public class SettingsObject extends Rectangle2D
{
    @Getter private FurnitureType type;
    @Getter private Vector teleportTo;

    public SettingsObject(Rectangle2D rectangle, FurnitureType type)
    {
        super(rectangle.getMinX(),
                rectangle.getMinY(),
                rectangle.getWidth(),
                rectangle.getHeight());
        this.type = type;
        this.teleportTo = null;
    }

    public SettingsObject(Rectangle2D rectangle, Vector teleportTo)
    {
        super(rectangle.getMinX(),
                rectangle.getMinY(),
                rectangle.getWidth(),
                rectangle.getHeight());
        this.type = FurnitureType.PORTAL;
        this.teleportTo = teleportTo;
    }

    public Rectangle2D getRect()
    {
        return new Rectangle2D(getMinX(), getMinY(), getWidth(), getHeight());
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(type.label);
        sb.append(" = ");
        sb.append((int) getMinX()).append(" ");
        sb.append((int) getMinY()).append(" ");
        sb.append((int) getMaxX()).append(" ");
        sb.append((int) getMaxY());
        if(type == FurnitureType.PORTAL && teleportTo != null)
        {
            sb.append(" ");
            sb.append(teleportTo.getX()).append(" ");
            sb.append(teleportTo.getY());
        }
        return sb.toString();
    }
}
