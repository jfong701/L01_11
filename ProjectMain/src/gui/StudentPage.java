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
  private static final int LEFT_COL_INDEX = 0;
  private static final int BOX_WIDTH = 2;
  private static final int LABEL_WIDTH = 2;
  private static final int RIGHT_COL_INDEX = LABEL_WIDTH + 1;
  private static final int ROW_HEIGHT = 10;

  public static void login(Stage primaryStage, String user, String pass) {

    Label welcomeLabel = new Label("Welcome " + user);
    welcomeLabel.setPadding(new Insets(10, 10, 10, 10));
    welcomeLabel.setFont(Font.font("Verdana", 20));

    HBox topBorder = new HBox(50);
    topBorder.setAlignment(Pos.CENTER_LEFT);
    topBorder.getChildren().add(welcomeLabel);

    // Create grid layout
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    grid.setPadding(new Insets(15, 15, 15, 15));
    grid.setVgap(5);
    grid.setHgap(5);
    currentRow = 0;

    // Label for course selection comboBox
    Label courseLabel = new Label("Choose a course:");
    grid.add(courseLabel, LEFT_COL_INDEX, currentRow, LABEL_WIDTH, ROW_HEIGHT);

    // Pull up a list of courses that the student is enrolled in.
    ComboBox<String> courseBox = new ComboBox<String>();
    courseBox.setPromptText("Choose a course");
    courseBox.getItems().addAll(DOA.getCourseIds());
    courseBox.setEditable(false);
    grid.add(courseBox, RIGHT_COL_INDEX, currentRow, BOX_WIDTH, ROW_HEIGHT);
    currentRow += ROW_HEIGHT;

    // Label for assignment selection comboBox
    Label assignmentLabel = new Label("Choose an assignment:");
    grid.add(assignmentLabel, LEFT_COL_INDEX, currentRow, LABEL_WIDTH,
        ROW_HEIGHT);

    // Show list of available assignments for that course
    ComboBox<Integer> assignmentBox = new ComboBox<Integer>();
    assignmentBox.setPromptText("Choose an assignment");
    assignmentBox.setDisable(true);

    grid.add(assignmentBox, RIGHT_COL_INDEX, currentRow, BOX_WIDTH,
        ROW_HEIGHT);
    currentRow += ROW_HEIGHT;

    // Button to open the selected assignment
    Button viewAssignments = new Button("Open");
    grid.add(viewAssignments, RIGHT_COL_INDEX, currentRow, BOX_WIDTH,
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
      viewAssignments.setDisable(false);
    });

    // 'OPEN' BUTTON EVENT HANDLER
    viewAssignments.setDisable(true);
    viewAssignments.setOnAction(e -> {

      String assignmentName = courseBox.getValue();
      int assignmentNumber = assignmentBox.getValue();

      StudentAssignmentPage.startAssignment(primaryStage, user, pass,
          assignmentName, assignmentNumber);
    });

    /*
     * VBox centerBorder = new VBox(50); centerBorder.setAlignment(Pos.CENTER);
     * centerBorder.getChildren().addAll(courseBox, assignmentBox,
     * viewAssignments);
     */

    BorderPane border = new BorderPane();
    border.setTop(welcomeLabel);
    border.setCenter(grid);

    primaryStage.setScene(new Scene(border, 500, 250));
  }
}
