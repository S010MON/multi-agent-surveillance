package app;

import app.view.FileMenuBar;
import app.view.simulation.Simulation;
import app.view.ScreenSize;
import app.view.mapBuilder.StartMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;

public class App extends Application
{
    private Scene scene;
    @Getter private FileMenuBar fileMenuBar;
    private StartMenu startMenu;
    private Simulation simulation;

    @Override
    public void start(Stage stage)
    {
        this.fileMenuBar = new FileMenuBar(this);
        stage.setTitle("Multi Agent Surveillance");

        startMenu = new StartMenu(this);
        scene = new Scene(startMenu, ScreenSize.width, ScreenSize.height);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }

    public void gotoSimulation()
    {
        if(simulation == null)
        {
            simulation = new Simulation(this);
            scene.setOnKeyTyped(e -> simulation.handleKey(e));
        }
        scene.setRoot(simulation);
    }

    public void gotoStart()
    {
        scene.setRoot(startMenu);
    }
}