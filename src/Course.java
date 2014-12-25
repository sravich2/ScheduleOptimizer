import java.util.ArrayList;

/**
 * Course as a standalone entity BEFORE module selection, therefore contains ALL
 * available modules for course. Examples: CS125, CS173
 * 
 * @author Sachin
 *
 */
public class Course {

	public String courseName;
	public int creditHours;
	public String courseCode;
	public ArrayList<ArrayList<Section>> partitionedSectionsInCourse;
	public Meeting[][] modulesAvailable;

	public Course(String name, String hours, String code, int numberOfModules) {
		modulesAvailable = new Meeting[numberOfModules][30];
		partitionedSectionsInCourse = new ArrayList<ArrayList<Section>>();
		courseName = name;
		courseCode = code;
		creditHours = (int) hours.charAt(0);
	}

	public String toString() {
		return courseName + "(" + courseCode + ")";
	}

}
