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

        /* View menu */
        Menu file = new Menu("File");

        MenuItem newFile = new MenuItem("New");
        newFile.setOnAction(e -> System.out.println("Test new..."));
        file.getItems().add(newFile);

        MenuItem openFile = new MenuItem("Open");
        openFile.setOnAction(e -> System.out.println("Test open..."));
        file.getItems().add(openFile);


        /* View menu */
        Menu view = new Menu("View");

        MenuItem mapView = new MenuItem("Map Builder");
        mapView.setOnAction(e -> app.gotoStart());
        view.getItems().add(mapView);

        MenuItem simView = new MenuItem("Simulation");
        simView.setOnAction(e -> app.gotoSimulation());
        view.getItems().add(simView);


        // Add to the Menu Bar object
        this.getMenus().add(file);
        this.getMenus().add(view);
    }
}
