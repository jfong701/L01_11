package gui;

import gui.ProfessorPage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.geometry.Insets;

public class IntroScreen extends Application {

	public static void main(String[] args) {
		launch(args);

	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Intro Screen");
		
		// Create and setup grid layout
		GridPane loginLayout = new GridPane();
		loginLayout.setPadding(new Insets(15, 15, 15, 15));
		loginLayout.setVgap(5);
		loginLayout.setHgap(5);
		
		// Username and password labels and inputs
		Label userLabel = new Label("Username");
		GridPane.setConstraints(userLabel, 5, 1);
		
		TextField userInput = new TextField("Professor");
		GridPane.setConstraints(userInput, 5, 2);
		
		Label passLabel = new Label("Password");
		GridPane.setConstraints(passLabel, 5, 4);
		
		PasswordField passInput = new PasswordField();
		GridPane.setConstraints(passInput, 5, 5);
		
		// Log in button
		Button loginButton = new Button("Login");
		loginButton.setOnAction(e -> {
			if (userInput.getText().equals("Professor")) {
			ProfessorPage.login(primaryStage, userInput.getText(), passInput.getText());
			}
		});
		GridPane.setConstraints(loginButton, 5, 6);
		
		
		loginLayout.getChildren().addAll(userLabel, userInput, passLabel, passInput, loginButton);	
		Scene loginScene = new Scene(loginLayout, 500, 250);
		primaryStage.setScene(loginScene);
		primaryStage.show();
	}

}
