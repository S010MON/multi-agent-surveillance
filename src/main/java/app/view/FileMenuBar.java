package app.view;

import app.App;
import app.view.mapBuilder.DisplayPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class FileMenuBar extends MenuBar
{
    private App app;

    public FileMenuBar(App app)
    {
        this.app = app;

        /* File menu */
        Menu file = new Menu("File");

        MenuItem newFile = new MenuItem("New");
        newFile.setOnAction(e ->
        {
            if(Alert.askAlert("Warning...", "By clicking continue, any changes currently" +
                                                        " in the map builder not saved to a file will" +
                                                        " be lost."))
            {
                app.getStartMenu().getDisplayPane().clear();
                app.gotoStart();
            }
        });
        file.getItems().add(newFile);

        MenuItem openFile = new MenuItem("Open");
        openFile.setOnAction(e -> System.out.println("Test open..."));
        file.getItems().add(openFile);

        /* Edit menu */
        Menu edit = new Menu("Edit");

        MenuItem undoFile = new MenuItem("Undo");
        undoFile.setOnAction(e -> app.getStartMenu().getDisplayPane().undo());
        edit.getItems().add(undoFile);

        MenuItem clear = new MenuItem("Clear");
        clear.setOnAction(e -> app.getStartMenu().getDisplayPane().clear());
        edit.getItems().add(clear);

        /* View menu */
        Menu view = new Menu("View");

        MenuItem mapView = new MenuItem("Map Builder");
        mapView.setOnAction(e -> app.gotoStart());
        view.getItems().add(mapView);

        MenuItem simView = new MenuItem("Simulation");
        simView.setOnAction(e -> app.gotoSimulation());
        view.getItems().add(simView);


        // Add to the Menu Bar object
        this.getMenus().addAll(file, edit, view);
    }
}
