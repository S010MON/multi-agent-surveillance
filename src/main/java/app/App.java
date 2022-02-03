package app;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application
{
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException
    {
        this.stage = stage;
        Group root = new Group();
        Scene scene = new Scene(root,600, 300);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}