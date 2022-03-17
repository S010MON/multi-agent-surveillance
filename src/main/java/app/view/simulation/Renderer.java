package app.view.simulation;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.Trail;
import app.model.agents.Agent;
import app.view.ScreenSize;
import app.view.agentView.AgentView;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Renderer extends Canvas
{
    private Map map;
    private Color backgroundColour, outlineColor;
    private double zoomRate = 0.2d;
    private Point2D click;
    private PriorityQueue<Trail> trails;

    public Renderer(Map map)
    {
        super(ScreenSize.width, ScreenSize.height);
        this.map = map;
        this.trails = new PriorityQueue<>();
        this.setInitialZoom(map.getSettings().getHeight(),map.getSettings().getWidth());
        backgroundColour = Color.WHITE;
        outlineColor = Color.rgb(191, 191, 191);

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

        map.drawIntruderSpawn(gc);
        map.drawGuardSpawn(gc);
        map.getFurniture().forEach(e -> e.draw(gc));
        trails.forEach(e -> drawTrail(gc, e));
        map.getAgents().forEach(e -> drawRays(gc, e.getView()));
        map.getAgents().forEach(e -> e.draw(gc));
    }

    public void addTrail(Trail t)
    {
        trails.add(t);
    }

    private void drawRays(GraphicsContext gc, ArrayList<Ray> rays)
    {
        rays.forEach(e -> e.draw(gc));
    }

    private void drawBackground(GraphicsContext gc)
    {
        gc.setFill(outlineColor);
        gc.fillRect(0,0,getWidth(), getHeight());
        gc.setFill(backgroundColour);
        gc.setLineWidth(3.0);
        gc.fillRect( 0 + Info.getInfo().offsetX ,
                0 + Info.getInfo().offsetY,
                map.getSettings().getWidth() *  Info.getInfo().zoom,
                map.getSettings().getHeight() * Info.getInfo().zoom);
    }

    private void drawTrail(GraphicsContext gc, Trail t)
    {
        gc.setFill(Color.GRAY);
        gc.fillOval(t.getLoc().getX() * Info.getInfo().getZoom() + Info.getInfo().offsetX,
                t.getLoc().getY() * Info.getInfo().getZoom() + Info.getInfo().offsetY,
                2 * Info.getInfo().getZoom(),
                2 * Info.getInfo().getZoom());
    }

    private void handleScroll(ScrollEvent e)
    {
        double dy = e.getDeltaY();
        if(dy > 0)
            Info.getInfo().zoom += zoomRate;
        else if(dy < 0)
            Info.getInfo().zoom -= zoomRate;
        render();
    }

    private void mousePressed(MouseEvent e)
    {
        click = new Point2D(e.getX(), e.getY());

        Vector vector = convertToMapVector(click);
        Agent agent = selectAgent(vector);
        if(agent != null)
        {
            AgentView view = new AgentView(agent);
        }
    }

    private void mouseReleased(MouseEvent e)
    {
        click = null;
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

    private void setInitialZoom(double mapHeight,double mapWidth)
    {
        double dy = ScreenSize.height/mapHeight;
        double dx = ScreenSize.width/mapWidth;
        double padding = 0.1;
        if(mapWidth * dy <= ScreenSize.width && mapHeight * dx<= ScreenSize.height)
            Info.getInfo().setZoom(Math.max(dy,dx)-padding);
        else
            Info.getInfo().setZoom(Math.min(dy,dx)-padding);
    }

    private Agent selectAgent(Vector v)
    {
        for(Agent a: map.getAgents())
        {
            if(v.dist(a.getPosition()) <= 20)
                return a;
        }
        return null;
    }

    private Vector convertToMapVector(Point2D point)
    {
        return new Vector((point.getX() - Info.getInfo().offsetX)/Info.getInfo().getZoom(),
                          (point.getY() - Info.getInfo().offsetY)/Info.getInfo().getZoom());
    }

    private Point2D convertToScreenPoint(Vector vector)
    {
        return new Point2D(vector.getX() * Info.getInfo().getZoom() + Info.getInfo().offsetX,
                           vector.getY() * Info.getInfo().getZoom() + Info.getInfo().offsetY);
    }
}
