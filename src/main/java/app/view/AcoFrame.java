package app.view;

import app.model.agents.ACO.AcoAgent;

public class AcoFrame
{
    private int squareSize = 5;

    public AcoFrame(int width, int height)
    {
        AcoAgent.initializeWorld(width, height);
    }
}
