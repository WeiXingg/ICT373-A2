
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

        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle("Error");
        alertWindow.setResizable(false);

        Label alertLabel = new Label(text);
        alertLabel.setStyle("-fx-font-size: 16px; -fx-wrap-text: true;");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> alertWindow.close());

        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(alertLabel, closeButton);

        scene = new Scene(box, 200, 120);
        alertWindow.setScene(scene);
        alertWindow.showAndWait();
    }
}
