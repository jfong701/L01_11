package gui;

import assignment.Assignment;
import assignment.SingleAnswerQuestion;
import jdbc.DOA;
import org.mariuszgromada.math.mxparser.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.concurrent.ThreadLocalRandom;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;



public class StudentAssignmentPage {

	private static int currentRow;

	/**
	 * Creates the StudentAssignmentPage scene where a student can see questions and
	 * enter answers for an assignment
	 *
	 * @param primaryStage
	 *            the existing stage of the program
	 * @param user
	 *            username of student
	 * @param pass
	 *            password of student
	 * @param courseName
	 *            Course code eg. "CSCC01"
	 * @param assignmentNumber
	 *            eg. 1 - for assignment 1.
	 */
	public static void startAssignment(Stage primaryStage, String user, String pass, String courseName,
			int assignmentNumber) {

		String assignmentNumStr = Integer.toString(assignmentNumber);

		// grab the Assignment object from the database containing the info we need.
		Assignment curAssgn = null;

		try {
			curAssgn = DOA.getAssignment(courseName, assignmentNumStr);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		int numQ = curAssgn.getNumQuestions();
		
		String assignmentName = curAssgn.getAssignmentName();

		// Extract question and answers from database, and process it to an easily
		// usable form.
		ArrayList<SingleAnswerQuestion> questionAndAnswerList = null;
		try {
			questionAndAnswerList = DOA.getAllAssignmentQuestions(courseName, assignmentNumStr);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int maxNumQuestions = questionAndAnswerList.size();
		if (numQ > maxNumQuestions) {
		    numQ = maxNumQuestions;
		}

		final int numQuestions = numQ;
		
		String[] questions = new String[numQuestions];
		Expression[] answers = new Expression[numQuestions];
		int[] questionIds = null;



		// iterate through our given IDs, and extract the question and answers we need
		// (in random order)
		if (numQuestions <= maxNumQuestions) {
			questionIds = Assignment.questSet(numQuestions, maxNumQuestions);
			for (int i = 0; i < numQuestions; i++) {
				SingleAnswerQuestion qaPair = questionAndAnswerList.get(questionIds[i] - 1);
				
				String[] question = qaPair.getQuestion().split(Pattern.quote(";"));
				if (question.length == 1) {
					questions[i] = qaPair.getQuestion();
					answers[i] = new Expression(qaPair.getAnswerFunction());
				} else {
					answers[i] = new Expression(qaPair.getAnswerFunction());
					String newQuestion = question[0] + ";\n";
					for (int j = 1; j < question.length; j++) {
						String[] variable = question[j].split(Pattern.quote("="));
						int original = Integer.parseInt(variable[1]);
						int min = original - qaPair.getLowerRange();
						int max = original + qaPair.getUpperRange();
						int newValue = ThreadLocalRandom.current().nextInt(min, max);
						Argument x = new Argument(variable[0], newValue);
						answers[i].addArguments(x);
						newQuestion = newQuestion.concat(variable[0] + "=" + newValue + "; ");
					}
					questions[i] = newQuestion;
				}
			}
		}
		
		// LAYOUT

		// window title
		String title = user + ": " + courseName + "Assignment #" + Integer.toString(assignmentNumber) + " - "
				+ assignmentName;
		primaryStage.setTitle(title);

		// page title (inside window)
		Label assignmentLabel = new Label(assignmentName);
		assignmentLabel.setPadding(new Insets(10, 10, 10, 10));
		assignmentLabel.setFont(Font.font("Verdana", 20));
		assignmentLabel.setId("headerLabel");

		HBox topBorder = new HBox(50);
		topBorder.setAlignment(Pos.CENTER_LEFT);
		topBorder.getStyleClass().add("hbox");
		topBorder.getChildren().add(assignmentLabel);

		// Declaring layout grid where the actual questions + boxes will be
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(5, 5, 5, 5));
		grid.setVgap(5);
		grid.setHgap(5);
		grid.getStyleClass().add("grid");
		currentRow = 0;

		// Declare arrays for elements so they can be added with a loop.
		TextFlow flow[];
		TextField answerFields[];
		flow = new TextFlow[numQuestions];
		answerFields = new TextField[numQuestions];

		// Create question labels, and textFields
		for (int i = 0; i < numQuestions; i++) {

			// create Textflow, which will have styled Text objects in it.
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
		submitButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		grid.add(backButton, 0, currentRow, 1, 1);
		grid.add(submitButton, 1, currentRow, 1, 1);

		// BACK BUTTON EVENT HANDLER
		backButton.setOnAction(e -> gui.StudentPage.login(primaryStage, user, pass));

		// SUBMIT BUTTON EVENT HANDLER
		submitButton.setOnAction(e -> {
			System.out.println("submitted");

			int grade = calculateGrade(numQuestions, answers, getUserGuesses(answerFields));

			// update the mark in the database.
			DOA.setMark(user, courseName, assignmentNumber, grade);

			// Display the grade of this attempt, and the best attempt in a message box.
			String message = "Assignment Submitted!\n\nGrade this attempt: " + Integer.toString(grade) + "%\n"
					+ "Best attempt:" + " " + Integer.toString(DOA.getMark(user, courseName, assignmentNumber)) + "%";
			MessageBox.show("Grade for " + assignmentName, message);
		});

		// embed the grid layout in a scrollPane
		ScrollPane sp = new ScrollPane(grid);
		sp.setFitToHeight(true);
		sp.setFitToWidth(true);
		sp.getStyleClass().add("scroll");

		BorderPane border = new BorderPane();
		border.setTop(topBorder);
		border.setCenter(sp);
		border.getStyleClass().add("border");

		Scene studentAssignmentScene = new Scene(border, 500, 500);
		studentAssignmentScene.getStylesheets().add("gui/style/css/student-style.css");
		primaryStage.setScene(studentAssignmentScene);
	}

	/**
	 * Extracts the Strings that the user enters in the answer fields, and outputs
	 * it as a String array.
	 * 
	 * @param answerFields
	 * @return the user's guesses as elements in a string array.
	 */
	private static String[] getUserGuesses(TextField[] answerFields) {

		int numGuesses = answerFields.length;
		String[] guesses = new String[numGuesses];

		for (int i = 0; i < numGuesses; i++) {
			guesses[i] = answerFields[i].getText();
		}
		return guesses;
	}

	/**
	 * Calculates the grade the student got on this attempt of the assignment.
	 * 
	 * @param numQuestions
	 *            The number of questions available to try
	 * @param actualAnswers
	 *            The answers pulled from the database
	 * @param userGuess
	 *            What the user (student) entered into the Textfields.
	 * @return A grade (percentage) of questions that the student got correct on the
	 *         assignment.
	 */
	private static int calculateGrade(int numQuestions, Expression[] actualAnswers, String[] userGuess) {

		int answeredCorrectly = 0;
		for (int i = 0; i < numQuestions; i++) {
			
			Expression dbAnswer = actualAnswers[i];
			System.out.println(actualAnswers[i].getExpressionString() + " " + actualAnswers[i].calculate() + "  " + userGuess[i] + "\n");
			System.out.println(actualAnswers[i].toString());
			// check the answer's numeric value. Use string checking as a fall back.
			try {
				if (dbAnswer.calculate() == Double.parseDouble(userGuess[i])) {
					answeredCorrectly++;
				}
			} catch (NumberFormatException e3) {
				if (dbAnswer.getExpressionString().equals(userGuess[i])) {
					answeredCorrectly++;

				}
			}
		}

		// calculate the grade percentage
		double percentMark = (double) ((double) (answeredCorrectly) / (double) (numQuestions)) * 100;
		return (int) (percentMark);
	}
}
