package gui;

import assignment.Assignment;
import jdbc.DOA;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class StudentPage {

	private static int currentRow;
	private static final int LEFT_WIDTH = 2;
	private static final int RIGHT_WIDTH = 2;
	private static final int LEFT_COL_INDEX = 0;
	private static final int RIGHT_COL_INDEX = LEFT_WIDTH + 1;
	private static final int ROW_HEIGHT = 5;
	private static final int ROW_HEIGHT_SMALL = 1;
	private static final long LENGTH_OF_UNIX_DAY = 86399000;
	private static ObservableList<Integer> assignmentIds;
	private static ObservableList<String> courseCodes;

	public static void login(Stage primaryStage, String user, String pass) {
		primaryStage.setTitle(user);

		Label welcomeLabel = new Label("Welcome " + user);
		welcomeLabel.setPadding(new Insets(10, 10, 10, 10));
		welcomeLabel.setFont(Font.font("Verdana", 20));
		welcomeLabel.setId("headerLabel");

		StackPane logoutStack = new StackPane();
		Button logoutBtn = new Button("Logout");
		logoutStack.getChildren().addAll(logoutBtn);
		logoutStack.setAlignment(Pos.CENTER_RIGHT);
		StackPane.setMargin(logoutBtn, new Insets(0, 10, 0, 0));

		HBox topBorder = new HBox(50);
		topBorder.setAlignment(Pos.CENTER_LEFT);
		topBorder.getChildren().add(welcomeLabel);
		topBorder.getChildren().add(logoutStack);
		topBorder.getStyleClass().add("hbox");
		HBox.setHgrow(logoutStack, Priority.ALWAYS);

		// Create grid layout
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(5, 15, 15, 15));
		grid.setVgap(5);
		grid.setHgap(5);
		grid.getStyleClass().add("grid");
		currentRow = 0;

		// keep assignment ids for a course in an observable list. (useful for
		// combo boxes which interact with each other)
		assignmentIds = FXCollections.observableArrayList();
		courseCodes = FXCollections.observableArrayList();

		// set course codes from database
		courseCodes.addAll(DOA.getCourseIds());

		// Label for course selection comboBox
		Label courseLabel = new Label("Choose a course:");
		grid.add(courseLabel, LEFT_COL_INDEX, currentRow, LEFT_WIDTH, ROW_HEIGHT);

		// ComboBox to select list of courses the student is enrolled in.
		ComboBox<String> courseBox = new ComboBox<String>();
		courseBox.setPromptText("Choose a course");
		courseBox.setItems(courseCodes);
		courseBox.setEditable(false);
		grid.add(courseBox, RIGHT_COL_INDEX, currentRow, RIGHT_WIDTH, ROW_HEIGHT);
		currentRow += ROW_HEIGHT;

		// Label for assignment selection comboBox
		Label assignmentLabel = new Label("Choose an assignment:");
		grid.add(assignmentLabel, LEFT_COL_INDEX, currentRow, LEFT_WIDTH, ROW_HEIGHT);

		// ComboBox to select available assignments for that course
		ComboBox<Integer> assignmentBox = new ComboBox<Integer>();
		assignmentBox.setPromptText("Choose an assignment");

		// set assignmentBox to use items from Observable.
		assignmentBox.setItems(assignmentIds);
		assignmentBox.setEditable(false);
		assignmentBox.setDisable(true);

		grid.add(assignmentBox, RIGHT_COL_INDEX, currentRow, RIGHT_WIDTH, ROW_HEIGHT);
		// add extra space to separate the 'Open' button
		currentRow += ROW_HEIGHT + ROW_HEIGHT;

		// label to indicate the deadline for an assignment
		// filled in when an assignment is selected
		Label deadlineLabel = new Label();
		deadlineLabel.setStyle("-fx-font-weight: bold");
		grid.add(deadlineLabel, LEFT_COL_INDEX, currentRow, LEFT_WIDTH, ROW_HEIGHT_SMALL);

		// use smaller spacing between info labels
		currentRow += ROW_HEIGHT_SMALL;

		// Label to show the course average for an assignment.
		Label assignmentAverageLabel = new Label();
		assignmentAverageLabel.setVisible(false);
		grid.add(assignmentAverageLabel, LEFT_COL_INDEX, currentRow, LEFT_WIDTH, ROW_HEIGHT_SMALL);

		currentRow += ROW_HEIGHT_SMALL;

		// Label that shows the student's current mark on that assignment.
		Label assignmentCurrentGradeLabel = new Label();
		assignmentCurrentGradeLabel.setVisible(false);
		grid.add(assignmentCurrentGradeLabel, LEFT_COL_INDEX, currentRow, LEFT_WIDTH, ROW_HEIGHT_SMALL);

		// Button to open the selected assignment
		Button viewAssignmentsBtn = new Button("Open");
		viewAssignmentsBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		grid.add(viewAssignmentsBtn, RIGHT_COL_INDEX, currentRow, RIGHT_WIDTH, ROW_HEIGHT_SMALL);

		// EVENT HANDLERS BELOW:

		// COURSEBOX EVENT HANDLER
		// when a course is selected, sets up the assignmentBox for that course.
		courseBox.setOnAction(e -> {

			assignmentIds.clear();
			assignmentIds.addAll(DOA.getAssignmentIds(courseBox.getValue()));

			assignmentBox.setDisable(false);

			// ensure assignment-specific labels are hidden until assignment is chosen.
			deadlineLabel.setVisible(false);
			assignmentAverageLabel.setVisible(false);
			assignmentCurrentGradeLabel.setVisible(false);
		});

		// ASSIGNMENTBOX EVENT HANDLER
		// when an assignment is chosen, enable the 'open' button and show labels.
		assignmentBox.setOnAction(e -> {

			// only update the label if the observable object hasn't been filled
			// (prevents concurrency issues within the UI)
			if (!assignmentIds.isEmpty()) {

				Date deadline;

				Assignment curAssgn = null;
				try {
					curAssgn = DOA.getAssignment(courseBox.getValue(), (assignmentBox.getValue()).toString());

					deadline = curAssgn.getDeadline();

					// add 23 hrs, 59 minutes, 59 seconds to the deadline, so students
					// can submit up to 11:59 pm of the deadline day
					deadline.setTime(deadline.getTime() + LENGTH_OF_UNIX_DAY);
					deadlineLabel.setText("Deadline: " + deadline.toString());
					deadlineLabel.setVisible(true);

					if (Calendar.getInstance().getTime().before(deadline)) {
						viewAssignmentsBtn.setDisable(false);
					} else {
						viewAssignmentsBtn.setDisable(true);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
					MessageBox.show("Database Error", "Cannot retrieve assignment.\nPlease try again.");
				}

				int avgGrade = DOA.getAvg(courseBox.getValue(), assignmentBox.getValue());
				assignmentAverageLabel.setText("Class assignment average: " + Integer.toString(avgGrade) + "%");
				assignmentAverageLabel.setVisible(true);

				int currentGrade = DOA.getMark(user, courseBox.getValue(), assignmentBox.getValue());
				assignmentCurrentGradeLabel.setText("Your assignment grade:  " + Integer.toString(currentGrade) + "%");
				assignmentCurrentGradeLabel.setVisible(true);
			}
		});

		// 'OPEN' BUTTON EVENT HANDLER
		viewAssignmentsBtn.setDisable(true);
		viewAssignmentsBtn.setOnAction(e -> {

			String assignmentName = courseBox.getValue();
			int assignmentNumber = assignmentBox.getValue();

			StudentAssignmentPage.startAssignment(primaryStage, user, pass, assignmentName, assignmentNumber);
		});

		// LOGOUT BUTTON EVENT HANDLER
		logoutBtn.setOnAction(e -> {
			IntroScreen.startProgram(primaryStage);
		});

		BorderPane border = new BorderPane();
		border.setTop(topBorder);
		border.setCenter(grid);
		border.getStyleClass().add("border");

		Scene studentPageScene = new Scene(border, 500, 250);
		studentPageScene.getStylesheets().add("gui/style/css/student-style.css");
		primaryStage.setScene(studentPageScene);
		courseBox.requestFocus();
	}
}
