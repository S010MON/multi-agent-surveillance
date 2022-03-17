package app.view.agentView;

import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashSet;

public class AgentViewCanvas extends Canvas
{
    private Agent agent;
    private Color backgroundColour, outlineColor;
    private double agentOffsetX;
    private double agentOffsetY;
    private double agentViewWidth;
    private double agentViewHeight;

    public AgentViewCanvas(Agent agent, double w, double h, double offsetX, double offsetY)
    {
        super(w, h);
        this.agentOffsetX = offsetX;
        this.agentOffsetY = offsetY;
        this.agentViewWidth = w;
        this.agentViewHeight = h;
        this.agent = agent;
        backgroundColour = Color.WHITE;
        outlineColor = Color.rgb(191, 191, 191);
        render();
    }

    public void render()
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        drawBackground(gc);

        drawSeen(gc, agent.getSeen());
    }

    public void drawSeen(GraphicsContext gc, HashSet<Vector> seen)
    {
        gc.setFill(Color.GREEN);
        double diameter = 5;
        for(Vector v : seen)
        {
            gc.fillOval(v.getX() - diameter/2 - agentOffsetX, v.getY() - diameter/2 - agentOffsetY, diameter, diameter);
        }
    }


    private void drawBackground(GraphicsContext gc)
    {
        gc.setFill(outlineColor);
        gc.fillRect(0,0,getWidth(), getHeight());
        gc.setFill(backgroundColour);
        gc.setLineWidth(3.0);
        gc.fillRect(0, 0, agentViewWidth, agentViewHeight);
    }

    private void canvasWidth(HashSet<Vector> vectors)
    {
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        for(Vector v : vectors)
        {
            if(v.getX() < minX)
            {
                minX = v.getX();
            }
            if(v.getX() > maxX)
            {
                maxX = v.getX();
            }
        }
        agentOffsetX = minX;
        agentViewWidth = maxX - minX;
    }

    private void canvasHeight(HashSet<Vector> vectors)
    {
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for(Vector v : vectors)
        {
            if(v.getY() < minY)
            {
                minY = v.getX();
            }
            if(v.getY() > maxY)
            {
                maxY = v.getY();
            }
        }
        agentOffsetY = minY;
        agentViewHeight = maxY - minY;
    }
}
