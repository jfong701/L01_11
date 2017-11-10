package gui;

import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import student.Student;

import java.util.ArrayList;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import jdbc.DOA;

public class StudentAssignmentPage {

  private static int currentRow;

  /**
   * Creates the StudentAssignmentPage scene where a student can see questions
   * and enter answers for an assignment
   *
   * @param primaryStage : the existing stage of the program
   * @param user : username of student
   * @param pass : password of student
   * @param assignmentName : name of the current assignment being filled out
   */
  public static void startAssignment(Stage primaryStage, String user,
      String pass, String assignmentName) {

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
    currentRow = 0;

    // System.out.println(DOA.getCourseIds());
    // System.out.println(DOA.getAssignmentIds("CSCC01"));

    // CHECK how many questions are in the assignment
    int numQuestions = Integer.parseInt(DOA.QuestionCount("CSCC01", "1"));

    // Extract questions to fill out labels.
    ArrayList<ArrayList<String>> questionAndAnswerList =
        DOA.getQuestions("CSCC01", "1");

    String questions[] = new String[numQuestions];
    String answers[] = new String[numQuestions];

    // Extract the questions and answers into local arrays for easier access.
    for (int i = 0; i < questionAndAnswerList.size(); i++) {
      ArrayList<String> qaPair = questionAndAnswerList.get(i);
      questions[i] = qaPair.get(0);
      answers[i] = qaPair.get(1);
    }

    // Title
    Label sceneTitle = new Label(assignmentName);
    sceneTitle.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
    grid.add(sceneTitle, 0, currentRow, 2, 1);
    currentRow++;

    // Declare arrays for question numbers, labels, input fields.
    Label questionNums[];
    Label questionLabels[];
    TextField answerFields[];

    questionNums = new Label[numQuestions];
    questionLabels = new Label[numQuestions];
    answerFields = new TextField[numQuestions];

    // Create question labels, and textFields
    for (int i = 0; i < numQuestions; i++) {
      questionNums[i] = new Label(Integer.toString(i + 1) + ".");
      grid.add(questionNums[i], 0, currentRow, 3, 1);
      currentRow++;

      questionLabels[i] = new Label(questions[i]);
      grid.add(questionLabels[i], 0, currentRow, 3, 1);
      currentRow++;

      answerFields[i] = new TextField();
      grid.add(answerFields[i], 0, currentRow, 3, 1);
      currentRow += 2;
    }

    currentRow += 5;

    Button backButton = new Button("Back");
    backButton.setOnAction(e -> gui.StudentPage.login(primaryStage, user, pass));
    grid.add(backButton, 0, currentRow);

    Button submitButton = new Button("Submit");
    submitButton.setOnAction(e -> {
      System.out.println("submitted");

      // Compare answers and output score to messageBox.
      int score = 0;

      // Compare input strings to stored answers.
      for (int i = 0; i < numQuestions; i++) {
        if (answerFields[i].getText().equals(answers[i])) {
          score++;
        }
      }

      // Display the grade in a message box.
      String message = "Score: " + Integer.toString(score) + "/"
          + Integer.toString(numQuestions);

      MessageBox.show(assignmentName, message);
    });
    grid.add(submitButton, 2, currentRow);
  }

  /**
   * A helper function to add questions to the GUI.
   *
   * @param questionNumber : Used to create Text Label Question#X
   * @param question : Text to be displayed as the question (e.g. "What is 3 +
   *        4?"
   */
  private static void addQuestions(int questionNumber, String question,
      String answer) {

  }
}
