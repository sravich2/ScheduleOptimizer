import java.util.Random;
import java.util.ArrayList;

/**
 * Contains helper methods for use throughout program NOTE: Edit/add Javadoc
 * documentation if you edit/add any methods to this class
 * 
 * @author Sachin
 *
 */
public class Worker {

	/**
	 * Converts sexagesimal time to decimal equivalent. Works for both military
	 * time and AM/PM format
	 * 
	 * @param time
	 *            string containing time in 24-hour format
	 * @return time in decimal format
	 */
	public int convertTimeBase60To10(String time) {
		int timeIn10 = 0;

			int hourIn60 = Integer.valueOf(time.substring(0, 2));
			String lastTwoChars = time.substring(time.length()-2);

			if (lastTwoChars.equals("PM") && hourIn60 != 12) {
				hourIn60 += 12;
			}

			if (lastTwoChars.equalsIgnoreCase("PM") || lastTwoChars.equalsIgnoreCase("AM"))
				time = time.substring(0, time.length()-3);
			int minuteIn60 = 0;
			if (time.contains(":"))
				minuteIn60 = Integer.valueOf(time.substring(3));
			else
				minuteIn60 = Integer.valueOf(time.substring(2));

			timeIn10 = hourIn60 * 60 + minuteIn60;

		return timeIn10;

	}

	/**
	 * Converts time in decimal format to 24-hour format (Example: 1400, 2000)
	 * 
	 * @param time
	 *            time in decimal format
	 * @return string containing time in 24-hour format
	 */
	public String convertTimeBase10To60(int time) {
		String timeIn60;
		int hourIn60 = time / 60;
		int minuteIn60 = time % 60;
		timeIn60 = String.format("%02d", hourIn60) + String.format("%02d", minuteIn60);

		return timeIn60;
	}

	/**
	 * Checks for time conflicts between 2 Modules. Checks truth of statement:
	 * "There is a conflict between modules module1 and module2"
	 * 
	 * @param module1
	 * @param module2
	 * @return boolean that represents existence of conflict
	 */
	public boolean checkConflict(Meeting module1, Meeting module2) {
		boolean daysClash = false;
		outerloop: for (int i = 0; i < module1.daysOfTheWeek.length; i++) {
			for (int j = 0; j < module2.daysOfTheWeek.length; j++) {
				if (module1.daysOfTheWeek[i] == module2.daysOfTheWeek[j]) {
					daysClash = true;
					break outerloop;
				}
			}
		}

		if (daysClash) {
			if (Math.max(module1.startTime, module2.startTime) <= Math.min(module1.endTime,
			        module2.endTime))
				return true;
		}
		return false;
	}

	/**
	 * Choose random Modules for given Course NOTE: Needs optimization and
	 * efficiency boosts
	 * 
	 * @param inputCourse
	 *            Course for which modules are to be chosen
	 * @return Module[] containing random selection of Modules for given Course
	 */
	public ArrayList<Meeting> chooseRandomModules(Course inputCourse) {
		Random rand = new Random();
		// Module[] finalCourses = new
		// Module[inputCourse.modulesAvailable.length];
		ArrayList<Meeting> finalCourses = new ArrayList<Meeting>(
		        inputCourse.modulesAvailable.length);
		for (int i = 0; i < inputCourse.modulesAvailable.length; i++)
			finalCourses.add(inputCourse.modulesAvailable[i][(int) (rand.nextDouble() * this
			        .realLength(inputCourse.modulesAvailable[i]))]);
		return finalCourses;
	}


	/**
	 * Prints out time table representation of schedule
	 * 
	 * @param schedule
	 *            Module[] representing schedule to be printed
	 * @return String containing time table representation of schedule
	 */
	public String toString(ArrayList<ArrayList<Meeting>> schedule) {
		StringBuilder output = new StringBuilder();

		output.append("   Monday         Tuesday          Wednesday         Thursday         Friday\n");
		for (int i = 0; i < 5; i++) {
			schedule.set(i, this.sortByTimeScheduleForOneDay(schedule.get(i)));
		}
		for (int i = 0; i < 8; i++) {
			boolean foundClass = false;
			for (int j = 0; j < 5; j++) {
				if (i < schedule.get(j).size()) {
					output.append(this.convertTimeBase10To60(schedule.get(j).get(i).startTime)).append(" - ").append(this.convertTimeBase10To60(schedule.get(j).get(i).endTime)).append("      ");
					foundClass = true;
				} else {
					output.append("                 ");
				}

			}
			if (!foundClass)
				break;
			output.append("\n");
		}

		return output.toString();
	}

	/**
	 * Calculates number of non-null references in Module[]
	 *
	 * @param input
	 *            Module[] whose length is to be found
	 * @return number of non-null references in Module[] input
	 */
	public int realLength(Meeting[] input) {
		int i = 0;
		while (input[i] != null)
			i++;
		return i;
	}

