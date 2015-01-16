import java.util.Arrays;

import org.w3c.dom.Document;


/**
 * Meeting, defined as a meeting of any section.
 *
 * @author sravich2
 */

public class Meeting {
	
	
	public final Worker help = new Worker();
	public Section inSection;
	public String type;
	public final char[] daysOfTheWeek;
	public String building;
	public String instructor;
	public String startDate;
	public String endDate;
	public int startTime;
	public int endTime;
	public final int duration;

	public Meeting(Section inSection, String type, String days, String startTime, String endTime, String building, String instructor, String startDate, String endDate) {
		this.inSection = inSection;
		this.type = type;
		this.daysOfTheWeek = days.toCharArray();
		if (!startTime.equals("") && Character.isDigit(startTime.charAt(0))) this.startTime = help.convertTimeBase60To10(startTime);
		if (!endTime.equals("") && Character.isDigit(endTime.charAt(0))) this.endTime = help.convertTimeBase60To10(endTime);
		this.duration = this.endTime - this.startTime;
		this.building = building;
		this.instructor = instructor;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Meeting(Meeting copyThis) {
		inSection = copyThis.inSection;
		type = copyThis.type;
		daysOfTheWeek = copyThis.daysOfTheWeek;
		building = copyThis.building;
		instructor = copyThis.instructor;
		startDate = copyThis.startDate;
		endDate = copyThis.endDate;
		startTime = copyThis.startTime;
		endTime = copyThis.endTime;
		duration = startTime - endTime;
	}

	public Meeting(String days, String startTime, String endTime) {
		this.daysOfTheWeek = days.toCharArray();
		this.startTime = help.convertTimeBase60To10(startTime);
		this.endTime = help.convertTimeBase60To10(endTime);
		this.duration = this.endTime - this.startTime;
	}

	public String toString() {
		return (type + ": " + startTime + " - " + endTime + "-" + String.valueOf(daysOfTheWeek));
	}
	
}
