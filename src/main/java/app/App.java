package app;

import app.view.Frame;
import javafx.application.Application;
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
        Frame frame = new Frame(2048, 1024);
        Scene scene = new Scene(frame,2048, 1024);
        scene.setOnKeyTyped(e -> frame.handleKey(e));
        stage.setTitle("Multi Agent Surveillance");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}