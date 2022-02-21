package app.view;

import app.App;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

public class StartMenu extends BorderPane
{
    @Getter private App app;

    public StartMenu(App app)
    {
        this.app = app;
        this.setTop(new FileMenuBar(app));
        this.setLeft(new SideMenu(this,250, 800));
        this.setCenter(new MapBuilder(this,1350, 800));
    }
}
