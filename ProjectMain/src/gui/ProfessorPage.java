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
		
		HBox topBorder = new HBox(50);
		topBorder.setAlignment(Pos.CENTER_LEFT);
		topBorder.getChildren().add(welcomeLabel);

		Button addStudents = new Button("Manage Student Information");
		addStudents.setOnAction(e -> ProfessorViewStudents.uploadStudents(primaryStage, user, pass));
		Button addAssignments = new Button("Add Assignments");
		addAssignments.setOnAction(e -> ProfessorAddAssignments.addAssignments(primaryStage, user, pass));
		Button addQuestions = new Button("Add Questions");
		addQuestions.setOnAction(e -> ProfessorAddQuestions.addQuestions(primaryStage, user, pass));

		
		VBox centerBorder = new VBox(50);
		centerBorder.setAlignment(Pos.CENTER);
		centerBorder.getChildren().addAll(addStudents, addAssignments, addQuestions);
		
		BorderPane border = new BorderPane();
		border.setTop(welcomeLabel);
		border.setCenter(centerBorder);

		primaryStage.setScene(new Scene(border, 500, 250));
	}
}
