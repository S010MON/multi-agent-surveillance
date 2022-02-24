package app.view.mapBuilder;

import app.App;
import app.controller.settings.SettingsGenerator;
import app.view.FileMenuBar;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

public class StartMenu extends BorderPane
{
    @Getter private App app;
    @Getter private FurniturePane furniturePane;
    @Getter private DisplayPane displayPane;
    @Getter private SettingsPane settingsPane;

    public StartMenu(App app)
    {
        this.app = app;

        displayPane = new DisplayPane(this);
        furniturePane = new FurniturePane(this);
        settingsPane = new SettingsPane(this, SettingsGenerator.mockSettings());

        this.setTop(new FileMenuBar(app));
        this.setLeft(furniturePane);
        this.setRight(settingsPane);
        this.setCenter(displayPane);
    }
}
