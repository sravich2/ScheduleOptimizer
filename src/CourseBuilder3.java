import java.lang.reflect.Array;
import java.util.ArrayList;

public class CourseBuilder3 {

	Worker help = new Worker();

	public ArrayList<Schedule> getAllSchedulesFromCourses(ArrayList<Course> coursesTaken) {
		
		ArrayList<ArrayList<Section>> sectionLists = new ArrayList<ArrayList<Section>>();
		for (Course aCoursesTaken : coursesTaken) {
			if (aCoursesTaken.partitionedSectionsInCourse != null) {
				for (ArrayList<Section> aPartitionedSectionsInCourse : aCoursesTaken.partitionedSectionsInCourse) {
					Section sampleSectionInPartition = aPartitionedSectionsInCourse.get(0);
					if (!sampleSectionInPartition.meetingsInSection.get(0).type.equalsIgnoreCase("online")) {
						if (!sampleSectionInPartition.meetingsInSection.get(0).type.equalsIgnoreCase("lecture") && sampleSectionInPartition.meetingsInSection.get(0).daysOfTheWeek.length > 1 &&
								sampleSectionInPartition.status.contains("estrict"))

							continue;
						sectionLists.add(aPartitionedSectionsInCourse);
					}

				}
			}
		}

		return getAllSchedules(sectionLists);
	}

	public ArrayList<Schedule> getAllSchedules(ArrayList<ArrayList<Section>> sectionLists) {
		if (sectionLists.size() == 0) return null;
		ArrayList<Schedule> finalArray = new ArrayList<Schedule>();

		// Base Case
		if (sectionLists.size() == 1) {
			for (int i = 0; i < sectionLists.get(0).size(); i++) {
				ArrayList<String> temp = new ArrayList<String>();
				temp.add(sectionLists.get(0).get(i).CRN);
				finalArray.add(new Schedule(sectionLists.get(0).get(i).meetingsInSection, temp));
			}

			return finalArray;
		}

		//Recursive Case
		else {
			ArrayList<Schedule> comboOfRemainingSections;
			ArrayList<ArrayList<Section>> copyOfSectionLists = new ArrayList<ArrayList<Section>>(sectionLists);

			copyOfSectionLists.remove(0);

			comboOfRemainingSections = getAllSchedules(copyOfSectionLists);

			for (int i = 0; i < sectionLists.get(0).size(); i++) {

				Section currentSection = sectionLists.get(0).get(i);

				for (Schedule comboOfRemainingSection : comboOfRemainingSections) {

					ArrayList<Meeting> currentMeetingAL = help.deepCopyMeetingAL(comboOfRemainingSection.mainSchedule);
					currentMeetingAL.addAll(currentSection.meetingsInSection);

					boolean conflict = false;
					loop:
					for (int m = currentMeetingAL.size()-1; m > currentMeetingAL.size()-1-currentSection.meetingsInSection.size(); m--) {
						for (int n = 0; n < currentMeetingAL.size(); n++) {
							if (m != n && help.checkConflict(currentMeetingAL.get(m), currentMeetingAL.get(n))) {
								conflict = true;
								break loop;
							}
						}
					}

					if (!conflict) {
						ArrayList<String> temp = new ArrayList<String>(comboOfRemainingSection.CRNList);
						temp.add(currentSection.CRN);
						finalArray.add(new Schedule(currentMeetingAL, temp));
					}

				}


			}
			return finalArray;
		}
		
	}

	public boolean checkConflictInSchedule(ArrayList<Meeting> schedule) {
		for (int m = 0; m < schedule.size(); m++) {
			for (int n = 0; n < schedule.size(); n++) {
				if (m != n && help.checkConflict(schedule.get(m), schedule.get(n))) {
					return true;
				}
			}
		}
		return false;
	}

}
