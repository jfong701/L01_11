# L01_11 Not Fast Just Furious

## System Requirements to run the program
  * [Oracle Java 8 JRE](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
  * A good internet connection (10 Mbps download / 2 Mbps upload) is recommended. You can check your internet speed [here](http://beta.speedtest.net/)
  
### Run the standalone.jar
1. Download a .jar file from the "executables" folder
2. Navigate to the directory where the .jar is stored.
3. Double click the .jar to open it.

### Run the program from Eclipse
(requires [Eclipse](https://www.eclipse.org/downloads/))
1. Download directory ProjectMain/ and all subdirectories and files
2. Open Eclipse and go to File -> Open Projects from File System
3. The value for "Import source" should be the ProjectMain folder. Click Finish.
4. Right click the ProjectMain folder and go to Properties.
5. Go to Java Build Path -> Libraries and click "Add JARS"
6. Navigate to the "lib" folder and add the mysql connector.
7. Repeat steps 5-6 and add "mxparser.jar"
8. Click "Apply and Close"
9. Navigate to ProjectMain/src/gui and run IntroScreen.java

## System Requirements to build the .jar
  * [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
  * [Eclipse](https://www.eclipse.org/downloads/)
  
### Build Instructions
1. Download directory ProjectMain/ and all subdirectories and files
2. Open a Eclipse and go to File -> Open Projects from File System
3. The value for "Import source" should be the ProjectMain folder. Click Finish.
4. In Package Explorer, Right click "ProjectMain" and go to Properties.
5. Go to Java Build Path -> Libraries and click "Add JARS"
6. Navigate to the "lib" folder and add the mysql connector.
7. Repeat steps 5-6 and add "mxparser.jar"
8. Right click the ProjectMain folder and click 'Export'
9. Select Java > 'Runnable JAR file' and click Next
10. Set Launch configuration to 'IntroScreen - ProjectMain'
11. Choose an appropriate Export destination and file name
12. In Libary handling select 'Extract required libaries into generated JAR
13. Click Finish

## Repository Contents
deliverables
* Deliverable 1
  * Not Fast Just Furious Team Agreement.pdf
* Delivarable 2
  * Product Backlog v0.pdf
  * Product Backlog v0.1.pdf
* Deliverable 3
  * Product Backlog v1.pdf
  * Product Backlog v1.1.pdf
  * Sprint 1 Backlog.pdf
* Deliverable 4
  * Sprint 1 Backlog.pdf
  * Sprint 2 Backlog.pdf
  * Sprint 3 Backlog.pdf
* Deliverable 5 (UnitTests in ProjectMain folder)
  * Product Backlog v4.pdf
  * Sprint 5 Backlog.pdf
  * Validation Testing v1.pdf
  * Code Review.pdf
* Deliverable 6 (UnitTests in ProjectMain folder)
  * NotFastJustFurious-2017.11.27.jar
  * Product Backlog v5.pdf
  * Sprint 6 Backlog.pdf
  * Validation Testing v2.pdf

executables
* NotFastJustFurious-2017.11.17.jar
* NotFastJustFurious-2017.11.27.jar

ProjectMain/lib/
* mxparser.jar
* mysql-connector-java-5.1.42-bin.jar
 
ProjectMain/src/
* assignment
  * Assignment.java
  * SingleAnswerQuestion.java
* gui
  * style
    * css
      * intro-screen.css
      * professor-style.css
      * student-style.css
    * images
      * TeamLogoAngry.jpg
      * TeamLogoAngryTranslucent1.png
      * TeamLogoAngryTranslucent25.png
  * IntroScreen.java
  * MessageBox.java
  * ProfessorAddAssignments.java
  * ProfessorAddProfessors.java
  * ProfessorAddQuestions.java
  * ProfessorAddStudents.java
  * ProfessorPage.java
  * ProfessorViewStudents.java
  * StudentAssignmentPage.java
  * StudentPage.java
* jdbc
  * DOA.java
  * MySQLAccess.java
  * OldMySQLAccess.java
* student
  * Professor.java
  * Student.java

ProjectMain/UnitTest
* assignment
  * TestAssignment.java
  * TestQuestion.java
* jdbc
  * TestDOA.java
* student
  * TestStudent.java
  
ProjectMain/ValidationTest
  * Validation Testing v1.pdf

Product Backlog
  * Product Backlog v0.pdf
  * Product Backlog v0.1.pdf
  * Product Backlog v1.pdf
  * Product Backlog v1.1.pdf
  * Product Backlog v2.pdf
  * Product Backlog v3.pdf
  * Product Backlog v4.pdf
  * Product Backlog v5.pdf

SampleFiles
  * ENGA01WordProblems.csv
  * InvalidQuestions.csv
  * MATA01SimpleMath.csv
  * MATA01VariableMath.csv
  * SampleAddSubQuestions.csv
  * SampleAssignmentFile.csv
  * SampleProfessorFile.csv
  * SampleProfessorFile2.csv
  * SampleStudentFile.csv
  * SampleStudentFile1.csv
  * SampleStudentFile2.csv
  * SampleStudentFile3.csv

Sprint Backlog
  * Sprint 1 Backlog.pdf
  * Sprint 2 Backlog.pdf
  * Sprint 3 Backlog.pdf
  * Sprint 4 Backlog.pdf
  * Sprint 5 Backlog.pdf
  * Sprint 6 Backlog.pdf

README.md
