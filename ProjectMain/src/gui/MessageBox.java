package gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MessageBox {
	
	public static void show(String title, String message) {
		Stage messageStage = new Stage();
		messageStage.setTitle(title);
		messageStage.setMinWidth(400);
		messageStage.setMinHeight(200);
		
		Label messageLabel = new Label(message);
		messageLabel.setFont(Font.font(20));
		
		VBox vBox = new VBox(20);
		vBox.getChildren().add(messageLabel);
		vBox.setAlignment(Pos.CENTER);
		
		messageStage.setScene(new Scene(vBox));
		messageStage.showAndWait();
	}
	
}
