package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import assignment.Assignment;
import assignment.SingleAnswerQuestion;
import jdbc.DOA;
import user.Student;
import validator.Validators;

public class ProfessorAddAssignments {
	
	private static TableView<Assignment> assignments;

    public static void addAssignments(Stage primaryStage, String user, String pass) {
    	
    	// Create layout
    	primaryStage.setTitle("View Assignments");
    	
    	BorderPane border = new BorderPane();
    	
    	setUpTable();
    	
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
		Button uploadAssignments = new Button("Upload Assignment Files...");
		uploadAssignments.setOnAction( e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Upload Assignment File");
			List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
			if (list != null) {
				for (File file : list) {
					List<String> errors = Validators.getErrorsInAssignmentFile(file.getAbsolutePath().replace('\\','/'));
					if (!errors.isEmpty()) {
						String error = "";
						for (String i : errors) error = error + i + '\n';
						MessageBox.show("Error", error);
					} else {
						DOA.uploadAssignmentFile(file.getAbsolutePath().replace('\\', '/'));
					}
				}
			}
			loadTable("");
		});
		grid.add(uploadAssignments, 0, 0, 1, 1);
        

        // Labels and TextFields
        // Course Label
        Label courseLabel = new Label("Enter course id:");
        grid.add(courseLabel, 0, 1, 1, 1);

        ComboBox<String> courseBox = new ComboBox<String>();
        courseBox.setPromptText("e.g CSCC43");
        courseBox.getItems().addAll(DOA.getCourseIds());
        courseBox.setEditable(true);
        courseBox.setOnAction( e -> loadTable(courseBox.getValue()));
        grid.add(courseBox, 0, 2, 1, 1);

        // Assignment Number label
        Label aIDLabel = new Label("Enter assignment id");
        grid.add(aIDLabel, 1, 1, 1, 1);

        TextField aIDField = new TextField();
        aIDField.setPromptText("e.g. 2");
        grid.add(aIDField, 1, 2, 1, 1);
        
        // Number Questions label
        Label qNumLabel = new Label("Enter number of questions");
        grid.add(qNumLabel, 2, 1, 1, 1);

        TextField qNumField = new TextField();
        qNumField.setPromptText("e.g. 7");
        grid.add(qNumField, 2, 2, 1, 1);
        
        // Assignment Name label
        Label aNameLabel = new Label("Enter assignment name");
        grid.add(aNameLabel, 0, 4, 1, 1);

        TextField aNameField = new TextField();
        aNameField.setPromptText("e.g. Assignment 1");
        grid.add(aNameField, 0, 5, 2, 1);
        
        // Deadline label
        Label deadlineLabel = new Label("Enter deadline");
        grid.add(deadlineLabel, 2, 4, 1, 1);

        DatePicker datePicker = new DatePicker();
        grid.add(datePicker, 2, 5, 1, 1);

        // Buttons
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> ProfessorPage.login(primaryStage, user, pass));
        grid.add(backButton, 0, 9, 1, 1);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
        	try {
				if (!Validators.isAssignmentValid(courseBox.getValue() == null ? "":courseBox.getValue(), aIDField.getText(), 
						qNumField.getText(), aNameField.getText(), datePicker.getValue())) {
					MessageBox.show("Error", "Invalid input for assignments.");
				} else {
		        	DOA.addAssignment(
		        			courseBox.getValue(),
		        			aIDField.getText(),
		        			qNumField.getText(),
		        			aNameField.getText(),
		        			java.sql.Date.valueOf(datePicker.getValue()));
		        	aIDField.clear();
		        	qNumField.clear();
		        	aNameField.clear();
		        	loadTable(courseBox.getValue());
				}
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        grid.add(submitButton, 2, 9, 1, 1);
        border.setCenter(assignments);
        border.setBottom(grid);
        border.getStyleClass().add("border-no-overlay");
        
        Scene addAssignmentsScene = new Scene(border, 750, 500);
        addAssignmentsScene.getStylesheets().add("gui/style/css/professor-style.css");
        primaryStage.setScene(addAssignmentsScene);
    }
    
	public static ObservableList<Assignment> getAssignments(String course_id) throws SQLException {
		ObservableList<Assignment> asmts = null;
		if (course_id.equals("")) {
			asmts = FXCollections.observableArrayList(DOA.getAllAssignments());
		} else {
			asmts = FXCollections.observableArrayList(DOA.getAllAssignments(course_id));
		}
		return asmts;
	}
	

	public static void loadTable(String course_id) {
		assignments.getItems().clear();
		try {
			assignments.setItems(getAssignments(course_id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    
	public static void setUpTable() {
		assignments = new TableView<Assignment>();
		
		// Table Columns
		TableColumn<Assignment, String> courseID = new TableColumn<>("Course ID");
		courseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
		
		TableColumn<Assignment, Integer> assignmentID = new TableColumn<>("Assignment ID");
		assignmentID.setCellValueFactory(new PropertyValueFactory<>("assignmentID"));
		
		TableColumn<Assignment, Integer> numQuestions = new TableColumn<>("Number of Questions");
		numQuestions.setCellValueFactory(new PropertyValueFactory<>("numQuestions"));
		
		TableColumn<Assignment, String> assignmentName = new TableColumn<>("Assignment Name");
		assignmentName.setCellValueFactory(new PropertyValueFactory<>("assignmentName"));
		
		TableColumn<Assignment, Date> deadline = new TableColumn<>("Deadline");
		deadline.setCellValueFactory(new PropertyValueFactory<>("deadline"));
		
		courseID.prefWidthProperty().bind(assignments.widthProperty().divide(5));
		assignmentID.prefWidthProperty().bind(assignments.widthProperty().divide(5));
		numQuestions.prefWidthProperty().bind(assignments.widthProperty().divide(5));
		assignmentName.prefWidthProperty().bind(assignments.widthProperty().divide(5));
		deadline.prefWidthProperty().bind(assignments.widthProperty().divide(5));

		
		assignments.getColumns().addAll(courseID, assignmentID, numQuestions, assignmentName, deadline);
		assignments.setFixedCellSize(25);
		loadTable("");

	}
}
