import java.util.ArrayList;
import java.util.Arrays;

public class ScheduleScorer {

	final Worker help = new Worker();
	public Preferences pref;
	public StringBuilder log = new StringBuilder();

	public ArrayList<Integer> notableTimes = new ArrayList<Integer>();

	public ScheduleScorer(Preferences pref) {
		this.pref = pref;
	}

	/**
	 * Scores a given schedule according to arbitrary set of rules, detailed in documentation
	 * Input taken as ArrayList<ArrayList<Meeting>>. See next line.
	 * Use convertMeetingArrayToSchedule method in Worker class to convert ArrayList<Meeting> to ArrayList<ArrayList<Meeting>>
	 *
	 * @param schedule ArrayList<ArrayList<Meeting>> which represents prospective schedule
	 * @return Score for given prospective schedule. Higher is better.
	 */
	public double scoreSchedule(ArrayList<ArrayList<Meeting>> schedule, ArrayList<String> crnList) {
		log.setLength(0);
		double score = 0;
		String periodOfDay = "";
		String days[] = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
		
		switch (pref.avoidTime) {
			case 1:
				periodOfDay = "morning";
				break;
			case 2:
				periodOfDay = "afternoon";
				break;
			case 3:
				periodOfDay = "evening";
				break;
			default:
		}
		 log.append("Schedule Report:\n");
		log.append("CRN List: ").append(crnList).append("\n");

		for (int i = 0; i < 5; i++) {
			log.append("\n").append(days[i]).append("\n");
			//1. Checking for lunch break
			if (pref.lunchBreak) {
				if (!this.checkLunchBreak(schedule.get(i))) {
					log.append("No lunch break on ").append(days[i]).append("\n");
					score -= 15;
				}
			}
			
			//2. Checking for classes at disfavored Time of Day
			int badClasses = this.classesAtTimeOfDay(schedule.get(i));

			if (badClasses > 0) {
				score -= badClasses * 7.5;
				log.append(badClasses).append((badClasses == 1) ? " class" : " classes").append(" in the ").append(periodOfDay).append(" on ").append(days[i]).append("\n");
			}

			//3. Checking existence of Breaks as per user preference
			if (pref.avoidBreaksBetweenClasses > 0) {
				int shortBreaks = this.countShortAndTotalBreaks(schedule.get(i))[0];
				int longBreaks = this.countShortAndTotalBreaks(schedule.get(i))[1];

				if ((pref.avoidBreaksBetweenClasses == 1 || pref.avoidBreaksBetweenClasses == 3) && shortBreaks > 0) {
					score -= shortBreaks * 5;
					log.append(shortBreaks).append(" short ").append((shortBreaks == 1) ? "break " : "breaks").append(" on ").append(days[i]).append("\n");
				}

				if ((pref.avoidBreaksBetweenClasses == 2 || pref.avoidBreaksBetweenClasses == 3) && longBreaks > 0) {
					score -= longBreaks * 5;
					log.append(longBreaks).append(" long ").append((longBreaks == 1) ? "break " : "breaks").append(" on ").append(days[i]).append("\n");
				}
			}


			//4. Calculating Maximum Minutes in a Row
			int maxMinsInRow = this.maxMinutesInRow();
			if (maxMinsInRow > pref.maxMinutesInARow) {
				score -= (maxMinsInRow - pref.maxMinutesInARow) / 10;
				log.append(maxMinsInRow).append(" minutes in a row on ").append(days[i]).append("\n");
			}
			
			//5. Calculating Minutes in class each day
			int maxMinsInDay = this.minutesInDay(schedule.get(i));
			if (maxMinsInDay > pref.maxMinutesInADay) {
				score -= (maxMinsInDay - pref.maxMinutesInADay) / 10;
				log.append(maxMinsInDay).append(" minutes on ").append(days[i]).append("\n");
			}
			

			/*
			//6. Checking for unwalkable classes
			if (pref.walk){
			int unwalkableClasses = this.countUnwalkableClasses(schedule.get(i)).length;
			if ((unwalkableClasses>0))
			{
				score -= 50;
				log.append(unwalkableClasses+ " unwalkable classes " + " on " + days[i] + "\n");
			}}
			*/
			
			//7. Check for day with minimum classes
			if (pref.dayWithMinimum <= 2) {
				if (countClassesOnADay(schedule.get(i)) <= pref.dayWithMinimum) {
					score += 10 * (3 - pref.dayWithMinimum);
					log.append(countClassesOnADay(schedule.get(i))).append(" classes on ").append(days[i]).append("\n");
				}
			}

			//8. Checking for instructor match
			if (pref.preferredInstructors != null && pref.preferredInstructors.size() > 0) {
				for (int j = 0; j < schedule.get(i).size(); j++) {
					if (pref.preferredInstructors.contains(schedule.get(i).get(j).instructor.split(",")[0]) && schedule.get(i).get(j).type.contains("ecture")) {
						score += 30;
						log.append(schedule.get(i).get(j).instructor).append(" is an instructor in this schedule\n");

						pref.preferredInstructors.remove(pref.preferredInstructors.indexOf(schedule.get(i).get(j).instructor.split(",")[0]));
					}
				}
			}
		}
		
		//9. Checking classes near weekend
		if (pref.extendWeekend) {
			int mondayClasses = countClassesOnADay(schedule.get(0));
			int fridayClasses = countClassesOnADay(schedule.get(4));
			if (mondayClasses > 0) {
				score -= mondayClasses * 2.5;
				log.append(mondayClasses).append(" classes on Monday\n");
			}
			if (fridayClasses > 0) {
				score -= fridayClasses * 2.5;
				log.append(fridayClasses).append(" classes on Friday\n");
			}
		}
		
		return score;
		
	}

