package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Date;

import assignment.Assignment;
import assignment.SingleAnswerQuestion;
import jdbc.DOA;

public class ProfessorAddQuestions {
	
	private static TableView<SingleAnswerQuestion> questions;	

    public static void addQuestions(Stage primaryStage, String user, String pass) {
        
    	// Create layout
    	primaryStage.setTitle("View Assignments");
    	
    	BorderPane border = new BorderPane();
    	
    	try {
			setUpTable();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        // Title
        Text sceneTitle = new Text("Add Questions");
        sceneTitle.setFont(Font.font("Verdana", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 1, 1);

        // Labels and TextFields
        
        // Course Label
        Label courseLabel = new Label("Enter course id:");
        grid.add(courseLabel, 0, 2, 1, 1);
        
        ComboBox<String> courseBox = new ComboBox<>();
        courseBox.getItems().addAll(DOA.getCourseIds());
        grid.add(courseBox, 0, 3, 3, 1);

        // Assignment Number label
        Label aIDLabel = new Label("Enter assignment id");
        grid.add(aIDLabel, 1, 2, 1, 1);
        
        ComboBox<Integer> assignmentBox = new ComboBox<>();
        assignmentBox.getItems().addAll(DOA.getAssignmentIds(courseBox.getValue()));
        
        // Modifying boxes so that changing value in courseBox, change assignments available in assignmentBox
        courseBox.setOnAction( e -> {
        	assignmentBox.getItems().clear();
			assignmentBox.getItems().addAll(DOA.getAssignmentIds(courseBox.getValue()));
			try {
				loadTable(courseBox.getValue(), "");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
        });
        
        assignmentBox.setOnAction( e -> {
        	try {
				loadTable(courseBox.getValue(), assignmentBox.getValue().toString());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
        
        grid.add(assignmentBox, 1, 3, 3, 1);
        
        // Question Label
        Label questionLabel = new Label("Enter a question");
        grid.add(questionLabel, 0, 5, 1, 1);

        TextField questionField = new TextField();
        questionField.setPromptText("e.g. What is 3 + 4");
        grid.add(questionField, 0, 6, 1, 1);

        // Answer label
        Label answerLabel = new Label("Enter the answer to the question");
        grid.add(answerLabel, 1, 5, 1, 1);

        TextField answerField = new TextField();
        answerField.setPromptText("e.g. 7");
        grid.add(answerField, 1, 6, 1, 1);

        // Buttons
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> ProfessorPage.login(primaryStage, user, pass));
        grid.add(backButton, 0, 10, 1, 1);

        // TO-DO: ADD LOGIC TO SUBMIT BUTTON
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {

            // .getText() from questionField, answerField
        	DOA.addQuestion(
        			courseBox.getValue(),
        			assignmentBox.getValue().toString(),
        			DOA.QuestionCount(courseBox.getValue(), assignmentBox.getValue().toString()),
        			questionField.getText(), 
        			answerField.getText());
        	
        	questionField.clear();
        	answerField.clear();
        	try {
				loadTable(courseBox.getValue(), assignmentBox.getValue().toString());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
          	
        });
        grid.add(submitButton, 2, 10, 1, 1);
        
        border.setCenter(questions);
        border.setBottom(grid);
        
        Scene addQuestionsScene = new Scene(border, 500, 500);
        primaryStage.setScene(addQuestionsScene);

    }
    
	public static ObservableList<SingleAnswerQuestion> getQuestions(String course_id, String assignment_id) throws SQLException {
		ObservableList<SingleAnswerQuestion> ques = null;
		if (course_id.isEmpty() && assignment_id.isEmpty()) {
			ques = FXCollections.observableArrayList(DOA.getAllQuestions());
		} else if (!(course_id.isEmpty()) && assignment_id.isEmpty()) {
			ques = FXCollections.observableArrayList(DOA.getAllCourseQuestions(course_id));
		} else if (!(course_id.isEmpty()) && !(assignment_id.isEmpty())) {
			ques = FXCollections.observableArrayList(DOA.getAllAssignmentQuestions(course_id, assignment_id));
		}
		return ques;
	}
	

	public static void loadTable(String course_id, String assignment_id) throws SQLException {
		questions.getItems().clear();
		questions.setItems(getQuestions(course_id, assignment_id));
	}
    
	public static void setUpTable() throws SQLException {
		questions = new TableView<>();
		
		// Table Columns
		TableColumn<SingleAnswerQuestion, String> courseID = new TableColumn<>("Course ID");
		courseID.setMinWidth(125);
		courseID.setCellValueFactory(new PropertyValueFactory<>("courseID"));
		
		TableColumn<SingleAnswerQuestion, Integer> assignmentID = new TableColumn<>("Assignment ID");
		assignmentID.setMinWidth(125);
		assignmentID.setCellValueFactory(new PropertyValueFactory<>("assignmentID"));
		
		TableColumn<SingleAnswerQuestion, String> question = new TableColumn<>("Question");
		question.setMinWidth(125);
		question.setCellValueFactory(new PropertyValueFactory<>("question"));
		
		TableColumn<SingleAnswerQuestion, String> answerFunction = new TableColumn<>("Answer Function");
		answerFunction.setMinWidth(125);
		answerFunction.setCellValueFactory(new PropertyValueFactory<>("answerFunction"));
		
		questions.getColumns().addAll(courseID, assignmentID, question, answerFunction);
		questions.setFixedCellSize(25);
		loadTable("", "");

	}

}
