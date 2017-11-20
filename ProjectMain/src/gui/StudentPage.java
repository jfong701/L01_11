package gui;

import javafx.scene.Scene;
import javafx.stage.Stage;
import jdbc.DOA;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class StudentPage {

  private static int currentRow;
  private static final int LEFT_WIDTH = 2;
  private static final int RIGHT_WIDTH = 2;
  private static final int LEFT_COL_INDEX = 0;
  private static final int RIGHT_COL_INDEX = LEFT_WIDTH + 1;
  private static final int ROW_HEIGHT = 5;

  public static void login(Stage primaryStage, String user, String pass) {
    primaryStage.setTitle(user);

    Label welcomeLabel = new Label("Welcome " + user);
    welcomeLabel.setPadding(new Insets(10, 10, 10, 10));
    welcomeLabel.setFont(Font.font("Verdana", 20));

    HBox topBorder = new HBox(50);
    topBorder.setAlignment(Pos.CENTER_LEFT);
    topBorder.getChildren().add(welcomeLabel);

    // Create grid layout
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(5, 15, 15, 15));
    grid.setVgap(5);
    grid.setHgap(5);
    currentRow = 0;

    // Label for course selection comboBox
    Label courseLabel = new Label("Choose a course:");
    grid.add(courseLabel, LEFT_COL_INDEX, currentRow, LEFT_WIDTH, ROW_HEIGHT);

    // Pull up a list of courses that the student is enrolled in.
    ComboBox<String> courseBox = new ComboBox<String>();
    courseBox.setPromptText("Choose a course");
    courseBox.getItems().addAll(DOA.getCourseIds());
    courseBox.setEditable(false);
    grid.add(courseBox, RIGHT_COL_INDEX, currentRow, RIGHT_WIDTH, ROW_HEIGHT);
    currentRow += ROW_HEIGHT;

    // Label for assignment selection comboBox
    Label assignmentLabel = new Label("Choose an assignment:");
    grid.add(assignmentLabel, LEFT_COL_INDEX, currentRow, LEFT_WIDTH,
        ROW_HEIGHT);

    // Show list of available assignments for that course
    ComboBox<Integer> assignmentBox = new ComboBox<Integer>();
    assignmentBox.setPromptText("Choose an assignment");
    assignmentBox.setDisable(true);

    grid.add(assignmentBox, RIGHT_COL_INDEX, currentRow, RIGHT_WIDTH,
        ROW_HEIGHT);
    // add extra space to separate the 'Open' button
    currentRow += ROW_HEIGHT + ROW_HEIGHT;

    // label to indicate the deadline for an assignment
    // filled in when an assignment is selected
    Label deadlineLabel = new Label();
    deadlineLabel.setStyle("-fx-font-weight: bold");
    grid.add(deadlineLabel, LEFT_COL_INDEX, currentRow, LEFT_WIDTH,
        ROW_HEIGHT);

    // Button to open the selected assignment
    Button viewAssignmentsBtn = new Button("Open");
    grid.add(viewAssignmentsBtn, RIGHT_COL_INDEX, currentRow, RIGHT_WIDTH,
        ROW_HEIGHT);

    currentRow += ROW_HEIGHT;

    // Label to show the course average for an assignment.
    Label assignmentAverageLabel = new Label();
    grid.add(assignmentAverageLabel, LEFT_COL_INDEX, currentRow, LEFT_WIDTH,
        ROW_HEIGHT);

    // EVENT HANDLERS BELOW:

    // COURSEBOX EVENT HANDLER
    // when a course is selected, sets up the assignmentBox for that course.
    courseBox.setOnAction(e -> {
      assignmentBox.getItems().clear(); // clear any old values

      // gets the value of the courseBox (chosen course), then fills assignment
      // box options
      assignmentBox.getItems()
          .addAll(DOA.getAssignmentIds(courseBox.getValue()));

      assignmentBox.setEditable(false);
      assignmentBox.setDisable(false);
    });

    // ASSIGNMENTBOX EVENT HANDLER
    // when an assignment is chosen, enable the 'open' button
    assignmentBox.setOnAction(e -> {
      viewAssignmentsBtn.setDisable(false);
      // deadlineLabel.setText("Due: November 17, 2017, at 5:30 pm");
      // assignmentAverageLabel.setText("Assignment average: 36.12%");
    });

    // 'OPEN' BUTTON EVENT HANDLER
    viewAssignmentsBtn.setDisable(true);
    viewAssignmentsBtn.setOnAction(e -> {

      String assignmentName = courseBox.getValue();
      int assignmentNumber = assignmentBox.getValue();

      StudentAssignmentPage.startAssignment(primaryStage, user, pass,
          assignmentName, assignmentNumber);
    });

    BorderPane border = new BorderPane();
    border.setTop(topBorder);
    border.setCenter(grid);

    primaryStage.setScene(new Scene(border, 500, 250));
  }
}
