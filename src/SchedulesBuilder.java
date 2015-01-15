import java.util.ArrayList;
import java.util.Arrays;

public class SchedulesBuilder {

	final Worker help = new Worker();

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
