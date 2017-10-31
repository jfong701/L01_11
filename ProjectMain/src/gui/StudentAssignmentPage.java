package gui;

import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import student.Student;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StudentAssignmentPage {

    /**
     * Creates the StudentAssignmentPage scene where a student can see questions and enter answers for an assignment
     *
     * @param primaryStage   : the existing stage of the program
     * @param user           : username of student
     * @param pass           : password of student
     * @param assignmentName : name of the current assignment being filled out
     */
    public static void startAssignment(Stage primaryStage, String user, String pass, String assignmentName) {

        // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(15, 15, 15, 15));
        grid.setVgap(5);
        grid.setHgap(5);

        ScrollPane sp = new ScrollPane(grid);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);

        Scene addStudentsScene = new Scene(sp, 500, 250);
        primaryStage.setScene(addStudentsScene);
        int currentRow = 0;

        // Title
        Text sceneTitle = new Text(assignmentName);
        sceneTitle.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, currentRow, 2, 1);
        currentRow++;

        // Questions
        Text questionNumLabel = new Text("1.");
        grid.add(questionNumLabel, 0, currentRow, 2, 1);
        currentRow++;

        Text questionLabel = new Text("What is 3 + 4?");
        grid.add(questionLabel, 0, currentRow,2, 1);
        currentRow++;

        TextField answerField = new TextField();
        grid.add(answerField, 0, currentRow, 2, 1);
        currentRow++;

        // copy-pasted for layout testing --> convert to loop in future
        Text questionNumLabel2 = new Text("2.");
        grid.add(questionNumLabel2, 0, currentRow, 2, 1);
        currentRow++;

        Text questionLabel2 = new Text("What is 3 + 4? ");
        grid.add(questionLabel2, 0, currentRow,2 , 1);
        currentRow++;

        TextField answerField2 = new TextField();
        grid.add(answerField2, 0, currentRow, 2, 1);
        currentRow++;

        Text questionNumLabel3 = new Text("3.");
        grid.add(questionNumLabel3, 0, currentRow, 2, 1);
        currentRow++;

        Text questionLabel3 = new Text("What is 3 + 4?");
        grid.add(questionLabel3, 0, currentRow, 2, 1);
        currentRow++;

        TextField answerField3 = new TextField();
        grid.add(answerField3, 0, currentRow, 2, 1);
        currentRow++;

        currentRow += 5;

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> gui.Student.login(primaryStage, user, pass));
        grid.add(backButton, 0, currentRow);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            System.out.println("submitted");
        });
        grid.add(submitButton, 1, currentRow);
    }

    /**
     * A helper function to add questions to the GUI.
     *
     * @param questionNumber : Used to create Text Label Question#X
     * @param question       : Text to be displayed as the question (e.g. "What is 3 + 4?"
     */
    private static void addQuestions(int questionNumber, String question) {

    }
}
