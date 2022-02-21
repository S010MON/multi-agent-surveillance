package app.view;

import app.App;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class FileMenuBar extends MenuBar
{
    private App app;

    public FileMenuBar(App app)
    {
        this.app = app;

        // File menu
        Menu fileMenu = new Menu("File");
        // File items
        MenuItem newFile = new MenuItem("New");
        MenuItem openFile = new MenuItem("Open");
        newFile.setOnAction(e -> System.out.println("Test new..."));
        openFile.setOnAction(e -> System.out.println("Test open..."));
        fileMenu.getItems().add(newFile);
        fileMenu.getItems().add(openFile);

        // Map Builder menu
        Menu mapBuildMenu = new Menu("Map Builder");
        // Map Builder item
        MenuItem mapBuilder = new MenuItem("Map Builder");
        mapBuilder.setOnAction(e -> app.gotoStart());
        mapBuildMenu.getItems().add(mapBuilder);

        // Add to the Menu Bar object
        this.getMenus().add(fileMenu);
        this.getMenus().add(mapBuildMenu);
    }
}
