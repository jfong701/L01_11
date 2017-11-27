package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import jdbc.DOA;
import user.Professor;
import user.Student;
import validator.Validators;

public class ProfessorAddProfessors {
	
	private static TableView<Professor> professors;
	private static Stage stage;

    public static void addProfessors(Stage primaryStage, String user, String pass) {
    	
    	stage = primaryStage;
    	
    	// Create layout
    	primaryStage.setTitle("View Professors");
    	
    	BorderPane border = new BorderPane();
    	
    	setUpTable();
    	
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
		Button uploadProfessors = new Button("Upload Professor Files...");
		uploadProfessors.setOnAction( e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Upload Professor File");
			List<File> list = fileChooser.showOpenMultipleDialog(primaryStage);
			if (list != null) {
				for (File file : list) {					
					List<String> errors = Validators.getErrorsInProfessorFile(file.getAbsolutePath().replace('\\', '/'));
					if (!errors.isEmpty()) {
						String error = "";
						for (String err : errors) error = error + err + '\n';
						MessageBox.show("Errors", error);
					} else {
						try {
							DOA.uploadProfessorFile(file.getAbsolutePath().replace('\\', '/'));
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}
			loadTable();
		});
		grid.add(uploadProfessors, 0, 0, 1, 1);
        

        // Labels and TextFields
        // Course Label
        Label profIDLabel = new Label("Enter Professor id:");
        grid.add(profIDLabel, 0, 1, 1, 1);

        TextField profIDField = new TextField();
        profIDField.setPromptText("e.g. 0123456789");
        grid.add(profIDField, 0, 2, 1, 1);

        // Professor Number label
        Label profFirstLabel = new Label("Enter Professor First Name:");
        grid.add(profFirstLabel, 1, 1, 1, 1);

        TextField profFirstField = new TextField();
        profFirstField.setPromptText("e.g. 2");
        grid.add(profFirstField, 1, 2, 1, 1);
        
        // Number Questions label
        Label profLastLabel = new Label("Enter Professor Last Name:");
        grid.add(profLastLabel, 2, 1, 1, 1);

        TextField profLastField = new TextField();
        profLastField.setPromptText("e.g. 7");
        grid.add(profLastField, 2, 2, 1, 1);
        
        // Buttons
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> ProfessorPage.login(primaryStage, user, pass));
        grid.add(backButton, 0, 9, 1, 1);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
        	if (!Validators.isProfessorValid(profIDField.getText(), profFirstField.getText(), profLastField.getText()))	{
        		MessageBox.show("Errors", "Invalid input for Professor.");
        	} else {
	        	DOA.addProfessor(
			       	profIDField.getText(),
			       	profFirstField.getText(),
			       	profLastField.getText());
	        	profIDField.clear();
	        	profFirstField.clear();
	        	profLastField.clear();
	        	loadTable();
        	}
        });
        grid.add(submitButton, 2, 9, 1, 1);
        
        border.setCenter(professors);
        border.setBottom(grid);
        border.getStyleClass().add("border-no-overlay");
        
        Scene addProfessorsScene = new Scene(border, 750, 500);
        addProfessorsScene.getStylesheets().add("gui/style/css/professor-style.css");
        primaryStage.setScene(addProfessorsScene);
    }
    
	public static ObservableList<Professor> getProfessors() {
		ObservableList<Professor> profs = null;
		profs = FXCollections.observableArrayList(DOA.getAllProfessors());
		return profs;
	}
	

	public static void loadTable() {
		professors.getItems().clear();
		professors.setItems(getProfessors());
	}
    
	public static void setUpTable() {
		professors = new TableView<Professor>();
		
		// Table Columns		
		TableColumn<Professor, Integer> profID = new TableColumn<>("Professor ID");
		profID.setCellValueFactory(new PropertyValueFactory<>("profID"));
		
		TableColumn<Professor, Integer> profFirstName = new TableColumn<>("First Name");
		profFirstName.setCellValueFactory(new PropertyValueFactory<>("profFirstName"));
		
		TableColumn<Professor, String> profLastName = new TableColumn<>("Last Name");
		profLastName.setCellValueFactory(new PropertyValueFactory<>("profLastName"));
		
		profID.prefWidthProperty().bind(professors.widthProperty().divide(3)); // w * 1/4
		profFirstName.prefWidthProperty().bind(professors.widthProperty().divide(3)); // w * 1/2
		profLastName.prefWidthProperty().bind(professors.widthProperty().divide(3)); // w * 1/4
				
		professors.getColumns().addAll(profID, profFirstName, profLastName);
		professors.setFixedCellSize(25);
		loadTable();

	}
}