	/**
	 * Calculates the number of minutes in class on a given day
	 *
	 * @param scheduleForOneDay ArrayList<Meeting> containing all classes on a single day
	 * @return Number of minutes in class during the given day
	 */
	public int minutesInDay(ArrayList<Meeting> scheduleForOneDay) {
		int minutesBusyInDay = 0;
		for (Meeting aScheduleForOneDay : scheduleForOneDay) {
			minutesBusyInDay += aScheduleForOneDay.duration;
		}
		return minutesBusyInDay;
	}

	/**
	 * Calculates the maximum number of minutes in a row on a given day
	 *
	 * @return Maximum number of minutes in a row during the given day
	 */
	public int maxMinutesInRow() {
		int maxMinutesInRow = 0;

		for (int i = 0; i < notableTimes.size() / 2 - 1; i++) {
			if (notableTimes.get(2 * (i + 1)) - notableTimes.get(2 * i + 1) < 20) {
				notableTimes.remove(2 * i + 1);
				notableTimes.remove(2 * i + 1);
				i--; //We don't want i to be incremented because we have removed 2 elements
			}
		}
		for (int i = 0; i < notableTimes.size() / 2; i++) {
			if (notableTimes.get(2 * (i) + 1) - notableTimes.get(2 * i) > maxMinutesInRow) maxMinutesInRow = notableTimes.get(2 * (i) + 1) - notableTimes.get(2 * i);
		}
		return maxMinutesInRow;

	}

	/**
	 * Checks whether there is a lunch break on the given day
	 *
	 * @param scheduleForOneDay ArrayList<Meeting> containing all classes on a single day
	 * @return boolean represents whether there is a break for lunch on the given day
	 */
	public boolean checkLunchBreak(ArrayList<Meeting> scheduleForOneDay) {
		int minutesBusyDuringLunchTime = 0;
		for (Meeting aScheduleForOneDay : scheduleForOneDay) {
			if (aScheduleForOneDay.endTime > 660 && aScheduleForOneDay.startTime < 840)//660 is decimal for 1100, 840 is decimal for 1400
			{
				minutesBusyDuringLunchTime += Math.min(aScheduleForOneDay.endTime, 840) - Math.max(aScheduleForOneDay.startTime, 660);
			}
		}

		return minutesBusyDuringLunchTime < 120;

	}

