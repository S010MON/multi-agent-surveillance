package app.view;

import app.controller.Ray;
import app.controller.Vector;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class Renderer extends Canvas
{
    public ArrayList<Ray> rays = new ArrayList<>(); // This will be removed and replaced with the Map

    public Renderer(int width, int height)
    {
        super(width, height);
        createRays();
        render();
    }

    public void render()
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(100,100,10,10);
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        for(Ray r: rays)
        {
            gc.strokeLine(r.getU().getX(), r.getU().getY(), r.getV().getX(), r.getV().getY());
        }
    }

    // Temp method to create a set of rays - GraphicsEngine should generate this
    private void createRays()
    {
        Vector origin = new Vector(100,100);
        rays.add(new Ray(origin, new Vector(210, 110)));
        rays.add(new Ray(origin, new Vector(310, 210)));
        rays.add(new Ray(origin, new Vector(410, 310)));
        rays.add(new Ray(origin, new Vector(510, 410)));
        rays.add(new Ray(origin, new Vector(610, 510)));
        rays.add(new Ray(origin, new Vector(710, 610)));
    }
}
