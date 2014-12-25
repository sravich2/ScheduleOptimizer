import java.util.Arrays;

import org.w3c.dom.Document;


/**
 * Module, defined as a component of any course.
 * Examples: Lecture, Discussion, Laboratory
 * @author sravich2
 */

public class Meeting {
	
	
	public Worker help = new Worker();
	public Section inSection;
	public String type;
	public char[] daysOfTheWeek;
	public String building; //Pull from CSV file.
	public String instructor; //Check from CSV as and when needed
	public String startDate;
	public String endDate;
	public int startTime;
	public int endTime;
	public int duration;

	public Meeting clone() {
		return new Meeting(this.inSection, this.type, String.valueOf(this.daysOfTheWeek), this.startTime, this.endTime, this.building, this.instructor, this.startDate, this.endDate);
	}

	public Meeting(Section inSection, String type, String days, String startTime, String endTime, String building, String instructor, String startDate, String endDate){
		this.inSection = inSection;
		this.type = type;
		this.daysOfTheWeek = days.toCharArray();
		if (!startTime.equals("") && Character.isDigit(startTime.charAt(0)))
			this.startTime = help.convertTimeBase60To10(startTime);
		if (!endTime.equals("") && Character.isDigit(endTime.charAt(0)))
			this.endTime = help.convertTimeBase60To10(endTime);
		this.duration = this.endTime-this.startTime;
		this.building = building;
		this.instructor = instructor;
	}

	public Meeting(Section inSection, String type, String days, int startTime, int endTime, String building, String instructor, String startDate, String endDate){
		this.type = type;
		this.daysOfTheWeek = days.toCharArray();
		if (!(startTime ==0))
			this.startTime = startTime;
		if (!(endTime == 0))
			this.endTime = endTime;
		this.duration = this.endTime-this.startTime;
		this.building = building;
		this.instructor = instructor;
	}

	public Meeting(String days, String startTime, String endTime, String location)
	{
		this.daysOfTheWeek = days.toCharArray();
		this.startTime = help.convertTimeBase60To10(startTime);
		this.endTime = help.convertTimeBase60To10(endTime);
		this.duration = this.endTime-this.startTime;
		this.building = location;
	}
	
	public Meeting(String days, String startTime, String endTime)
	{
		this.daysOfTheWeek = days.toCharArray();
		this.startTime = help.convertTimeBase60To10(startTime);
		this.endTime = help.convertTimeBase60To10(endTime);
		this.duration = this.endTime-this.startTime;
	}
	
	public Meeting(String days, int startTime, int endTime)
	{
		this.daysOfTheWeek = days.toCharArray();
		this.startTime = (startTime);
		this.endTime = (endTime);
		this.duration = this.endTime-this.startTime;
	}
	
	public Meeting(String days, int startTime, int endTime, String building)
	{
		this.daysOfTheWeek = days.toCharArray();
		this.startTime = (startTime);
		this.endTime = (endTime);
		this.duration = this.endTime-this.startTime;
		this.building = building;
	}
	
	public boolean equals(Meeting checkMeeting)
	{
		return Arrays.equals(this.daysOfTheWeek, checkMeeting.daysOfTheWeek) && this.startTime == checkMeeting.startTime && this.endTime == checkMeeting.endTime;
	}
	
	public String toString(){
		return (type + ": " + startTime + " - " + endTime+"-"+String.valueOf(daysOfTheWeek));
	}
	
}
