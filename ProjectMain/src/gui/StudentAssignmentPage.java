package gui;

import javafx.stage.Stage;
import student.Student;
import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StudentAssignmentPage {
    public static void startAssignment(Stage primaryStage, String user, String pass, String assignmentName) {

      // Create layout
      //StackPane layout = new StackPane();
      GridPane grid = new GridPane();
      grid.setPadding(new Insets(15, 15, 15, 15));
      grid.setVgap(5);
      grid.setHgap(5);
      Scene addStudentsScene = new Scene(grid, 500, 250);
      primaryStage.setScene(addStudentsScene);
      int currentRow = 0;

      // Title
      Text sceneTitle = new Text(assignmentName);
      sceneTitle.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
      grid.add(sceneTitle, 0, currentRow);
      currentRow++;

      // Questions
      Text questionNumLabel = new Text("Question #1");
      grid.add(questionNumLabel, 0, currentRow);
      currentRow++;

      Text questionLabel = new Text("What is 3 + 4?");
      grid.add(questionLabel, 0, currentRow);
      currentRow++;

      TextField answerField = new TextField();
      grid.add(answerField, 0, currentRow);
      currentRow++;

      currentRow +=10;

      Button backButton = new Button("Back");
      backButton.setOnAction(e -> gui.Student.login(primaryStage, user, pass));
      grid.add(backButton, 0, currentRow);

      Button submitButton = new Button("Submit");
      submitButton.setOnAction(e -> {
          System.out.println("submitted");
      });
      grid.add(submitButton, 1, currentRow);
    }
}
