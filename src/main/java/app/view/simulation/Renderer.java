package app.view.simulation;

import app.controller.graphicsEngine.Ray;
import app.model.Map;
import app.view.ScreenSize;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Renderer extends Canvas
{
    private Map map;
    private Color backgroundColour;
    private double zoomRate = 0.2d;
    private Point2D click;

    public Renderer(Map map)
    {
        super(ScreenSize.width, ScreenSize.height);
        this.map = map;
        backgroundColour = Color.WHITE;

        setOnMousePressed(this::mousePressed);
        setOnMouseReleased(this::mouseReleased);
        setOnMouseDragged(this::mouseDragged);
        setOnScroll(this::handleScroll);

        render();
    }

    public void render()
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        drawBackground(gc);

        // Draw map boundary
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3.0);
        gc.strokeRect(3, 3, map.getSettings().getWidth(), map.getSettings().getHeight());

        map.drawIntruderSpawn(gc);
        map.drawGuardSpawn(gc);
        map.getFurniture().forEach(e -> e.draw(gc));
        map.getAgents().forEach(e -> drawRays(gc, e.getView()));
        map.getAgents().forEach(e -> e.draw(gc));
    }

    private void drawRays(GraphicsContext gc, ArrayList<Ray> rays)
    {
        rays.forEach(e -> e.draw(gc));
    }

    private void drawBackground(GraphicsContext gc)
    {
        gc.setFill(backgroundColour);
        gc.fillRect(0,0,getWidth(), getHeight());
    }

    private void handleScroll(ScrollEvent e)
    {
        double dy = e.getDeltaY();
        if(dy > 0)
            Info.getInfo().zoom += zoomRate;
        else if(dy < 0)
            Info.getInfo().zoom -= zoomRate;
        else if(dy < 0)
        {
            double dZoom = Info.getInfo().zoom - zoomRate;
            if(dZoom > 0)
                Info.getInfo().zoom += zoomRate;
        }
        render();
    }

    private void mousePressed(MouseEvent e)
    {
        click = new Point2D(e.getX(), e.getY());
        System.out.println("click = " + click);
    }

    private void mouseReleased(MouseEvent e)
    {
        click = null;
        System.out.println("click = null");
    }

    private void mouseDragged(MouseEvent e)
    {
        if(click != null)
        {
            double dx = e.getX() - click.getX();
            double dy = e.getY() - click.getY();
            click = new Point2D(e.getX(), e.getY());
            Info.getInfo().moveX(dx);
            Info.getInfo().moveY(dy);
            render();
        }
    }
}
