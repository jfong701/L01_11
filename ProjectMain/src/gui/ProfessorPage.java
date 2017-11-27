package gui;


import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class ProfessorPage {
	public static void login(Stage primaryStage, String user, String pass) {
		
		Label welcomeLabel = new Label("Welcome " + user);
		welcomeLabel.setPadding(new Insets(10, 10, 10, 10));
		welcomeLabel.setFont(Font.font("Verdana", 20));
		welcomeLabel.setId("headerLabel");
		
		StackPane logoutStack = new StackPane();
		Button logoutBtn = new Button("Logout");
		logoutBtn.setOnAction(e -> IntroScreen.startProgram(primaryStage));
		logoutStack.getChildren().addAll(logoutBtn);
		logoutStack.setAlignment(Pos.CENTER_RIGHT);
		StackPane.setMargin(logoutBtn, new Insets(0, 10, 0, 0));
		
		HBox topBorder = new HBox(50);
		topBorder.setAlignment(Pos.CENTER_LEFT);
		topBorder.getChildren().add(welcomeLabel);
		topBorder.getChildren().add(logoutStack);
		topBorder.getStyleClass().add("hbox");
		HBox.setHgrow(logoutStack, Priority.ALWAYS);

		Button addProfessors = new Button("Add Professors");
		addProfessors.setOnAction(e -> ProfessorAddProfessors.addProfessors(primaryStage, user, pass));		
		Button addStudents = new Button("Manage Student Information");
		addStudents.setOnAction(e -> ProfessorViewStudents.uploadStudents(primaryStage, user, pass));
		Button addAssignments = new Button("Add Assignments");
		addAssignments.setOnAction(e -> ProfessorAddAssignments.addAssignments(primaryStage, user, pass));
		Button addQuestions = new Button("Add Questions");
		addQuestions.setOnAction(e -> ProfessorAddQuestions.addQuestions(primaryStage, user, pass));

		
		VBox centerBorder = new VBox(25);
		centerBorder.setAlignment(Pos.CENTER);
		centerBorder.getChildren().addAll(addProfessors, addStudents, addAssignments, addQuestions);
		centerBorder.getStyleClass().add("grid");
		
		BorderPane border = new BorderPane();
		border.setTop(topBorder);
		border.setCenter(centerBorder);
		border.getStyleClass().add("border");

		Scene professorScene = new Scene(border, 500, 250);
		professorScene.getStylesheets().add("gui/style/css/professor-style.css");
		primaryStage.setScene(professorScene);
		addProfessors.requestFocus();
	}
}
