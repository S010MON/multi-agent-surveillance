package app.view;

import app.App;
import app.controller.io.FilePath;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.stage.FileChooser;

import java.io.File;

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

        MenuItem pauseView = new MenuItem("Pause/Resume");
        pauseView.setOnAction(e -> app.pause());
        edit.getItems().add(pauseView);

        /* View menu */
        Menu view = new Menu("View");

        MenuItem mapView = new MenuItem("Map Builder");
        mapView.setOnAction(e -> app.gotoStart());
        view.getItems().add(mapView);

        MenuItem simView = new MenuItem("Simulation");
        simView.setOnAction(e -> app.gotoSimulation());
        view.getItems().add(simView);

        /* Display Options */
        Menu displayOptions = new Menu("Display Options");

        RadioMenuItem rays = new RadioMenuItem("Display Rays");
        rays.setSelected(true);
        rays.setOnAction(e -> app.updateRenderer(1, rays.isSelected()));

        RadioMenuItem sound = new RadioMenuItem("Display Sound");
        sound.setSelected(true);
        sound.setOnAction(e -> app.updateRenderer(2, sound.isSelected()));

        RadioMenuItem trail = new RadioMenuItem("Display Trail");
        trail.setSelected(true);
        trail.setOnAction(e -> app.updateRenderer(3, trail.isSelected()));

        RadioMenuItem miniMaps = new RadioMenuItem("Display MiniMaps");
        miniMaps.setSelected(true);
        miniMaps.setOnAction(e -> app.updateRenderer(4, miniMaps.isSelected()));

        RadioMenuItem areas = new RadioMenuItem("Display Spawn Areas etc.");
        areas.setSelected(true);
        areas.setOnAction(e -> app.updateRenderer(5, areas.isSelected()));

        displayOptions.getItems().addAll(rays, sound, trail, miniMaps, areas);

        // Add to the Menu Bar object
        this.getMenus().addAll(file, edit, view, displayOptions);
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
