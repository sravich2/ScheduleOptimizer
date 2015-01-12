import java.util.ArrayList;
import java.util.Arrays;

public class SchedulesBuilder {

	Worker help = new Worker();

	public ArrayList<Schedule> getAllSchedulesFromCourses(ArrayList<Course> coursesTaken){
		ArrayList<Schedule> finalArray = new ArrayList<Schedule>();
		ArrayList<ArrayList<Section>> sectionCombos = generateSectionListsFromCourses(coursesTaken);


		for (ArrayList<Section> sectionCombo : sectionCombos){
			ArrayList<String> crnList = new ArrayList<String>();
			boolean conflict = false;
			ArrayList<Meeting> meetingList = new ArrayList<Meeting>();

			loop:
			for (Section section : sectionCombo){
				crnList.add(section.CRN);
				meetingList.addAll(section.meetingsInSection);

				for (int i = meetingList.size()-1;i>meetingList.size()-section.meetingsInSection.size()-1;i--){
					for (int j = 0; j < meetingList.size()-section.meetingsInSection.size();j++){
						if (help.checkConflict(meetingList.get(i), meetingList.get(j))) {
							conflict = true;
							break loop;
						}
					}
				}

			}

			if (!conflict){
				finalArray.add(new Schedule(meetingList, crnList));
			}

		}

		return finalArray;
	}

	public ArrayList<Schedule> getAllSchedulesFromCourses2(ArrayList<Course> coursesTaken) {

		ArrayList<ArrayList<Section>> sectionLists = new ArrayList<ArrayList<Section>>();
		for (Course course : coursesTaken) {
			if (course.partitionedSectionsInCourse != null) {
				for (ArrayList<Section> aPartitionedSectionsInCourse : course.partitionedSectionsInCourse) {
					Section sampleSectionInPartition = aPartitionedSectionsInCourse.get(0);
					if (!sampleSectionInPartition.meetingsInSection.get(0).type.equalsIgnoreCase("online")) {
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
			ArrayList<Schedule> incompleteSchedules;
			ArrayList<ArrayList<Section>> copyOfSectionLists = new ArrayList<ArrayList<Section>>(sectionLists);

			copyOfSectionLists.remove(0);
			incompleteSchedules = getAllSchedules(copyOfSectionLists);

			for (int i = 0; i < sectionLists.get(0).size(); i++) {

				Section currentSection = sectionLists.get(0).get(i);

				for (Schedule incompleteSchedule : incompleteSchedules) {

					ArrayList<Meeting> currentMeetingAL = help.deepCopyMeetingAL(incompleteSchedule.mainSchedule);
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
						ArrayList<String> crnList = new ArrayList<String>(incompleteSchedule.CRNList);
						crnList.add(currentSection.CRN);
						finalArray.add(new Schedule(currentMeetingAL, crnList));
					}

				}
			}
			return finalArray;
		}
		
	}

	public ArrayList<ArrayList<Section>> generateSectionCombinationsFromPartitions(ArrayList<ArrayList<Section>> sections){
		ArrayList<ArrayList<Section>> finalArray = new ArrayList<ArrayList<Section>>();
		if (sections.size() == 0) return null;
		if (sections.size() == 1){
			for (int i = 0; i < sections.get(0).size(); i++) {
				finalArray.add(new ArrayList<Section>(Arrays.asList(sections.get(0).get(i))));
			}
			return finalArray;
		}
		else{
			ArrayList<ArrayList<Section>> copyOfSections = new ArrayList<ArrayList<Section>>(sections);
			copyOfSections.remove(0);

			ArrayList<ArrayList<Section>> incompleteCombinations = generateSectionCombinationsFromPartitions(copyOfSections);

			for (int i = 0; i < sections.get(0).size(); i++) {

				Section currentSection = sections.get(0).get(i);

				for (ArrayList<Section> incompleteCombination: incompleteCombinations) {

					ArrayList<Section> currentCombination = new ArrayList<Section>(incompleteCombination);
					currentCombination.add(currentSection);
					finalArray.add(currentCombination);
				}
			}
			return finalArray;
		}
	}

	//Wrapper for generateSectionListsFromOptionsList
	public ArrayList<ArrayList<Section>> generateSectionListsFromCourses(ArrayList<Course> coursesTaken) {
		ArrayList<ArrayList<ArrayList<Section>>> optionsList = new ArrayList<ArrayList<ArrayList<Section>>>();
		for (Course course : coursesTaken){
			optionsList.add(course.sectionOptionsForCourse);
		}
		return generateSectionListsFromOptionsList(optionsList);
	}

	//This basically generates a list of list of sections i.e. a list of potential schedules
	public ArrayList<ArrayList<Section>> generateSectionListsFromOptionsList(ArrayList<ArrayList<ArrayList<Section>>> optionsList){

		if (optionsList.size() == 0) return null;
		if (optionsList.size() == 1){
			return optionsList.get(0);
		}
		else {
			ArrayList<ArrayList<Section>> finalArray = new ArrayList<ArrayList<Section>>();
			ArrayList<ArrayList<ArrayList<Section>>> copyOfOptionsList = new ArrayList<ArrayList<ArrayList<Section>>>(optionsList);
			copyOfOptionsList.remove(0);
			ArrayList<ArrayList<Section>> sectionCombosFromFollowingOptions = generateSectionListsFromOptionsList(copyOfOptionsList);

			for (int i = 0; i < optionsList.get(0).size();i++){
				ArrayList<Section> currentOption = optionsList.get(0).get(i);

				for (ArrayList<Section> combo : sectionCombosFromFollowingOptions){

					ArrayList<Section> currentCombo = new ArrayList<Section>(combo);
					currentCombo.addAll(currentOption);
					finalArray.add(currentCombo);
				}
			}

			return finalArray;
		}

	}

}
