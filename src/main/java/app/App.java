package app;

import app.view.FileMenuBar;
import app.view.Simulation;
import app.view.ScreenSize;
import app.view.StartMenu;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;


import java.io.IOException;

public class App extends Application
{
    private Stage stage;
    private Scene scene;
    @Getter private FileMenuBar fileMenuBar;
    private StartMenu startMenu;
    private Simulation simulation;

    @Override
    public void start(Stage stage)
    {
        this.stage = stage;
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
        simulation = new Simulation(this);
        scene.setOnKeyTyped(e -> simulation.handleKey(e));
        scene.setRoot(simulation);
    }

    public void gotoStart()
    {
        System.out.println("Start");
        scene.setRoot(startMenu);
    }
}