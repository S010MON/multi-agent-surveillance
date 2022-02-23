package app.view.mapBuilder;

import app.controller.settings.Settings;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
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
        Label header = new Label("Current simulation settings:");
        header.setFont(Font.font("", FontWeight.BOLD, 24));
        Label mapName = new Label("Map Name - " + s.getName());
        mapName.setFont(new Font(16));
        Label gameMode = new Label("Game Mode - " + s.getGameMode());
        gameMode.setFont(new Font(16));
        Label timeStep = new Label("Time Step - " + s.getTimeStep());
        timeStep.setFont(new Font(16));
        Label scaling = new Label("Scaling - " + s.getScaling());
        scaling.setFont(new Font(16));
        Label noOfGuards = new Label("Number of Guards - " + s.getNoOfGuards());
        noOfGuards.setFont(new Font(16));
        Label noOfIntruders = new Label("Number of Intruders - " + s.getNoOfIntruders());
        noOfIntruders.setFont(new Font(16));
        Label baseGuard = new Label("Walk speed Guard - " + s.getWalkSpeedGuard());
        baseGuard.setFont(new Font(16));
        Label baseIntruder = new Label("Walk speed Intruder - " + s.getWalkSpeedIntruder());
        baseIntruder.setFont(new Font(16));
        Label sprintGuard = new Label("Sprint speed Guard - " + s.getSprintSpeedGuard());
        sprintGuard.setFont(new Font(16));
        Label sprintIntruder = new Label("Sprint speed Intruder - " + s.getSprintSpeedIntruder());
        sprintIntruder.setFont(new Font(16));
        vbox.getChildren().addAll(  header, mapName, gameMode, timeStep, scaling, noOfGuards,
                                    noOfIntruders, baseGuard, baseIntruder, sprintGuard,
                                    sprintIntruder);
        this.setMargin(vbox, new Insets(10, 10, 10, 10));
        this.getChildren().addAll(vbox);
    }
}
