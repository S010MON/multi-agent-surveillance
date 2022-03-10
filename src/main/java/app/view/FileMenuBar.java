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
        newFile.setOnAction(e -> System.out.println("Test new..."));
        file.getItems().add(newFile);


        /* Open file menu*/
        /*
        Menu openFile = new Menu("Open");
        for(File f: filterErrorFiles(getResourceFolderFiles("")))
        {
            MenuItem fileOption = new MenuItem(f.getName());
            openFile.getItems().add(fileOption);
            fileOption.setOnAction(e -> app.gotoSimulation(f.getName()));
        }
        file.getItems().add(openFile);
        */

        MenuItem openFile = new MenuItem("Open");

        // create an Event Handler
        EventHandler<ActionEvent> eventOpen =
                new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent e)
                    {
                        // get the file selected
                        File file = app.getFileChooser().showOpenDialog(app.getStage());

                        if (file != null) {
                            app.gotoSimulation(file.getName());
                        }
                        System.out.println("clicked on openFile");
                    }
                };
        openFile.setOnAction(eventOpen);
        file.getItems().add(openFile);


        MenuItem undoFile = new MenuItem("Undo");
        undoFile.setOnAction(e -> app.getStartMenu().getDisplayPane().undo());
        file.getItems().add(undoFile);


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

    private List<File> filterErrorFiles(List<File> files)
    {
        List<File> legalFiles = new ArrayList<>();
        for (File f : files)
        {
            try {
                Settings settings = FileManager.loadSettings(f.getPath());
                Map map = new Map(settings);

                //if no errors thrown, can add to legalFiles
                legalFiles.add(f);
            }
            catch (Exception e)
            {
                System.out.println("file " + f.getName() + " does not contain a legal map");
            }
        }
        return legalFiles;
    }

    private List<File> getResourceFolderFiles (String folder)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();

        List<File> files = new ArrayList<>();
        for (File f : new File(path).listFiles())
        {
            if(f.getPath().endsWith(".txt"))
                files.add(f);
        }
        return files;
    }
}
