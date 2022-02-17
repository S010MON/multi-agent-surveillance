package app.view;

import app.controller.FileParser;
import app.controller.Settings;
import app.model.agents.ACO.AcoAgent;
import app.model.map.Map;
import javafx.scene.layout.BorderPane;

public class AcoFrame
{
    private int squareSize = 5;

    public AcoFrame(int width, int height)
    {
        AcoAgent.initializeWorld(width, height);
    }
}
