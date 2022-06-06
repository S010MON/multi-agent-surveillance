package app.view.simulation;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Line;
import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.Trail;
import app.model.agents.Agent;
import app.model.Type;
import app.view.ScreenSize;
import app.view.agentView.AgentView;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Setter;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Renderer extends Canvas
{
    @Setter private boolean displayRay = true;
    @Setter private boolean displaySound = true;
    @Setter private boolean displayTrail = true;
    @Setter private boolean displayMiniMaps = true;
    @Setter private boolean displayAreas = true;
    private Map map;
    private Color backgroundColour, outlineColor;
    private double zoomRate = 0.2d;
    private Point2D click;


    public Renderer(Map map)
    {
        super(ScreenSize.width, ScreenSize.height);
        this.map = map;
        this.setInitialZoom(map.getSettings().getHeight(),map.getSettings().getWidth());
        backgroundColour = Color.WHITE;
        outlineColor = Color.rgb(191, 191, 191);

        setOnMousePressed(this::mousePressed);
        setOnMouseReleased(this::mouseReleased);
        setOnMouseDragged(this::mouseDragged);
        setOnMouseMoved(this::mouseMoved);
        setOnScroll(this::handleScroll);
        render();
    }

    public void render()
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        drawBackground(gc);
        map.getFurniture().forEach(e -> e.draw(gc));

        if(displayAreas) map.drawIndicatorBoxes(gc);
        if(displaySound) map.getSoundSources().forEach(e -> e.draw(gc));
        if(displayTrail) map.getTrails().forEach(e -> drawTrail(gc, e));
        if(displayRay) map.getAgents().forEach(e -> drawRays(gc, e.getView()));

        map.getAgents().forEach(e -> e.draw(gc));

        if(displayMiniMaps)
        {
            drawMiniMapGuard(gc);
            drawMiniMapIntruder(gc);
        }
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


    private void drawMiniMapGuard(GraphicsContext gc)
    {
        final double DIAMETER = 2;
        final double SCALE = 3;
        double mapWidth = map.getSettings().getWidth();
        double mapHeight = map.getSettings().getHeight();
        double mapWidthScaled = map.getSettings().getWidth() / SCALE;
        double mapHeightScaled = map.getSettings().getHeight() / SCALE;
        double xPos = mapWidth + 20;
        double yPos = 20;

        // Add the label
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(20 * Info.getInfo().getZoom()));
        gc.fillText("Guard Team: " + map.percentageComplete(Type.GUARD) + "%",
                xPos * Info.getInfo().getZoom() + Info.getInfo().offsetX,
                (yPos - 20) * Info.getInfo().getZoom() + Info.getInfo().offsetY,
                mapWidthScaled * Info.getInfo().getZoom());

        // Add the rectangle background
        gc.setFill(Color.WHITE);
        gc.fillRect((xPos) * Info.getInfo().getZoom() + Info.getInfo().offsetX,
                (yPos) * Info.getInfo().getZoom() + Info.getInfo().offsetY,
                mapWidthScaled * Info.getInfo().getZoom(),
                mapHeightScaled * Info.getInfo().getZoom());

        // Add explored vectors
        gc.setFill(Color.BLUE);
        for (Vector v : map.getGuardsSeen())
        {
            double x = v.getX() - DIAMETER / 2;
            double y = v.getY() - DIAMETER / 2;
            gc.fillOval((xPos + (x / SCALE)) * Info.getInfo().getZoom() + Info.getInfo().offsetX,
                    (yPos + (y / SCALE)) * Info.getInfo().getZoom() + Info.getInfo().offsetY,
                    DIAMETER * Info.getInfo().getZoom(),
                    DIAMETER * Info.getInfo().getZoom());
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect((xPos -2) * Info.getInfo().getZoom() + Info.getInfo().offsetX,
                (yPos -2) * Info.getInfo().getZoom() + Info.getInfo().offsetY,
                (mapWidthScaled + 4) * Info.getInfo().getZoom(),
                (mapHeightScaled + 4) * Info.getInfo().getZoom());
    }

    private void drawMiniMapIntruder(GraphicsContext gc)
    {
        final double DIAMETER = 2;
        final double SCALE = 3;
        double mapWidth = map.getSettings().getWidth();
        double mapHeight = map.getSettings().getHeight();
        double mapWidthScaled = map.getSettings().getWidth() / SCALE;
        double mapHeightScaled = map.getSettings().getHeight() / SCALE;
        double xPos = mapWidth + 20;
        double yPos = mapHeight - mapHeightScaled;

        // Add the label
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(20 * Info.getInfo().getZoom()));
        gc.fillText("Intruder Team: " + map.percentageComplete(Type.INTRUDER) + "%",
                      xPos * Info.getInfo().getZoom() + Info.getInfo().offsetX,
                      (yPos - 20) * Info.getInfo().getZoom() + Info.getInfo().offsetY,
                      mapWidthScaled * Info.getInfo().getZoom());

        // Add the rectangle background
        gc.setFill(Color.WHITE);
        gc.fillRect((xPos) * Info.getInfo().getZoom() + Info.getInfo().offsetX,
                    (yPos) * Info.getInfo().getZoom() + Info.getInfo().offsetY,
                    mapWidthScaled * Info.getInfo().getZoom(),
                    mapHeightScaled * Info.getInfo().getZoom());

        // Add explored vectors
        gc.setFill(Color.RED);
        for (Vector v : map.getIntrudersSeen())
        {
            double x = v.getX() - DIAMETER / 2;
            double y = v.getY() - DIAMETER / 2;
            gc.fillOval((xPos + (x / SCALE)) * Info.getInfo().getZoom() + Info.getInfo().offsetX,
                        (yPos + (y / SCALE)) * Info.getInfo().getZoom() + Info.getInfo().offsetY,
                        DIAMETER * Info.getInfo().getZoom(),
                        DIAMETER * Info.getInfo().getZoom());
        }

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect((xPos -2) * Info.getInfo().getZoom() + Info.getInfo().offsetX,
                (yPos -2) * Info.getInfo().getZoom() + Info.getInfo().offsetY,
                (mapWidthScaled + 4) * Info.getInfo().getZoom(),
                (mapHeightScaled + 4) * Info.getInfo().getZoom());
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
            agent.addViewWindow(view);
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

    private void mouseMoved(MouseEvent e)
    {
        Point2D p = new Point2D(e.getX(), e.getY());
        Vector mousePsn = convertToMapVector(p);

        if(map.getHuman() != null)
        {
            Vector humanPsn = map.getHuman().getPosition();
            Line line = new Line(humanPsn, mousePsn);
            map.getHuman().rotateTo(line.angle());
        }
    }

    private void setInitialZoom(double mapHeight,double mapWidth)
    {
        mapWidth = mapWidth * 1.4;

        double dy = ScreenSize.height/mapHeight;
        double dx = ScreenSize.width/mapWidth;
        double padding = 0.05;
        double scale;
        if(mapWidth * dy <= ScreenSize.width && mapHeight * dx<= ScreenSize.height)
            scale = Math.max(dy,dx)-padding;
        else
            scale = Math.min(dy,dx)-padding;

        Info.getInfo().setZoom(scale);

        double offsetX = (Math.abs(ScreenSize.width- mapWidth * scale)/2);
        double offsetY = (Math.abs(ScreenSize.height- mapHeight * scale))/2;

        Info.getInfo().setOffsetX(offsetX);
        Info.getInfo().setOffsetY(offsetY);
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
