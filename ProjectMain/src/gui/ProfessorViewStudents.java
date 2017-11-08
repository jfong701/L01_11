package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdbc.DOA;
import student.Students;

public class ProfessorViewStudents {
	public static void uploadStudents(Stage primaryStage, String user, String pass) {
		
		primaryStage.setTitle("View Students");
		
		BorderPane border = new BorderPane();
		
		//File chooser to add multiple students
		HBox fBox = new HBox();
		fBox.setPadding(new Insets(15, 12, 15, 12));
		fBox.setSpacing(10);
		
		
		Button uploadStudents = new Button("Upload Student Files...");
		uploadStudents.setOnAction( e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Upload Student File");
			List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
			if (list != null) {
				for (File file : list) {
					DOA.start();
					DOA.uploadStudentFile(file.getAbsolutePath().replace('\\', '/'));
					DOA.close();
				}
			}
		});
		
		// Add one student page
		Button addStudent = new Button("Add Student");
		addStudent.setOnAction(e -> ProfessorAddStudents.addStudents(primaryStage, user, pass));
		
		// Back to professor page
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> ProfessorPage.login(primaryStage, user, pass));

		
		fBox.getChildren().addAll(uploadStudents, addStudent, backButton);
		border.setBottom(fBox);
		Scene scene = new Scene(border, 500, 250);
		primaryStage.setScene(scene);
	}
}
