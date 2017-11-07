package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import assignment.Question;
import jdbc.DOA;

public class ProfessorAddAssignments {
	

    public static void addAssignments(Stage primaryStage, String user, String pass) {
        // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene addAssignmentsScene = new Scene(grid, 500, 250);
        primaryStage.setScene(addAssignmentsScene);
        
        // Title
        Text sceneTitle = new Text("Add Assignments");
        sceneTitle.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 1, 1);

        // Labels and TextFields
        // Course Label
        Label courseLabel = new Label("Enter course id:");
        grid.add(courseLabel, 0, 2, 1, 1);

        TextField courseField = new TextField();
        courseField.setPromptText("e.g CSCC43");
        grid.add(courseField, 0, 3, 1, 1);

        // Assignment Number label
        Label aIDLabel = new Label("Enter assignment id");
        grid.add(aIDLabel, 1, 2, 1, 1);

        TextField aIDField = new TextField();
        aIDField.setPromptText("e.g. 2");
        grid.add(aIDField, 1, 3, 1, 1);
        
        // Number Questions label
        Label qNumLabel = new Label("Enter number of questions");
        grid.add(qNumLabel, 2, 2, 1, 1);

        TextField qNumField = new TextField();
        qNumField.setPromptText("e.g. 7");
        grid.add(qNumField, 2, 3, 1, 1);
        
        // Assignment Name label
        Label aNameLabel = new Label("Enter assignment name");
        grid.add(aNameLabel, 0, 5, 1, 1);

        TextField aNameField = new TextField();
        aNameField.setPromptText("e.g. Assignment 1");
        grid.add(aNameField, 0, 6, 2, 1);
        
        // Deadline label
        Label deadlineLabel = new Label("Enter deadline");
        grid.add(deadlineLabel, 2, 5, 1, 1);

        DatePicker datePicker = new DatePicker();
        grid.add(datePicker, 2, 6, 1, 1);

        // Buttons
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> ProfessorPage.login(primaryStage, user, pass));
        grid.add(backButton, 0, 10, 1, 1);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {

        	DOA.start();
        	DOA.addAssignment(
        			courseField.getText(),
        			aIDField.getText(),
        			Integer.toString(5),
        			aNameField.getText(),
        			java.sql.Date.valueOf(datePicker.getValue()));
        	DOA.close();
        	

        });
        grid.add(submitButton, 2, 10, 1, 1);

    }
}
