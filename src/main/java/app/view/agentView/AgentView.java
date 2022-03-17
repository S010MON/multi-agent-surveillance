package app.view.agentView;

import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.HashSet;

public class AgentView extends Stage
{
    private Agent agent;
    private AgentViewCanvas canvas;
    private double agentOffsetX;
    private double agentOffsetY;
    private double agentViewWidth;
    private double agentViewHeight;

    public AgentView(Agent agent)
    {
        this.agent = agent;
        setTitle("Agent View");
        setCanvasWidth(agent.getSeen());
        setCanvasHeight(agent.getSeen());
        canvas = new AgentViewCanvas(agent,
                                     agentViewWidth,
                                     agentViewHeight,
                                     agentOffsetX,
                                     agentOffsetY);
        BorderPane bp = new BorderPane();
        bp.setCenter(canvas);
        Scene scene = new Scene(bp, agentViewWidth, agentViewHeight);
        setScene(scene);
        show();
    }

    public void update()
    {
        canvas.render();
    }

    private void setCanvasWidth(HashSet<Vector> vectors)
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

    private void setCanvasHeight(HashSet<Vector> vectors)
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
