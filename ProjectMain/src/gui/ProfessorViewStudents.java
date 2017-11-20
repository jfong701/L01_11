package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jdbc.DOA;
import student.Student;
import student.Students;

public class ProfessorViewStudents {
	
	private static TableView<Student> students;
	private static ComboBox<String> courseBox;
	
	public static void uploadStudents(Stage primaryStage, String user, String pass) {
		
		primaryStage.setTitle("View Students");
		
		BorderPane border = new BorderPane();
		
		// Choose which course to add students to.
        courseBox = new ComboBox<>();
        courseBox.getItems().add("All Courses");
        courseBox.getItems().addAll(DOA.getCourseIds());
        courseBox.setValue("All Courses");
		courseBox.setOnAction(e -> {
			loadTable();
		});
		// Student Table View
		setUpTable();
				
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
					// check for any errors in the student file using the method in DOA
					List<String> errors = DOA.getErrorsInStudentFile(file.getAbsolutePath().replace('\\', '/'));
					String error = "";
					// if there is at least one string in the list of errors, then show a message box containing the errors
					if (errors.size() > 0) {
						for (int i=0; i<errors.size(); i++) {
							error = error + errors.get(i) + '\n';
						}
						MessageBox.show("Error", error);
					} else {
						DOA.uploadStudentFile(file.getAbsolutePath().replace('\\', '/'));
						try {
							DOA.uploadCourseStudents(courseBox.getValue(), file);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					DOA.close();
				}
			}
			loadTable();
		});
		uploadStudents.disableProperty().bind(courseBox.valueProperty().isEqualTo("All Courses"));
		
		// Add one student page
		Button addStudent = new Button("Add Student");
		addStudent.setOnAction(e -> ProfessorAddStudents.addStudents(primaryStage, user, pass));
		addStudent.disableProperty().bind(courseBox.valueProperty().isEqualTo("All Courses"));

		
		// Back to professor page
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> ProfessorPage.login(primaryStage, user, pass));
        	
		fBox.getChildren().addAll(uploadStudents, addStudent, backButton);
		border.setTop(courseBox);
		border.setBottom(fBox);
		border.setCenter(students);
		Scene scene = new Scene(border, 500, 250);
		primaryStage.setScene(scene);
	}
	
	
	public static ObservableList<Student> getStudents() {
		ObservableList<Student> students = null;
		if (courseBox.getValue().equals("All Courses")) {
			students = FXCollections.observableArrayList(DOA.getAllStudents());
		} else {
			students = FXCollections.observableArrayList(DOA.getStudentsFromCourse(courseBox.getValue()));
		}
		return students;
	}
	
	public static void setUpTable() {
students = new TableView<Student>();
		
		// Table Columns
		TableColumn<Student, String> studentNumber = new TableColumn<>("Student Number");
		studentNumber.setMinWidth(125);
		studentNumber.setCellValueFactory(new PropertyValueFactory<>("studentNo"));
		
		TableColumn<Student, String> studentUTOR = new TableColumn<>("UTORid");
		studentUTOR.setMinWidth(125);
		studentUTOR.setCellValueFactory(new PropertyValueFactory<>("studentUTORID"));
		
		TableColumn<Student, String> studentFirstName = new TableColumn<>("First Name");
		studentFirstName.setMinWidth(125);
		studentFirstName.setCellValueFactory(new PropertyValueFactory<>("studentFirstName"));
		
		TableColumn<Student, String> studentLastName = new TableColumn<>("Last Name");
		studentLastName.setMinWidth(125);
		studentLastName.setCellValueFactory(new PropertyValueFactory<>("studentLastName"));
		
		students.getColumns().addAll(studentNumber, studentUTOR, studentFirstName, studentLastName);
		students.setFixedCellSize(25);
		loadTable();

	}
	
	public static void loadTable() {
		students.getItems().clear();
		students.setItems(getStudents());
	}
}
