package gui;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jdbc.DOA;
import user.Student;

public class ProfessorAddStudents {
	
    public static void addStudents(Stage primaryStage, String user, String pass) {
        // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.getStyleClass().add("border-no-overlay");
        Scene addStudentsScene = new Scene(grid, 500, 250);
        addStudentsScene.getStylesheets().add("gui/style/css/professor-style.css");
        primaryStage.setScene(addStudentsScene);

        // Title
        Text sceneTitle = new Text("Add Students");
        sceneTitle.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 1, 1);

        // Labels and TextFields
        // Student Name
        Label studentNameLabel = new Label("Enter a student's name");
        grid.add(studentNameLabel, 0, 2, 1, 1);

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        grid.add(firstNameField, 0, 3, 2, 1);

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        grid.add(lastNameField, 2, 3, 2, 1);

        // Student Number
        Label studentNumLabel = new Label("Enter a student number");
        grid.add(studentNumLabel, 0, 5, 1, 1);

        TextField studentNumField = new TextField();
        studentNumField.setPromptText("e.g. 1001234567");
        grid.add(studentNumField, 0, 6, 2, 1);
        
        // UTORid
        Label uTORidLabel = new Label("Enter a student UTORid");
        grid.add(uTORidLabel, 2, 5, 1, 1);

        TextField uTORidField = new TextField();
        uTORidField.setPromptText("e.g. abcdefg1");
        grid.add(uTORidField, 2, 6, 2, 1);


        // Buttons
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> ProfessorViewStudents.uploadStudents(primaryStage, user, pass));
        grid.add(backButton, 0, 10, 1, 1);
        
        

        // TO-DO: ADD LOGIC TO SUBMIT BUTTON
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
        	
        	String stuNum = studentNumField.getText();
        	String utor_id = uTORidField.getText();
        	String first = firstNameField.getText();
        	String last = lastNameField.getText();
        	
        	// ADDED "password" TO MATCH NEW CONSTRUCTOR. CHANGE IF NECESSARY.
        	Student student = new Student(stuNum, utor_id, first, last, "password");
        	DOA.addStudent(stuNum, utor_id, first, last);
            // .getText() from firstNameField, lastNameField, studentNumField
        	MessageBox.show("New Student Added",
        			student.getStudentNo() + ": " + student.getStudentFirstName() + " " + student.getStudentLastName());

        });
        grid.add(submitButton, 2, 10, 1, 1);

    }
}
