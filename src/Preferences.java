import java.util.ArrayList;

/**
 * Framework for storing and working with user preferences
 * 
 * @author Sachin
 * 
 */
public class Preferences {
	public boolean lunchBreak;
	public boolean walk;
	public int maxMinutesInADay;
	public int maxMinutesInARow;
	public int avoidTime; // 0 - No preference, 1 - morning, 2 - afternoon, 3 -
						  // evening
	public int avoidBreaksBetweenClasses=0;// 0 - No preference, 1 - Avoid short
										 // breaks, 2 - Avoid long breaks, 3 -
										 // Avoid all breaks
	public boolean extendWeekend; // false - No preference, true - Minimize
								  // classes on Friday and Monday
	public int dayWithMinimum; // Accepts 0, 1, 2. Tries to fit in a day with
							   // said number of classes
	public ArrayList<String> preferredInstructors;

	public Preferences(boolean lunch, int inDay, int inRow, int avoid, int avoidShortBreaks,
	        boolean weekend, int minClasses, boolean walk) {
		this.lunchBreak = lunch;
		this.maxMinutesInADay = inDay;
		this.maxMinutesInARow = inRow;
		this.avoidTime = avoid;
		this.avoidBreaksBetweenClasses = avoidShortBreaks;
		this.extendWeekend = weekend;
		this.dayWithMinimum = minClasses;
		this.walk = walk;
	}
	public Preferences(){}

}