	/**
	 * Converts Module[] containing list of Modules to Module[][] containing
	 * day-wise references to Modules First dimension ranges from 0 to 4,
	 * represents days of the working week
	 * 
	 * @param arr
	 *            Module[] whose day-wise representation is sought
	 * @return Module[][] containing day-wise representation of Module[] arr
	 */
	public ArrayList<ArrayList<Meeting>> convertModuleArrayToSchedule(ArrayList<Meeting> arr) {
		ArrayList<ArrayList<Meeting>> schedule = new ArrayList<ArrayList<Meeting>>();

		for (int i = 0; i < 5;i++) {
			schedule.add(new ArrayList<Meeting>());
		}

		for (Meeting anArr : arr) {
			if (String.valueOf(anArr.daysOfTheWeek).contains("M"))
				schedule.get(0).add(anArr);
			if (String.valueOf(anArr.daysOfTheWeek).contains("T"))
				schedule.get(1).add(anArr);
			if (String.valueOf(anArr.daysOfTheWeek).contains("W"))
				schedule.get(2).add(anArr);
			if (String.valueOf(anArr.daysOfTheWeek).contains("R"))
				schedule.get(3).add(anArr);
			if (String.valueOf(anArr.daysOfTheWeek).contains("F"))
				schedule.get(4).add(anArr);
		}

		return schedule;
	}

	/**
	 * Removes elements of Module[] at given index and the index following it
	 *
	 * @param inputArray
	 *            Module[] from which elements are to be removed
	 * @param k
	 *            Index of first removal
	 * @return Module[] with elements at k and k+1 removed
	 */
	public int[] removeFromArray(int[] inputArray, int k) // Removes elements at
														  // k and k + 1
	{
		int[] newArray = new int[inputArray.length - 2];
		System.arraycopy(inputArray, 0, newArray, 0, k);
		System.arraycopy(inputArray, k + 2, newArray, k, newArray.length - k);
		return newArray;
	}

	/**
	 * Compares two Module[] (compares contents, not memory locations)
	 * 
	 * @param module1
	 * @param module2
	 * @return boolean representing whether module1 and module2 are equal
	 */
	public boolean compareModuleArrays(Meeting[] module1, Meeting[] module2) {
		if (module1.length != module2.length)
			return false;

		for (int i = 0; i < module1.length; i++) {
			if (!module1[i].equals(module2[i])) {
				return false;
			}
		}

		return true;
	}

	public ArrayList<Meeting> deepCopyMeetingAL(ArrayList<Meeting> inputAL){
		ArrayList<Meeting> newAL = new ArrayList<Meeting>();
		for (Meeting aMeeting : inputAL){
			newAL.add(aMeeting.clone());
		}
		return newAL;
	}

	/**
	 * Creates a deep copy of Module
	 * 
	 * @param inputModule
	 *            Module whose deep copy is needed
	 * @return Module which is a deep copy of inputModule
	 */
	public Meeting deepCopyModule(Meeting inputModule) {
		return new Meeting(String.valueOf(inputModule.daysOfTheWeek),
		        inputModule.startTime, inputModule.endTime, inputModule.building);
	}

	/**
	 * Creates a deep copy of Module[]
	 * 
	 * @param inputArray
	 *            Module[] whose deep copy is needed
	 * @return Module[] which is a deep copy of inputArray
	 */
	public Meeting[] deepCopyModuleArray(Meeting[] inputArray) {
		Meeting[] outputArray = new Meeting[inputArray.length];
		for (int i = 0; i < outputArray.length; i++) {
			outputArray[i] = this.deepCopyModule(inputArray[i]);
		}
		return outputArray;
	}

	/**
	 * Converts string representing location to a format usable in URLs
	 * Primarily for making calls to the Distance Matrix API
	 * 
	 * @param locationString
	 *            String representing location
	 * @return String representing location in URL format
	 */
	public String parseLocationToURLFormat(String locationString) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(locationString.toLowerCase());
		while (buffer.indexOf(" ") > -1) {
			buffer.setCharAt(buffer.indexOf(" "), '+');
		}

		return buffer.toString();
	}

	/**
	 * Sorts the classes on a day in chronological order
	 * 
	 * @param scheduleForOneDay
	 *            Module[] representing schedule on a day
	 * @return Module[] representing same schedule with classes in chronological
	 *         order
	 */
	public ArrayList<Meeting> sortByTimeScheduleForOneDay(ArrayList<Meeting> scheduleForOneDay) {
		ArrayList<Meeting> finalSchedule = new ArrayList<Meeting>();

		ArrayList<Meeting> scheduleForOneDay2 = new ArrayList<Meeting>();
		for (int i = 0; i < scheduleForOneDay.size();i++){
			scheduleForOneDay2.add(scheduleForOneDay.get(i).clone());
		}

		int nextIndex;
		int earliest;
		int count = 0;
		while (count < scheduleForOneDay2.size()) {
			earliest = 9999;
			nextIndex = 0;
			for (int i = 0; i < scheduleForOneDay2.size(); i++) {
				if (scheduleForOneDay2.get(i).startTime < earliest) {
					nextIndex = i;
					earliest = scheduleForOneDay2.get(i).startTime;
				}

			}
			finalSchedule.add(scheduleForOneDay.get(nextIndex));
			scheduleForOneDay2.get(nextIndex).startTime = 9999;

			count++;
		}

		return finalSchedule;
	}
}
