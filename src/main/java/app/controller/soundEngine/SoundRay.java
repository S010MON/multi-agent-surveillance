package app.controller.soundEngine;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class SoundRay extends Ray
{
    @Setter private boolean visible = true;
    @Getter private SoundRay parent;
    @Getter private int bounces;
    private ArrayList<SoundRay> children;

    public SoundRay(Vector u, Vector v)
    {
        super(u, v);
        this.colour = Color.rgb(0,0,255, 0.07);
        this.bounces = 0;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public SoundRay(Vector u, Vector v, int bounces)
    {
        super(u, v);
        this.colour = Color.rgb(0,0,255, 0.07);
        this.bounces = bounces;
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public SoundRay(Vector u, Vector v, int bounces, SoundRay parent)
    {
        super(u, v);
        this.colour = Color.rgb(0,0,255, 0.07);
        this.bounces = bounces;
        this.children = new ArrayList<>();
        this.parent = parent;
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        if(visible)
        {
            gc.setStroke(colour);
            gc.setLineWidth(LINE_WIDTH);
            gc.strokeLine(getU().getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                    getU().getY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                    getV().getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                    getV().getY() * Info.getInfo().zoom + Info.getInfo().offsetY);
        }
    }

    public String toString()
    {
        return "ang:" + angle() + " u:" + getU().toString() + " v:" + getV().toString() + " bounces:" + bounces;
    }
}
