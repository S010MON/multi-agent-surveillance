package app.view.agentView;

import app.controller.linAlg.Vector;
import app.controller.linAlg.VectorSet;
import app.model.agents.Agent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class AgentViewCanvas extends Canvas
{
    private Agent agent;
    private Color backgroundColour, outlineColor;
    private double agentOffsetX;
    private double agentOffsetY;
    private double agentViewWidth;
    private double agentViewHeight;
    private final double diameter = 5;

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

    public void drawSeen(GraphicsContext gc, VectorSet seen)
    {
        gc.setFill(Color.GREEN);
        for(Vector v : seen)
        {
            gc.fillOval(v.getX() - diameter/2 - agentOffsetX,
                        v.getY() - diameter/2 - agentOffsetY,
                        diameter,
                        diameter);
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
}
