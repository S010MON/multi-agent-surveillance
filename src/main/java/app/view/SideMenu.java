package app.view;

import app.model.furniture.FurnitureType;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Random;

public class SideMenu extends StackPane
{
    private MapBuilder mb;

    public SideMenu(int width, int height, MapBuilder mb)
    {
        this.setWidth(width);
        this.setHeight(height);
        this.mb = mb;
        VBox vbox = new VBox(10);
        loadButtons(vbox);
        this.setMargin(vbox, new Insets(10, 10, 10, 10));
        this.getChildren().addAll(vbox);
    }

    public void loadButtons(VBox vbox)
    {
        // User Rectangle Area
        Label l = new Label("Enter MinX, MinY, width, height:");
        TextField minX = new TextField();
        TextField minY = new TextField();
        TextField width = new TextField();
        TextField height = new TextField();

        vbox.getChildren().addAll(l, minX, minY, width, height);

        // Furniture type enums
        Label furniture = new Label("Furniture Items:");
        vbox.getChildren().add(furniture);
        for(FurnitureType ft : FurnitureType.values())
        {
            Button furnType = new Button(""+ft);
            furnType.setOnAction(e -> {
                DrawRectangle dr = new DrawRectangle();
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
            });
            vbox.getChildren().add(furnType);
        }

        // Functionality buttons
        Label func = new Label("Create your Map:");
        Button create = new Button("Create");
        Button crOpen = new Button("Create & Open");

        vbox.getChildren().addAll(func, create, crOpen);

        Button undo = new Button("Undo");
        undo.setOnAction(e -> mb.undo());

        vbox.getChildren().addAll(undo);
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
}
