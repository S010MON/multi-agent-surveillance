package app.view;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class FileMenuBar extends MenuBar
{
    public FileMenuBar(Frame frame)
    {
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
        mapBuilder.setOnAction(e ->
        {
            frame.setCenter(new MapBuilder(1350, 800));
            frame.setLeft(new SideMenu(250, 800));
        });
        mapBuildMenu.getItems().add(mapBuilder);

        // Add to the Menu Bar object
        this.getMenus().add(fileMenu);
        this.getMenus().add(mapBuildMenu);
    }
}
