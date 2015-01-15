import java.util.ArrayList;


public class Section {

	final String sectionCode;
	final String CRN;
	final String status;
	final String creditHours;
	final ArrayList<Meeting> meetingsInSection;
	
	public Section(String code, String crn, String status, String hours){
		sectionCode = code;
		CRN = crn;
		this.status = status;
		creditHours = hours;
		meetingsInSection = new ArrayList<Meeting>();	
	}

	public String toString(){
		
		return this.sectionCode;
		
	}
}
