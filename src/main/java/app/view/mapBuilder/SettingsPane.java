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

public class SettingsPane extends StackPane
{
    private StartMenu startMenu;
    private Settings s;
    private TextField name;
    private TextField mode;
    private TextField w;
    private TextField h;
    private TextField noGuards;
    private TextField noIntruders;
    private TextField bGuard;
    private TextField bIntruder;
    private TextField sGuard;
    private TextField sIntruder;

    public SettingsPane(StartMenu startMenu, Settings s)
    {
        this.startMenu = startMenu;
        this.s = s;
        this.setPrefWidth(300);

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
        Label width = new Label("Width:");
        width.setFont(new Font(16));
        Label height = new Label("Height:");
        height.setFont(new Font(16));
        Label guard = new Label("Guards:");
        guard.setFont(Font.font("", FontWeight.BOLD, 18));
        Label noOfGuards = new Label("Number");
        noOfGuards.setFont(new Font(16));
        Label baseGuard = new Label("Walk Speed");
        baseGuard.setFont(new Font(16));
        Label sprintGuard = new Label("Sprint Speed");
        sprintGuard.setFont(new Font(16));
        Label intruder = new Label("Intruders:");
        intruder.setFont(Font.font("", FontWeight.BOLD, 18));
        Label noOfIntruders = new Label("Number");
        noOfIntruders.setFont(new Font(16));
        Label baseIntruder = new Label("Walk Speed");
        baseIntruder.setFont(new Font(16));
        Label sprintIntruder = new Label("Sprint Speed");
        sprintIntruder.setFont(new Font(16));

        // Text Fields with current values editable.
        name = new TextField();
        name.setMaxWidth(100);
        name.setText(s.getName());
        mode = new TextField();
        mode.setMaxWidth(100);
        mode.setText(""+s.getGameMode());
        w = new TextField();
        w.setMaxWidth(100);
        w.setText(""+s.getWidth());
        h = new TextField();
        h.setMaxWidth(100);
        h.setText(""+s.getHeight());
        noGuards = new TextField();
        noGuards.setMaxWidth(100);
        noGuards.setText(""+s.getNoOfGuards());
        noIntruders = new TextField();
        noIntruders.setMaxWidth(100);
        noIntruders.setText(""+s.getNoOfIntruders());
        bGuard = new TextField();
        bGuard.setMaxWidth(100);
        bGuard.setText(""+s.getWalkSpeedGuard());
        bIntruder = new TextField();
        bIntruder.setMaxWidth(100);
        bIntruder.setText(""+s.getWalkSpeedIntruder());
        sGuard = new TextField();
        sGuard.setMaxWidth(100);
        sGuard.setText(""+s.getSprintSpeedGuard());
        sIntruder = new TextField();
        sIntruder.setMaxWidth(100);
        sIntruder.setText(""+s.getSprintSpeedIntruder());

        // Add labels and fields to grid
        grid.add(mapName, 0, 0);
        grid.add(name, 1, 0);
        grid.add(gameMode, 0, 1);
        grid.add(mode, 1, 1);
        grid.add(width, 0, 2);
        grid.add(w, 1, 2);
        grid.add(height, 0, 3);
        grid.add(h, 1, 3);
        grid.add(guard, 0, 5);
        grid.add(noOfGuards, 0, 6);
        grid.add(noGuards, 1, 6);
        grid.add(baseGuard, 0, 7);
        grid.add(bGuard, 1, 7);
        grid.add(sprintGuard, 0, 8);
        grid.add(sGuard, 1, 8);
        grid.add(intruder, 0, 10);
        grid.add(noOfIntruders, 0, 11);
        grid.add(noIntruders, 1, 11);
        grid.add(baseIntruder, 0, 12);
        grid.add(bIntruder, 1, 12);
        grid.add(sprintIntruder, 0, 13);
        grid.add(sIntruder, 1, 13);

        vbox.getChildren().addAll(header, grid);
        this.setMargin(vbox, new Insets(10, 10, 10, 10));
        this.getChildren().addAll(vbox);
    }

    public int getMapWidth()
    {
        return Integer.parseInt(w.getText());
    }

    public int getMapHeight()
    {
        return Integer.parseInt(h.getText());
    }

    public void getSettings()
    {
        s.setName(name.getText());
        s.setGameMode(Integer.parseInt(mode.getText()));
        s.setWidth(Integer.parseInt(w.getText()));
        s.setHeight(Integer.parseInt(h.getText()));
        s.setNoOfGuards(Integer.parseInt(noGuards.getText()));
        s.setNoOfIntruders(Integer.parseInt(noIntruders.getText()));
        s.setWalkSpeedGuard(Double.parseDouble(bGuard.getText()));
        s.setWalkSpeedIntruder(Double.parseDouble(bIntruder.getText()));
        s.setSprintSpeedGuard(Double.parseDouble(sGuard.getText()));
        s.setWalkSpeedIntruder(Double.parseDouble(sIntruder.getText()));
    }
}
