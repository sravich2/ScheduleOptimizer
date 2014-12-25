import java.util.ArrayList;

public class CourseBuilder2 {

	public int indexOfCourseBeingIncremented = 0;
	Worker help = new Worker();
	public Meeting[] currentSchedule;

	public ArrayList<Meeting[]> getAllSchedulesFromCourses(ArrayList<Course> coursesTaken) {
		ArrayList<Meeting[]> modulesTaken = new ArrayList<Meeting[]>();
		for (int i = 0; i < coursesTaken.size(); i++) {
			for (int j = 0; j < coursesTaken.get(i).modulesAvailable.length; j++) {
				modulesTaken.add(coursesTaken.get(i).modulesAvailable[j]);
			}
		}
		return getAllSchedules(modulesTaken);
	}

	public ArrayList<Meeting[]> getAllSchedules(ArrayList<Meeting[]> modulesToBeTaken) {
		ArrayList<Meeting[]> finalArray = new ArrayList<Meeting[]>(modulesToBeTaken.size());

		// Base Case
		if (modulesToBeTaken.size() == 1) {
			for (int i = 0; i < help.realLength(modulesToBeTaken.get(0)); i++) {
				Meeting[] temp = new Meeting[] { modulesToBeTaken.get(0)[i] };

				finalArray.add(temp);
			}

			return finalArray;
		}

		else {
			ArrayList<Meeting[]> comboOfRemaining = new ArrayList<Meeting[]>();
			ArrayList<Meeting[]> copyOfModulesTaken = new ArrayList(modulesToBeTaken);
			copyOfModulesTaken.remove(0);
			comboOfRemaining = getAllSchedules(copyOfModulesTaken);
			boolean conflict;
			for (int i = 0; i < help.realLength(modulesToBeTaken.get(0)); i++) {
				Meeting[] temp = new Meeting[modulesToBeTaken.size()];
				temp[0] = modulesToBeTaken.get(0)[i];

				for (int j = 0; j < comboOfRemaining.size(); j++) {
					conflict = false;
					for (int k = 0; k < modulesToBeTaken.size() - 1; k++) {
						temp[k + 1] = comboOfRemaining.get(j)[k];
					}
					loop: for (int m = 0; m < temp.length - 1; m++) {
						for (int n = m + 1; n < temp.length; n++) {
							if (m != n && help.checkConflict(temp[m], temp[n])) {
								// System.out.println(help.toString(temp));
								conflict = true;
								break loop;
							}
						}
					}

					if (!conflict)
						finalArray.add(help.deepCopyModuleArray(temp));
				}

			}

			return finalArray;
		}

	}

}
