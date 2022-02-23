package app.view.mapBuilder;

import app.controller.settings.Settings;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SettingsPane extends StackPane // Might need to change this to extend something else
{
    private StartMenu startMenu;
    private Settings s;

    public SettingsPane(StartMenu startMenu, Settings s)
    {
        this.startMenu = startMenu;
        this.s = s;

        // Labels displaying current settings information.
        VBox vbox = new VBox(10);
        Label header = new Label("Settings:");
        header.setFont(Font.font("", FontWeight.BOLD, 24));

        // Grid layout
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        // Labels
        Label mapName = new Label("Map Name:");
        mapName.setFont(new Font(16));
        Label gameMode = new Label("Game Mode:");
        gameMode.setFont(new Font(16));
        Label timeStep = new Label("Time Step:");
        timeStep.setFont(new Font(16));
        Label scaling = new Label("Scaling:");
        scaling.setFont(new Font(16));
        Label noOfGuards = new Label("Number of Guards:");
        noOfGuards.setFont(new Font(16));
        Label noOfIntruders = new Label("Number of Intruders:");
        noOfIntruders.setFont(new Font(16));
        Label baseGuard = new Label("Walk speed Guard:");
        baseGuard.setFont(new Font(16));
        Label baseIntruder = new Label("Walk speed Intruder:");
        baseIntruder.setFont(new Font(16));
        Label sprintGuard = new Label("Sprint speed Guard:");
        sprintGuard.setFont(new Font(16));
        Label sprintIntruder = new Label("Sprint speed Intruder:");
        sprintIntruder.setFont(new Font(16));

        // Text Fields with current values editable.
        TextField name = new TextField();
        name.setText(s.getName());
        TextField mode = new TextField();
        mode.setText(""+s.getGameMode());
        TextField step = new TextField();
        step.setText(""+s.getTimeStep());
        TextField scale = new TextField();
        scale.setText(""+s.getScaling());
        TextField noGuards = new TextField();
        noGuards.setText(""+s.getNoOfGuards());
        TextField noIntruders = new TextField();
        noIntruders.setText(""+s.getNoOfIntruders());
        TextField bGuard = new TextField();
        bGuard.setText(""+s.getWalkSpeedGuard());
        TextField bIntruder = new TextField();
        bIntruder.setText(""+s.getWalkSpeedIntruder());
        TextField sGuard = new TextField();
        sGuard.setText(""+s.getSprintSpeedGuard());
        TextField sIntruder = new TextField();
        sIntruder.setText(""+s.getSprintSpeedIntruder());

        // Add labels and fields to grid
        grid.add(mapName, 0, 0);
        grid.add(name, 1, 0);
        grid.add(gameMode, 0, 1);
        grid.add(mode, 1, 1);
        grid.add(timeStep, 0, 2);
        grid.add(step, 1, 2);
        grid.add(scaling, 0, 3);
        grid.add(scale, 1, 3);
        grid.add(noOfGuards, 0, 4);
        grid.add(noGuards, 1, 4);
        grid.add(noOfIntruders, 0, 5);
        grid.add(noIntruders, 1, 5);
        grid.add(baseGuard, 0, 6);
        grid.add(bGuard, 1, 6);
        grid.add(baseIntruder, 0, 7);
        grid.add(bIntruder, 1, 7);
        grid.add(sprintGuard, 0, 8);
        grid.add(sGuard, 1, 8);
        grid.add(sprintIntruder, 0, 9);
        grid.add(sIntruder, 1, 9);

        vbox.getChildren().addAll(header, grid);
        this.setMargin(vbox, new Insets(10, 10, 10, 10));
        this.getChildren().addAll(vbox);
    }
}
