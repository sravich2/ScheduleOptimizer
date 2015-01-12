//
//import java.util.ArrayList;
//
//public class SchedulesBuilder2 {
//
//	Worker help = new Worker();
//
//	public ArrayList<Schedule> getAllSchedulesFromCourses2(ArrayList<Course> coursesTaken) {
//
//		ArrayList<ArrayList<ArrayList<Section>>> sectionLists = new ArrayList<ArrayList<ArrayList<Section>>>();
//		for (Course course : coursesTaken) {
//			if (course.partitionedSectionsInCourse != null) {
//				for (ArrayList<ArrayList<Section>> aPartitionedSectionsInCourse : course.partitionedSectionsInCourse) {
//					Section sampleSectionInPartition = aPartitionedSectionsInCourse.get(0).get(0);
//					if (!sampleSectionInPartition.meetingsInSection.get(0).type.equalsIgnoreCase("online")) {
//						if ((!sampleSectionInPartition.meetingsInSection.get(0).type.equalsIgnoreCase("lecture") && sampleSectionInPartition.meetingsInSection.get(0).daysOfTheWeek.length > 1) ||
//								sampleSectionInPartition.status.contains("estrict"))
//							continue;
//						sectionLists.add(aPartitionedSectionsInCourse);
//					}
//				}
//			}
//		}
//
//		return getAllSchedules(sectionLists);
//	}
//
//	public ArrayList<Schedule> getAllSchedules(ArrayList<ArrayList<ArrayList<Section>>> sectionLists) {
//		if (sectionLists.size() == 0) return null;
//		ArrayList<Schedule> finalArray = new ArrayList<Schedule>();
//
//		// Base Case
//		if (sectionLists.size() == 1) {
//			for (int i = 0; i < sectionLists.get(0).size(); i++) {
//				ArrayList<Meeting> meetingsTaken = new ArrayList<Meeting>();
//				ArrayList<String> temp = new ArrayList<String>();
//				for (int j = 0; j < sectionLists.get(0).get(i).size(); j++) {
//
//					meetingsTaken.addAll(sectionLists.get(0).get(j).get(j).meetingsInSection);
//					temp.add(sectionLists.get(0).get(i).get(j).CRN);
//
//				}
//				finalArray.add(new Schedule(meetingsTaken, temp));
//
//			}
//			return finalArray;
//		}
//
//		//Recursive Case
//		else {
//			ArrayList<Schedule> incompleteSchedules;
//			ArrayList<ArrayList<Section>> copyOfSectionLists = new ArrayList<ArrayList<Section>>(sectionLists);
//
//			copyOfSectionLists.remove(0);
//
//			incompleteSchedules = getAllSchedules(copyOfSectionLists);
//
//			for (int i = 0; i < sectionLists.get(0).size(); i++) {
//
//				Section currentSection = sectionLists.get(0).get(i);
//
//				for (Schedule incompleteSchedule : incompleteSchedules) {
//
//					ArrayList<Meeting> currentMeetingAL = help.deepCopyMeetingAL(incompleteSchedule.mainSchedule);
//					currentMeetingAL.addAll(currentSection.meetingsInSection);
//
//					boolean conflict = false;
//					loop:
//					for (int m = currentMeetingAL.size()-1; m > currentMeetingAL.size()-1-currentSection.meetingsInSection.size(); m--) {
//						for (int n = 0; n < currentMeetingAL.size(); n++) {
//							if (m != n && help.checkConflict(currentMeetingAL.get(m), currentMeetingAL.get(n))) {
//								conflict = true;
//								break loop;
//							}
//						}
//					}
//
//					if (!conflict) {
//						ArrayList<String> crnList = new ArrayList<String>(incompleteSchedule.CRNList);
//						crnList.add(currentSection.CRN);
//						finalArray.add(new Schedule(currentMeetingAL, crnList));
//					}
//
//				}
//
//
//			}
//			return finalArray;
//		}
//
//	}
//
//}
