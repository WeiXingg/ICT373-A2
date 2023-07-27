/*
 ICT373 Assignment 2
 Ong Wei Xing 34444625
 26/7/2023
 AlertHandler.java
 AlertHandler class, to create new window with custom alert message for handling of error
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertHandler {

    public void showAlert(String text) {
        Stage alertWindow = new Stage();
        Scene scene;

        // Settings of new window
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle("Information Box");
        alertWindow.setResizable(false);

        // Label for custom alert message
        Label alertLabel = new Label(text);
        alertLabel.setStyle("-fx-font-size: 16px; -fx-wrap-text: true;");

        // Close window button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> alertWindow.close());

        // Arranging of node
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);
        
        // Add custom text and close button to VBox
        box.getChildren().addAll(alertLabel, closeButton);

        // Set scene
        scene = new Scene(box, 200, 120);
        alertWindow.setScene(scene);
        alertWindow.showAndWait();
    }
}
