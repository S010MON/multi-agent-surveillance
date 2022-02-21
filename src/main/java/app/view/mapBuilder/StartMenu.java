package app.view.mapBuilder;

import app.App;
import app.view.FileMenuBar;
import app.view.ScreenSize;
import app.view.mapBuilder.FurniturePane;
import app.view.mapBuilder.MapBuilder;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

public class StartMenu extends BorderPane
{
    @Getter private App app;

    public StartMenu(App app)
    {
        this.app = app;
        this.setTop(new FileMenuBar(app));
        this.setLeft(new FurniturePane(this));
        this.setRight(new SettingsPane(this));
        this.setCenter(new MapBuilder(this));
    }
}
