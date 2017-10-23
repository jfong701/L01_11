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

public class ProfessorAddAssignments {

    public static void addAssignments(Stage primaryStage, String user, String pass) {
        // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene addStudentsScene = new Scene(grid, 500, 250);
        primaryStage.setScene(addStudentsScene);

        // Title
        Text sceneTitle = new Text("Add Questions");
        sceneTitle.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 1, 1);

        // Labels and TextFields
        // Question Label
        Label questionLabel = new Label("Enter a question");
        grid.add(questionLabel, 0, 2, 1, 1);

        TextField questionField = new TextField();
        questionField.setPromptText("e.g. What is 3 + 4");
        grid.add(questionField, 0, 3, 3, 1);

        // Answer label
        Label answerLabel = new Label("Enter the answer to the question");
        grid.add(answerLabel, 0, 5, 1, 1);

        TextField answerField = new TextField();
        answerField.setPromptText("e.g. 7");
        grid.add(answerField, 0, 6, 3, 1);

        // Buttons
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> Professor.login(primaryStage, user, pass));
        grid.add(backButton, 0, 10, 1, 1);

        // TO-DO: ADD LOGIC TO SUBMIT BUTTON
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {

            // .getText() from questionField, answerField

        });
        grid.add(submitButton, 2, 10, 1, 1);

    }
}
