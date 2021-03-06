package app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alert
{
    static boolean response;
    static String answer;
    static TextField userAnswer;

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
        continueButton.setOnAction(e -> handleContinue(window));
        Button cancelButton = new Button("cancel");
        cancelButton.setOnAction(e -> handleCancel(window));

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

    public static String answerAlert(String title, String message)
    {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(500);

        Label lbl = new Label();
        lbl.setText(message);

        userAnswer = new TextField();
        Button cancelButton = new Button("Save");
        cancelButton.setOnAction(e -> handleSave(window));

        HBox hLayout = new HBox(25);
        hLayout.getChildren().addAll(userAnswer, cancelButton);
        hLayout.setAlignment(Pos.CENTER);
        VBox layout = new VBox(10);
        layout.getChildren().addAll(lbl, hLayout);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15, 15, 15, 15));

        Scene sc = new Scene(layout);
        window.setScene(sc);
        window.showAndWait();

        return answer;
    }

    private static void handleContinue(Stage window)
    {
        response = true;
        window.close();
    }

    private static void handleCancel(Stage window)
    {
        response = false;
        window.close();
    }

    private static void handleSave(Stage window)
    {
        answer = userAnswer.getText();
        window.close();
    }
}
