import student.Student;
import student.Classes;
import professor.Professor;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Classes test = new Classes("CSCC01F17", "Software Engineering");
		
		//test.insertSQL();
		
		Classes tst = new Classes("CSCC01F17");
		
		//tst.deleteSQL();
		
		//tst.selectSQL();
		//String prt1 = tst.getClassName();
		//System.out.print(prt1);
		
		tst.setClassName("Software Design");
		//tst.setStudentLastName("HUNG");
		tst.updateSQL();
		String prt2 = tst.getClassName();
		System.out.print(prt2);
		
		//Professor test = new Professor("001", "Sohee", "Kwang");
		//test.insertSQL();
		
		//Professor test = new Professor("001");
		
		//test.deleteSQL();
		
		//test.selectSQL();
		//String prt1 = test.getProfessorFirstName() + " " + test.getProfessorLastName();
		//System.out.println(prt1);
		
		//test.setProfessorFirstName("Nick");
		//test.setProfessorLastName("Chan");
		//test.updateSQL();
		//String prt2 = test.getProfessorFirstName() + " " + test.getProfessorLastName();
		//System.out.println(prt2);
	}

}
