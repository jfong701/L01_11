# L01_11 Not Fast Just Furious

## Repository Contents
deliverables
* deliverable 1
  * Not Fast Just Furious Team Agreement.pdf
* delivarable 2
  * Product Backlog v0.pdf
  * Product Backlog v0.1.pdf
* deliverable 3
  * Product Backlog v1.pdf
  * Product Backlog v1.1.pdf
  * Sprint 1 Backlog.pdf
* deliverable 4
  * Sprint 1 Backlog.pdf
  * Sprint 2 Backlog.pdf
  * Sprint 3 Backlog.pdf

executables
* NotFastJustFurious-2017.11.17.jar

ProjectMain/lib/
* mysql-connector-java-5.1.42-bin.jar
 
ProjectMain/src/
* assignment
  * Assignment.java
  * Question.java
  * RandQuests.java
* gui
  * IntroScreen.java
  * MessageBox.java
  * ProfessorAddAssignments.java
  * ProfessorAddQuestions.java
  * ProfessorAddStudents.java
  * ProfessorPage.java
  * ProfessorViewStudents.java
  * StudentAssignmentPage.java
  * StudentPage.java
* jdbc
  * DOA.java
  * MySQLAccess.java
* student
  * Student.java
  * Students.java

Product Backlog
  * Product Backlog v0.pdf
  * Product Backlog v0.1.pdf
  * Product Backlog v1.pdf
  * Product Backlog v1.1.pdf
  * Product Backlog v2.pdf
  * Product Backlog v3.pdf
  * Product Backlog v4.pdf
  
Sprint Backlog
  * Sprint 1 Backlog.pdf
  * Sprint 2 Backlog.pdf
  * Sprint 3 Backlog.pdf
  * Sprint 4 Backlog.pdf
  * Sprint 5 Backlog.pdf

README.md
SampleStudentFile.csv

## System Requirements to run the .jar
  * Java 8 JRE
  * An internet connection
  
### Run the standalone.jar
1. Download a .jar file from the "executables" folder
2. Navigate to the directory where the .jar is stored.
3. Double click the .jar to open it.

### Run the program from Eclipse
(requires Eclipse)
1. Download directory ProjectMain/ and all subdirectories and files
2. Open Eclipse and go to File -> Open Projects from File System
3. The value for "Import source" should be the ProjectMain folder. Click Finish.
4. Right click and go to Properties.
5. Go to Java Build Path -> Libraries and click "Add JARS"
6. Navigate to the "lib" folder and add the mysql connector.
7. Navigate to ProjectMain/src/gui and run IntroScreen.java

## System Requirements to build the .jar
  * Java 8 JDK
  * Eclipse
  
### Build Instructions
1. Download directory ProjectMain/ and all subdirectories and files
2. Open a Eclipse and go to File -> Open Projects from File System
3. The value for "Import source" should be the ProjectMain folder. Click Finish.
4. Right click and go to Properties.
5. Go to Java Build Path -> Libraries and click "Add JARS"
6. Navigate to the "lib" folder and add the mysql connector.
7. Right click the ProjectMain folder and click 'Export'
8. Select Java > 'Runnable JAR file' and click Next
9. Set Launch configuration to 'IntroScreen - ProjectMain'
10. Choose an appropriate Export destination and file name
11. In Libary handling select 'Extract required libaries into generated JAR
12. Click Finish
