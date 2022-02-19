package app.view;

import app.model.furniture.FurnitureType;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        Random rand = new Random();

        // Furniture type enums
        Label furniture = new Label("Furniture Items:");
        vbox.getChildren().add(furniture);
        for(FurnitureType ft : FurnitureType.values())
        {
            Button furnType = new Button(""+ft);
            furnType.setOnAction(e -> {
                mb.run(new DrawRectangle(new Rectangle2D(rand.nextInt(300), rand.nextInt(300), 300+rand.nextInt(300), 300+rand.nextInt(300)), mb.gc, Color.GREEN, true));
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
}
