package app.view.mapBuilder;

import app.App;
import app.controller.io.FileManager;
import app.controller.settings.Settings;
import app.controller.settings.SettingsGenerator;
import app.view.Alert;
import app.view.FileMenuBar;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

public class StartMenu extends BorderPane
{
    @Getter private App app;
    @Getter private FurniturePane furniturePane;
    @Getter private DisplayPane displayPane;
    @Getter private SettingsPane settingsPane;
    private Settings settings;

    public StartMenu(App app)
    {
        this.app = app;
        settings = SettingsGenerator.mockSettings();
        displayPane = new DisplayPane(this);
        furniturePane = new FurniturePane(this, displayPane);
        settingsPane = new SettingsPane(this, settings);

        this.setTop(new FileMenuBar(app));
        this.setLeft(furniturePane);
        this.setRight(settingsPane);
        this.setCenter(displayPane);
    }

    public void saveSettings(boolean open)
    {
        furniturePane.getSettings(settings);
        settingsPane.getSettings();
        FileManager.saveSettings(settings, settings.getName());
        Alert.displayAlert("Done!", "The map file was successfully created.");
        // TODO Display success if it was actually successful, and failure if not
        if(open)
        {
            String fileName = settings.getName();
            app.gotoSimulation(fileName);
        }
    }
}
