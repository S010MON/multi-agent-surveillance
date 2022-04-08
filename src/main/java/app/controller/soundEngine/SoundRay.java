package app.controller.soundEngine;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import javafx.scene.paint.Color;

public class SoundRay extends Ray
{
    public SoundRay(Vector u, Vector v)
    {
        super(u, v);
        this.colour = Color.BLUE;
    }
}
