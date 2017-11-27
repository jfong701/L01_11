package gui;

import java.sql.SQLException;

import com.mysql.jdbc.Statement;

import gui.ProfessorPage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jdbc.DOA;
import validator.Validators;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.geometry.Insets;

import gui.StudentPage;

public class IntroScreen extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	public static void startProgram(Stage primaryStage) {
		primaryStage.setTitle("Angry Sloth");

		// Create and setup grid layout
		GridPane loginLayout = new GridPane();
		loginLayout.setPadding(new Insets(15, 15, 15, 15));
		loginLayout.setVgap(5);
		loginLayout.setHgap(5);

		// Username and password labels and inputs
		Label userLabel = new Label("Username");
		GridPane.setConstraints(userLabel, 5, 1);

		TextField userInput = new TextField();
		GridPane.setConstraints(userInput, 5, 2);

		Label passLabel = new Label("Password");
		GridPane.setConstraints(passLabel, 5, 4);

		PasswordField passInput = new PasswordField();
		GridPane.setConstraints(passInput, 5, 5);

		// Log in button
		Button loginButton = new Button("Login");
		loginButton.setOnAction(e -> {
			try {
				if (Validators.checkProf(userInput.getText(), passInput.getText())) {
					ProfessorPage.login(primaryStage, userInput.getText(), passInput.getText());
				} else if (Validators.checkStudent(userInput.getText(), passInput.getText())) {
					StudentPage.login(primaryStage, userInput.getText(), passInput.getText());
				} else {
					MessageBox.show("Login Failed", "Invalid Credentials");
				}
			} catch (SQLException exc) {
				exc.printStackTrace();
			}
		});

		// event handler for textfield to click the login Button when Enter is pressed
		passInput.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				loginButton.fire();
			}
		});
		GridPane.setConstraints(loginButton, 5, 6);
		loginLayout.getChildren().addAll(userLabel, userInput, passLabel, passInput, loginButton);
		Scene loginScene = new Scene(loginLayout, 500, 250);
		loginScene.getStylesheets().add("gui/style/css/intro-screen.css");
		primaryStage.setScene(loginScene);
		primaryStage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception, SQLException {
		startProgram(primaryStage);
	}
}
