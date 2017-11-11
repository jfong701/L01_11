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
  public static void login(Stage primaryStage, String user, String pass) {

    Label welcomeLabel = new Label("Welcome " + user);
    welcomeLabel.setPadding(new Insets(10, 10, 10, 10));
    welcomeLabel.setFont(Font.font("Verdana", 20));


    HBox topBorder = new HBox(50);
    topBorder.setAlignment(Pos.CENTER_LEFT);
    topBorder.getChildren().add(welcomeLabel);

    // Pull up a list of courses that the student is enrolled in.
    ComboBox<String> courseBox = new ComboBox<String>();
    courseBox.setPromptText("Choose a course");
    courseBox.getItems().addAll(DOA.getCourseIds());
    courseBox.setEditable(false);

    // Show list of available assignments for that course
    ComboBox<Integer> assignmentBox = new ComboBox<Integer>();
    assignmentBox.setPromptText("Choose an assignment");
    assignmentBox.setDisable(true);

    // Button to open the selected assignment
    Button viewAssignments = new Button("Open");


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


    VBox centerBorder = new VBox(50);
    centerBorder.setAlignment(Pos.CENTER);
    centerBorder.getChildren().addAll(courseBox, assignmentBox,
        viewAssignments);

    BorderPane border = new BorderPane();
    border.setTop(welcomeLabel);
    border.setCenter(centerBorder);

    primaryStage.setScene(new Scene(border, 500, 250));
  }
}
