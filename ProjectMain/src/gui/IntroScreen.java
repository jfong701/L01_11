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
import javafx.geometry.Insets;

import gui.StudentPage;

public class IntroScreen extends Application {

	public static void main(String[] args) {
		launch(args);

	}
	
	@Override
	public void start(Stage primaryStage) throws Exception, SQLException {
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
			try {
				if (Validators.loginProf(userInput.getText(), passInput.getText())) {
					ProfessorPage.login(primaryStage, userInput.getText(), passInput.getText());
				} else if (Validators.loginStudent(userInput.getText(), passInput.getText())) {
					StudentPage.login(primaryStage, userInput.getText(), passInput.getText());
				} else {
					MessageBox.show("Login Failed", "Invalid Credentials");
				}
			} catch (SQLException exc) {
				exc.printStackTrace();
			}
		});
		GridPane.setConstraints(loginButton, 5, 6);
		//System.out.println(DOA.getAvg("CSCC01", 1));
		
		loginLayout.getChildren().addAll(userLabel, userInput, passLabel, passInput, loginButton);	
		Scene loginScene = new Scene(loginLayout, 500, 250);
		//loginScene.getStylesheets().add(getClass().getResource("style/css/template.css").toExternalForm());
		loginScene.getStylesheets().add("gui/style/css/template.css");
		primaryStage.setScene(loginScene);
		primaryStage.show();
	}
}
