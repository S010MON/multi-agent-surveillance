package app.view.mapBuilder;

import app.App;
import app.controller.settings.SettingsGenerator;
import app.view.FileMenuBar;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

public class StartMenu extends BorderPane
{
    @Getter private App app;

    public StartMenu(App app)
    {
        this.app = app;
        this.setTop(new FileMenuBar(app));
        MapBuilder mb = new MapBuilder(this);
        this.setLeft(new FurniturePane(this, mb));
        this.setRight(new SettingsPane(this, SettingsGenerator.mockSettings()));
        this.setCenter(mb);
    }
}