	/**
	 * Calculates the number of classes during disfavored time of day on the given day
	 *
	 * @param scheduleOnDay ArrayList<Meeting> containing all classes on a single day
	 * @return Number of classes during disfavored time of day on the given day
	 */
	public int classesAtTimeOfDay(ArrayList<Meeting> scheduleOnDay) //Returns number of bad classes
	{
		int countOfClassesInUndesirableTime = 0;
		Meeting checkAgainstThisMeeting;
		switch (pref.avoidTime) {
			default:
				return 0;
			case 1:
				checkAgainstThisMeeting = new Meeting("MTWRF", "08:00 AM", "10:59 AM");
				break;
			case 2:
				checkAgainstThisMeeting = new Meeting("MTWRF", "11:00 AM", "03:59 PM");
				break;
			case 3:
				checkAgainstThisMeeting = new Meeting("MTWRF", "04:00 AM", "10:00 PM");
				break;
		}

		for (Meeting aScheduleOnDay : scheduleOnDay) {

			if (help.checkConflict(checkAgainstThisMeeting, aScheduleOnDay)) {
				countOfClassesInUndesirableTime++;
			}
		}
		return countOfClassesInUndesirableTime;
	}

	/**
	 * Counts the number of short breaks and total breaks between classes on the given day
	 * Short breaks: 20-90 minutes
	 * Long breaks: 91+ minutes
	 *
	 * @param scheduleForOneDay ArrayList<Meeting> representing schedule for a day
	 * @return int[] containing count of short breaks and long breaks, in that order
	 */
	public int[] countShortAndTotalBreaks(ArrayList<Meeting> scheduleForOneDay) {
		int[] countBreaks = new int[2];
		int countShortBreaks = 0;
		int countLongBreaks = 0;
		int breakBetweenClasses;

		notableTimes.clear();

		//int[] notableTimes = new int[scheduleForOneDay.size() * 2];
		for (Meeting aScheduleForOneDay : scheduleForOneDay) {
			notableTimes.add(aScheduleForOneDay.startTime);
			notableTimes.add(aScheduleForOneDay.endTime);
			//notableTimes[count] = aScheduleForOneDay.startTime;
			//notableTimes[count + 1] = aScheduleForOneDay.endTime;
			//count += 2;
		}
		Integer[] notableArray = new Integer[notableTimes.size()];
		Arrays.sort(notableTimes.toArray(notableArray));
		notableTimes = new ArrayList<Integer>(Arrays.asList(notableArray));

		for (int i = 0; i < notableTimes.size() / 2 - 1; i++) {
			breakBetweenClasses = notableTimes.get(2 * (i + 1)) - notableTimes.get(2 * i + 1);

			if (breakBetweenClasses >= 20 && breakBetweenClasses <= 90) countShortBreaks++;
			if (breakBetweenClasses > 90) countLongBreaks++;
		}

		countBreaks[0] = countShortBreaks;
		countBreaks[1] = countLongBreaks;
		return countBreaks;
	}


	//OPTIMISE THIS METHOD
	public int[] countUnwalkableClasses(ArrayList<Meeting> scheduleForOneDay)
	{
		DistanceTimeMatrix matrix = new DistanceTimeMatrix();
		double[] travelInfo = new double[2];
		int[] howLateAreYouGoingToBe = new int[10];
		int count = 0;
		ArrayList<Meeting> scheduleForOneDay2 = help.sortByTimeScheduleForOneDay(scheduleForOneDay);

		for (int i = 0; i < scheduleForOneDay2.size() - 1; i++) {
			try {
				//travelInfo = matrix.getTravelTimeAndDistance("http://www.mapquestapi
				// .com/directions/v2/route?key=Fmjtd%7Cluurn9u7ng%2Cbx%3Do5-9wznl0&outFormat=json&routeType=pedestrian&enhancedNarrative=true&locale=en_US&from=1304+w+springfield+avenue+urbana&to
				// =201+n+goodwin+avenue+Urbana");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
			if (travelInfo[0] > scheduleForOneDay2.get(i + 1).startTime - scheduleForOneDay2.get(i).endTime) {
				System.out.print(scheduleForOneDay2.get(i).building + " to ");
				System.out.println(scheduleForOneDay2.get(i + 1).building + "  " + travelInfo[0] + " > " + scheduleForOneDay2.get(i + 1).startTime + " - " + scheduleForOneDay2.get(i).endTime);
				howLateAreYouGoingToBe[count] = (int) (travelInfo[0] - (scheduleForOneDay2.get(i + 1).startTime - scheduleForOneDay2.get(i).endTime));
				count++;
			}
		}
		howLateAreYouGoingToBe = Arrays.copyOfRange(howLateAreYouGoingToBe, 0, count + 1);
		return howLateAreYouGoingToBe;
	}
	
	public int countClassesOnADay(ArrayList<Meeting> scheduleForOneDay) {
		return scheduleForOneDay.size();
	}

}
