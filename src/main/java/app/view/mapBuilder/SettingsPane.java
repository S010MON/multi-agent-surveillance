package app.view.mapBuilder;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class SettingsPane extends StackPane // Might need to change this to extend something else
{
    private StartMenu startMenu;

    public SettingsPane(StartMenu startMenu)
    {
        this.startMenu = startMenu;

        Label label = new Label("Settings Go Here");
        this.getChildren().add(label);
    }
}
