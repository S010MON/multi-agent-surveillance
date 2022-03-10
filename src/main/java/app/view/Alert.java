package app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alert
{
    static boolean response;

    public static void displayAlert(String title, String message)
    {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(500);

        Label lbl = new Label();
        lbl.setText(message);
        Button closeButton = new Button("OK");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(lbl, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15, 15, 15, 15));

        Scene sc = new Scene(layout);
        window.setScene(sc);
        window.showAndWait();
    }

    public static boolean askAlert(String title, String message)
    {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(500);

        Label lbl = new Label();
        lbl.setText(message);

        Button continueButton = new Button("continue");
        continueButton.setOnAction(e ->
        {
            response = true;
            window.close();
        });
        Button cancelButton = new Button("cancel");
        cancelButton.setOnAction(e ->
        {
            response = false;
            window.close();
        });

        HBox hLayout = new HBox(25);
        hLayout.getChildren().addAll(continueButton, cancelButton);
        hLayout.setAlignment(Pos.CENTER);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(lbl, hLayout);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15, 15, 15, 15));

        Scene sc = new Scene(layout);
        window.setScene(sc);
        window.showAndWait();

        return response;
    }
}
