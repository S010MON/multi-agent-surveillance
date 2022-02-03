package app.view;

import javafx.scene.layout.BorderPane;

public class Frame extends BorderPane
{
    public Frame(int width, int height)
    {
        Renderer renderer = new Renderer(width, height);
        this.setCenter(renderer);
    }
}
