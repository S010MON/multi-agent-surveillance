package app.view.mapBuilder;

import app.controller.settings.Settings;
import app.model.furniture.FurnitureType;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.ArrayDeque;

public class FurniturePane extends StackPane
{
    private TextField minX;
    private TextField minY;
    private TextField width;
    private TextField height;
    private TextField x;
    private TextField y;
    private StartMenu startMenu;
    private DisplayPane displayPane;
    private final int BUTTON_WIDTH = 150;

    public FurniturePane(StartMenu startMenu, DisplayPane displayPane)
    {
        this.startMenu = startMenu;
        this.displayPane = displayPane;
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
            furnType.setPrefWidth(BUTTON_WIDTH);
            vbox.getChildren().add(furnType);
        }

        // Functionality buttons
        Label func = new Label("Create your Map:");
        Button create = new Button("Create");
        create.setPrefWidth(BUTTON_WIDTH);
        create.setOnAction(e -> e.consume()); // TODO add event handling

        Button crOpen = new Button("Create & Open");
        crOpen.setPrefWidth(BUTTON_WIDTH);

        vbox.getChildren().addAll(func, create, crOpen);

        Button undo = new Button("Undo");
        undo.setOnAction(e -> displayPane.undo());
        undo.setPrefWidth(BUTTON_WIDTH);

        vbox.getChildren().addAll(undo);
    }

    private void handleActionEvent(ActionEvent e, FurnitureType type)
    {
        Rectangle2D location = new Rectangle2D( parseRect(minX),
                                                parseRect(minY),
                                                parseRect(width),
                                                parseRect(height));
        MbObject object = new MbObject(location, type);
        displayPane.addObject(object);
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

    public void getSettings(Settings s)
    {
        for(MbObject object : displayPane.getObjects())
        {
            if(object.getType() == FurnitureType.PORTAL)
                s.addTeleport(object.getRect(), object.getVector(), object.getRotation());
            else
                s.addFurniture(object.getRect(), object.getType());
        }
    }
}
