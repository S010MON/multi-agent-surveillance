package app.view;

import app.controller.Beam;
import app.controller.Vector;
import app.model.Agent;
import app.model.MapTemp;
import app.model.Placeable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Renderer extends Canvas
{
    private MapTemp map;
    public Renderer(MapTemp map, int width, int height)
    {
        super(width, height);
        this.map = map;
        render();
    }

    public void render()
    {
        GraphicsContext gc = this.getGraphicsContext2D();

        for(Beam r: map.getBeams())
        {
            r.draw(gc);
        }

        for(Placeable p: map.getObjects())
        {
            p.draw(gc);
        }

        for (Agent a: map.getAgents())
        {
            for(Beam r: a.getView())
            {
                r.draw(gc);
            }
            a.draw(gc);
        }
    }


}
