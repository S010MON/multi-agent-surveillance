package app.view;

import app.App;
import app.controller.io.FilePath;
import app.view.mapBuilder.DisplayPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

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

        MenuItem openFile = new MenuItem("Open");
        // TODO: add opening of files here
        //openFile.setOnAction(e -> System.out.println("Test open..."));
        openFile.setOnAction(e -> printFiles());
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

    private void printFiles()
    {
        for (File f : filterFiles()) {
            System.out.println(f);
        }
    }

    private List<File> filterFiles()
    {
        List<File> files = new ArrayList<>();
        for (File f : getResourceFolderFiles(""))
        {
            if(f.getPath().endsWith(".txt"))
                files.add(f);
        }
        return files;
    }

    private File[] getResourceFolderFiles (String folder)
    {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();
        return new File(path).listFiles();
    }
}
