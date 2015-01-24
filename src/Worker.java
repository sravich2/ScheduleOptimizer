import java.util.Random;
import java.util.ArrayList;

/**
 * Contains helper methods for use throughout program
 * NOTE: Edit/add Javadoc documentation if you edit/add any methods to this class
 *
 * @author Sachin
 */
public class Worker {

	/**
	 * Converts sexagesimal time to decimal equivalent. Works for both military
	 * time and AM/PM format
	 *
	 * @param time string containing time in 24-hour format
	 * @return time in decimal format
	 */
	public int convertTimeBase60To10(String time) {

		int timeIn10;
		int hourIn60 = Integer.valueOf(time.substring(0, 2));
		String lastTwoChars = time.substring(time.length() - 2);

		if (lastTwoChars.equals("PM") && hourIn60 != 12) {
			hourIn60 += 12;
		}

		if (lastTwoChars.equalsIgnoreCase("PM") || lastTwoChars.equalsIgnoreCase("AM")) time = time.substring(0, time.length() - 3);
		int minuteIn60;
		if (time.contains(":")) minuteIn60 = Integer.valueOf(time.substring(3));
		else minuteIn60 = Integer.valueOf(time.substring(2));

		timeIn10 = hourIn60 * 60 + minuteIn60;

		return timeIn10;

	}

	/**
	 * Converts time in decimal format to 24-hour format (Example: 1400, 2000)
	 *
	 * @param time time in decimal format
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
	 * Checks for time conflicts between 2 Meetings.
	 *
	 * @param meeting1 First meeting
	 * @param meeting2 Second meeting
	 * @return boolean that represents existence of conflict
	 */
	public boolean checkConflict(Meeting meeting1, Meeting meeting2) {
		boolean daysClash = false;
		outerloop:
		for (int i = 0; i < meeting1.daysOfTheWeek.length; i++) {
			for (int j = 0; j < meeting2.daysOfTheWeek.length; j++) {
				if (meeting1.daysOfTheWeek[i] == meeting2.daysOfTheWeek[j]) {
					daysClash = true;
					break outerloop;
				}
			}
		}

		return daysClash && (Math.max(meeting1.startTime, meeting2.startTime) <= Math.min(meeting1.endTime, meeting2.endTime));
	}


	/**
	 * Prints out time table representation of schedule
	 *
	 * @param schedule ArrayList<Meeting> representing schedule to be printed
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
			if (!foundClass) break;
			output.append("\n");
		}

		return output.toString();
	}

	/**
	 * Converts ArrayList<Meeting> containing list of Modules to ArrayList<ArrayList<Meeting>> containing
	 * day-wise references to Modules First dimension ranges from 0 to 4,
	 * represents days of the working week
	 *
	 * @param meetingAL ArrayList<Meeting> whose day-wise representation is sought
	 * @return ArrayList<ArrayList<Meeting>> containing day-wise representation of ArrayList<Meeting> meetingAL
	 */
	public ArrayList<ArrayList<Meeting>> convertModuleArrayToSchedule(ArrayList<Meeting> meetingAL) {
		ArrayList<ArrayList<Meeting>> schedule = new ArrayList<ArrayList<Meeting>>();

		for (int i = 0; i < 5; i++) {
			schedule.add(new ArrayList<Meeting>());
		}

		for (Meeting meeting : meetingAL) {
			if (String.valueOf(meeting.daysOfTheWeek).contains("M")) schedule.get(0).add(meeting);
			if (String.valueOf(meeting.daysOfTheWeek).contains("T")) schedule.get(1).add(meeting);
			if (String.valueOf(meeting.daysOfTheWeek).contains("W")) schedule.get(2).add(meeting);
			if (String.valueOf(meeting.daysOfTheWeek).contains("R")) schedule.get(3).add(meeting);
			if (String.valueOf(meeting.daysOfTheWeek).contains("F")) schedule.get(4).add(meeting);
		}

		return schedule;
	}

	/**
	 * Converts string representing location to a format usable in URLs
	 * Primarily for making calls to the MapQuest API
	 *
	 * @param locationString String representing location
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
	 * @param scheduleForOneDay ArrayList<Meeting></Meeting> representing schedule on a day
	 * @return ArrayList<Meeting> representing same schedule with classes in chronological
	 * order
	 */
	public ArrayList<Meeting> sortByTimeScheduleForOneDay(ArrayList<Meeting> scheduleForOneDay) {
		ArrayList<Meeting> finalSchedule = new ArrayList<Meeting>();

		ArrayList<Meeting> scheduleForOneDay2 = new ArrayList<Meeting>();
		for (Meeting meeting : scheduleForOneDay) {
			scheduleForOneDay2.add(new Meeting(meeting));
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
