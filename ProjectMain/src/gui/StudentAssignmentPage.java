package gui;

import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import student.Student;

import java.sql.SQLException;
import java.util.ArrayList;

import com.sun.org.apache.xpath.internal.SourceTree;

import assignment.Assignment;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
   * @param courseName: Course code eg. "CSCC01"
   * @param assignmentNumber: eg. 1 - for assignment 1.
   */
  public static void startAssignment(Stage primaryStage, String user,
      String pass, String courseName, int assignmentNumber) {
	
	String assignmentNumStr = Integer.toString(assignmentNumber);
	Assignment curAssgn = null;
    
	try {
		curAssgn = DOA.getAssignment(courseName, assignmentNumStr);
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

    int numQuestions = 2;
    String assignmentName = curAssgn.getAssignmentName();
    int questionIds[] = Assignment.questSet(numQuestions, curAssgn.getNumQuestions());
    
    ArrayList<ArrayList<String>> questionAndAnswerList = DOA.getQuestions(courseName, assignmentNumStr);

    String[] questions = new String[numQuestions];
    String[] answers = new String[numQuestions];

    // iterate through our given IDs, and extract the question and answers we need
    for (int i = 0; i < questionIds.length; i++) {
    	// gets the pairs in random order
    	ArrayList<String> qaPair = questionAndAnswerList.get(questionIds[i]);
    	questions[i] = qaPair.get(0);
    	answers[i] = qaPair.get(1);
    }

    // window title
    String title =
        user + ": " + courseName + ", " + Integer.toString(assignmentNumber);
    primaryStage.setTitle(title);

    // page title (inside window)
    Label assignmentLabel = new Label(Integer.toString(assignmentNumber));
    //Label assignmentLabel = new Label(curAssgn.getAssignmentName());
    assignmentLabel.setPadding(new Insets(10, 10, 10, 10));
    assignmentLabel.setFont(Font.font("Verdana", 20));

    HBox topBorder = new HBox(50);
    topBorder.setAlignment(Pos.CENTER_LEFT);
    topBorder.getChildren().add(assignmentLabel);


    // Create main layout
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(5, 5, 5, 5));
    grid.setVgap(5);
    grid.setHgap(5);

    currentRow = 0;    

    // Declare arrays for textFlow, and input fields.
    TextFlow flow[];
    TextField answerFields[];

    flow = new TextFlow[numQuestions];
    answerFields = new TextField[numQuestions];

    // Create question labels, and textFields
    for (int i = 0; i < numQuestions; i++) {

      // create Textflow, and add styled Text objects to it.
      flow[i] = new TextFlow();

      Text numTxt = new Text(Integer.toString(i + 1) + ".  ");
      numTxt.setStyle("-fx-font-weight: bold; -fx-font-size: 18px");

      Text questionTxt = new Text(questions[i]);

      flow[i].getChildren().addAll(numTxt, questionTxt);
      grid.add(flow[i], 0, currentRow, 2, 1);

      currentRow++;

      // add a text field to enter answers in
      answerFields[i] = new TextField();
      grid.add(answerFields[i], 0, currentRow, 2, 1);
      currentRow += 2;
    }

    currentRow += 5;

    Button backButton = new Button("Back");
    Button submitButton = new Button("Submit");

    // grid.add(backButton, 0, currentRow);
    // grid.add(submitButton, 2, currentRow);
    grid.add(backButton, 0, currentRow, 1, 1);
    grid.add(submitButton, 1, currentRow, 1, 1);

    // BACK BUTTON EVENT HANDLER
    backButton
        .setOnAction(e -> gui.StudentPage.login(primaryStage, user, pass));

    // SUBMIT BUTTON EVENT HANDLER
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

      double percentMark = (double)((double)(score)/(double)(numQuestions)) * 100;
      int percentMarkInt = (int)(percentMark);
      
      // Display the grade in a message box.
      String message = "Score: " + Integer.toString(percentMarkInt) + "%";

      MessageBox.show("Grade for " + assignmentName, message);
    });
    // embed the layout in a scrollPane
    ScrollPane sp = new ScrollPane(grid);
    sp.setFitToHeight(true);
    sp.setFitToWidth(true);

    BorderPane border = new BorderPane();
    border.setTop(topBorder);
    border.setCenter(sp);

    Scene addStudentsScene = new Scene(border, 500, 500);
    primaryStage.setScene(addStudentsScene);
  }
}
