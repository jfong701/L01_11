package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import assignment.Question;
import jdbc.DOA;

public class ProfessorAddQuestions {
	

    public static void addQuestions(Stage primaryStage, String user, String pass) {
        // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene addStudentsScene = new Scene(grid, 500, 250);
        primaryStage.setScene(addStudentsScene);
        
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
        	String questionString = questionField.getText();
        	String answerString = questionField.getText();
        	System.out.println(String.format("%d %d", questionString.length(), answerString.length()));
            // .getText() from questionField, answerField
        	if (questionString.length() < 1 || answerString.length() < 1) {
        		MessageBox.show("Error", "Either no question or no answer.");
        	} else {
	        	Question ques = new Question(questionField.getText(), answerField.getText());
	        	DOA.addQuestion(
	        			courseBox.getValue(),
	        			assignmentBox.getValue().toString(),
	        			DOA.QuestionCount(courseBox.getValue(), assignmentBox.getValue().toString()),
	        			questionField.getText(), 
	        			answerField.getText());
	        	
	        	System.out.println(ques);
        	}
        });
        grid.add(submitButton, 2, 10, 1, 1);
    }
}
