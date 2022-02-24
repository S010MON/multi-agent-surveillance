package app.view.mapBuilder;

import app.App;
import app.controller.io.FileManager;
import app.controller.settings.Settings;
import app.controller.settings.SettingsGenerator;
import app.controller.settings.SettingsObject;
import app.view.FileMenuBar;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

public class StartMenu extends BorderPane
{
    @Getter private App app;
    private MapBuilder mb;
    private FurniturePane fp;
    private SettingsPane sp;
    private Settings settings;

    public StartMenu(App app)
    {
        this.app = app;
        this.setTop(new FileMenuBar(app));
        settings = SettingsGenerator.mockSettings();
        mb = new MapBuilder(this);
        fp = new FurniturePane(this, mb);
        sp = new SettingsPane(this, settings);
        this.setLeft(fp);
        this.setRight(sp);
        this.setCenter(mb);
    }

    public void saveSettings()
    {
        fp.getSettings(settings);
        sp.getSettings();
        FileManager.saveSettings(settings, settings.getName());
    }
}
