package app.view;

import app.App;
import app.controller.io.FileManager;
import app.controller.io.FilePath;
import app.controller.settings.Settings;
import app.model.Map;
import app.view.mapBuilder.DisplayPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


        /* Open file menu*/
        FileChooser fileChooser = new FileChooser();
        String directory = FilePath.get("");
        fileChooser.setInitialDirectory(new File(directory));
        MenuItem openFile = new MenuItem("Open");
        openFile.setOnAction(e -> eventOpen(e, fileChooser));
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

        MenuItem pauseView = new MenuItem("Pause simulation");
        pauseView.setOnAction(e -> app.pauseSimulation());
        view.getItems().add(pauseView);


        // Add to the Menu Bar object
        this.getMenus().addAll(file, edit, view);
    }

    private void eventOpen(ActionEvent e, FileChooser fileChooser)
    {
        // get the file selected
        File file = fileChooser.showOpenDialog(app.getStage());

        if (file != null) {
            app.gotoSimulation(file.getName());
        }
        System.out.println("clicked on openFile");
    }
}
