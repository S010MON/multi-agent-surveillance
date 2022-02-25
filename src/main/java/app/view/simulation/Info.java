package app.view.simulation;

import lombok.Getter;
import lombok.Setter;

public class Info
{
    private static Info instance;
    @Getter @Setter public double offsetX;
    @Getter @Setter public double offsetY;
    @Getter @Setter public double zoom;

    private Info()
    {
        offsetX = 0;
        offsetY = 0;
        zoom = 1;
    }

    public static Info getInfo()
    {
        if(instance == null)
            instance = new Info();
        return instance;
    }

    public void moveX(double dx)
    {
        getInfo().offsetX += dx;
    }

    public void moveY(double dy)
    {
        getInfo().offsetY += dy;
    }
}
