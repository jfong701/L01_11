package student;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.*;

import jdbc.DOA;

/**
 * 
 */

/**
 * @author DAVID
 *
 */
public class Students {

	/**
	 * 
	 */
	List<Student> studentList;
	public Students() {
		// TODO Auto-generated constructor stub
		this.studentList = new ArrayList<Student>();
	}
	
	
	/**
	 * @return the studentList
	 */
	public List<Student> getStudentList() {
		return studentList;
	}
	
	
	/**
	 * @param the fileName of the file that contains Student info
	 */
	public void readStudentsFile(String fileName) {
		FileReader file = null;
		BufferedReader buffer = null;
		// Error check IO calls 
		try {
			file = new FileReader(fileName);
			buffer = new BufferedReader(file);
			String line;
			String[] splitLine;
			String studentNo, studentUtor, studentFirstName, studentLastName;
			// read line by line, split and trim the strings and create a Student to put into studentList
			while ((line = buffer.readLine()) != null) {
				splitLine = line.split(",");
				studentNo = splitLine[0].trim();
				studentUtor = splitLine[1].trim();
				studentFirstName = splitLine[1].trim();
				studentLastName = splitLine[2].trim();
				studentList.add(new Student(studentNo, studentUtor, studentFirstName, studentLastName));
			}
			
		} catch (IOException error) {
			System.err.println("IOException: " + error.getMessage());
		}
		
	}

	
	/**
	 * @param  studentNo to search for
	 * @return Student object or null if not in list
	 */
	public Student findStudent(String studentNo) {
		boolean found = false;
		int i = 0;
		while (!found && i < studentList.size()) {
			if (studentList.get(i).getStudentNo().equals(studentNo)) {
				found = true;
			} else {
				i++;
			}
		}
		return studentList.get(i);
	}
	
	/** 
	 * @return string format of the ArrayList of Student
	 */
	@Override
	public String toString() {
		String format = "";
		for (int i=0; i<studentList.size(); i++) {
			format = format + studentList.get(i) + '\n';
		}
		return format;
	}
}