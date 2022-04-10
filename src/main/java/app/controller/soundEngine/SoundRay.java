package app.controller.soundEngine;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import javafx.scene.paint.Color;
import lombok.Getter;

public class SoundRay extends Ray
{
    @Getter private int bounces;

    public SoundRay(Vector u, Vector v)
    {
        super(u, v);
        this.colour = Color.BLUE;
        this.bounces = 0;
    }

    public SoundRay(Vector u, Vector v, int bounces)
    {
        super(u, v);
        this.colour = Color.BLUE;
        this.bounces = bounces;
    }

    public String toString()
    {
        return "ang:" + angle() + " u:" + getU().toString() + " v:" + getV().toString() + " bouuces:" + bounces;
    }
}
