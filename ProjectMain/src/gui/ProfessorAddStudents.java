package gui;

import com.sun.org.apache.xpath.internal.SourceTree;
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

import javax.xml.bind.SchemaOutputResolver;
import java.awt.*;

public class ProfessorAddStudents {

    public static void addStudents(Stage primaryStage, String user, String pass) {
        // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene addStudentsScene = new Scene(grid, 500, 250);
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

        // Buttons
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> Professor.login(primaryStage, user, pass));
        grid.add(backButton, 0, 10, 1, 1);

        // TO-DO: ADD LOGIC TO SUBMIT BUTTON
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {

            // .getText() from firstNameField, lastNameField, studentNumField

        });
        grid.add(submitButton, 2, 10, 1, 1);

    }
}
