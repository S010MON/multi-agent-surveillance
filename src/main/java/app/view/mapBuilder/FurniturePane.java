package app.view.mapBuilder;

import app.controller.linAlg.Vector;
import app.controller.settings.Settings;
import app.model.furniture.FurnitureType;
import app.view.mapBuilder.DrawRectangle;
import app.view.mapBuilder.MapBuilder;
import app.view.mapBuilder.StartMenu;
import app.view.mapBuilder.UIRect;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FurniturePane extends StackPane
{
    private MapBuilder mb;
    private TextField minX;
    private TextField minY;
    private TextField width;
    private TextField height;
    private TextField x;
    private TextField y;
    private StartMenu startMenu;

    public FurniturePane(StartMenu startMenu, MapBuilder mb)
    {
        this.mb = mb;
        this.startMenu = startMenu;
        VBox vbox = new VBox(10);
        loadButtons(vbox);
        this.setMargin(vbox, new Insets(10, 10, 10, 10));
        this.getChildren().addAll(vbox);
    }

    public void loadButtons(VBox vbox)
    {
        // User Rectangle Area
        Label l = new Label("Enter MinX, MinY, width, height:");
        minX = new TextField();
        minY = new TextField();
        width = new TextField();
        height = new TextField();
        vbox.getChildren().addAll(l, minX, minY, width, height);

        // Portal co-ordinates
        Label teleport = new Label("Teleport to x and y:");
        x = new TextField();
        y = new TextField();

        vbox.getChildren().addAll(teleport, x, y);

        // Furniture type enums
        Label furniture = new Label("Furniture Items:");
        vbox.getChildren().add(furniture);
        for(FurnitureType ft : FurnitureType.values())
        {
            Button furnType = new Button(""+ft);
            furnType.setOnAction(e -> handleActionEvent(e, ft));
            vbox.getChildren().add(furnType);
        }

        // Functionality buttons
        Label func = new Label("Create your Map:");
        Button create = new Button("Create");
        create.setOnAction(e -> {
            Settings s = saveMap();

            // Write to file (Add number of guards etc.)
        });

        Button crOpen = new Button("Create & Open");

        vbox.getChildren().addAll(func, create, crOpen);

        Button undo = new Button("Undo");
        undo.setOnAction(e -> mb.undo());

        vbox.getChildren().addAll(undo);
    }

    private void handleActionEvent(ActionEvent e, FurnitureType ft)
    {
        DrawRectangle dr = new DrawRectangle();
        dr.setType(ft);
        dr.setRect(new Rectangle2D( parseRect(minX), parseRect(minY), parseRect(width),
                parseRect(height)));
        dr.setGc(mb.gc);
        switch(ft.label)
        {
            case "wall" ->
                    {
                        dr.setColour(Color.SANDYBROWN);
                        dr.setFill(true);
                    }
            case "shade" ->
                    {
                        dr.setColour(Color.LIGHTGRAY);
                        dr.setFill(true);
                    }
            case "glass" ->
                    {
                        dr.setColour(Color.LIGHTBLUE);
                        dr.setFill(true);
                    }
            case "tower" ->
                    {
                        dr.setColour(Color.OLIVE);
                        dr.setFill(true);
                    }
            case "teleport" ->
                    {
                        dr.setVector(new Vector(parseRect(x), parseRect(y)));
                        dr.setColour(Color.OLIVEDRAB);
                        dr.setFill(true);
                    }
            case "spawnAreaGuards" ->
                    {
                        dr.setColour(Color.BLUE);
                        dr.setFill(false);
                    }
            case "spawnAreaIntruders" ->
                    {
                        dr.setColour(Color.RED);
                        dr.setFill(false);
                    }
            case "targetArea" ->
                    {
                        dr.setColour(Color.GOLD);
                        dr.setFill(false);
                    }
        }
        mb.run(dr);
    }

    public int parseRect(TextField tf)
    {
        try
        {
         return Integer.parseInt(tf.getText());
        }
        catch(NumberFormatException e)
        {
            System.out.println("Input a number");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    public Settings saveMap()
    {
        Settings s = new Settings();
        s.setWidth(2048);
        s.setHeight(1024);
        for(UIRect object : mb.getHistory())
        {
            DrawRectangle rectObject = (DrawRectangle) object;
            if(rectObject.getType().label.equals("teleport"))
            {
                s.addTeleport(rectObject.getRect(), rectObject.getVector());
            }
            else
            {
                s.addFurniture(rectObject.getRect(), rectObject.getType());
            }
        }

        return s;
    }
}
