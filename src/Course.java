import java.util.ArrayList;

/**
 * Course as a standalone entity BEFORE module selection, therefore contains ALL
 * available modules for course. Examples: CS125, CS173
 * 
 * @author Sachin
 *
 */
public class Course {

	public final String courseName;
	public final int creditHours;
	public final String courseCode;
	public boolean hasLectureDiscussion;
	public ArrayList<ArrayList<Section>> partitionedSectionsInCourse;
	public ArrayList<ArrayList<Section>> sectionOptionsForCourse;

	public Course(String name, String hours, String code) {

		partitionedSectionsInCourse = new ArrayList<ArrayList<Section>>();
		courseName = name;
		courseCode = code;
		creditHours = (int) hours.charAt(0);
		hasLectureDiscussion = false;
	}

	public String toString() {
		return courseName + "(" + courseCode + ")";
	}

}
