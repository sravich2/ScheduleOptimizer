import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Contains main method Contains manually entered sample data for classes
 *
 * @author Sachin
 */
public class Driver {

	public static final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	public static DocumentBuilder dBuilder = null;
	public static ArrayList<String> coursesTaken;
	public static Preferences pref = new Preferences();
	public static final SchedulesBuilder scheduleBuilder = new SchedulesBuilder();

	public static void main(String[] args) {

		getUserInput();

		long time = System.currentTimeMillis();
		Worker help = new Worker();
		ScheduleScorer scorer = new ScheduleScorer(pref);

		ArrayList<Course> courseList = new ArrayList<Course>();
		updateCourseSectionMeetingObjects(coursesTaken, courseList);
		updateSectionOptionsForCourses(courseList);

		ArrayList<Schedule> allSchedules = scheduleBuilder.getAllSchedulesFromCourses(courseList);
		ArrayList<ArrayList<Meeting>> bestSchedule = new ArrayList<ArrayList<Meeting>>();
		ArrayList<String> scheduleReadable = new ArrayList<String>();
		ArrayList<StringBuilder> bestLog = new ArrayList<StringBuilder>();
		double bestScore = -999;
		int countOfBestSchedule = 0;
		int countOfExecutions = 0;

		while (allSchedules != null && countOfExecutions < allSchedules.size()) {
			Schedule scheduleForSemester = allSchedules.get(countOfExecutions);
			scheduleForSemester.twoDimensionalSchedule = help.convertModuleArrayToSchedule(scheduleForSemester.mainSchedule);
			double currentScore = (scorer.scoreSchedule(scheduleForSemester.twoDimensionalSchedule, scheduleForSemester.CRNList));

			if (currentScore > bestScore) {
				bestSchedule.clear();
				scheduleReadable.clear();
				bestLog.clear();
				countOfBestSchedule = 0;

				bestLog.add(new StringBuilder(scorer.log));
				bestSchedule.add(help.deepCopyMeetingAL(scheduleForSemester.mainSchedule));
				scheduleReadable.add(help.toString(scheduleForSemester.twoDimensionalSchedule));
				bestScore = currentScore;
				countOfBestSchedule++;

			} else if (currentScore == bestScore) {

				bestLog.add(new StringBuilder(scorer.log));
				bestSchedule.add(help.deepCopyMeetingAL(scheduleForSemester.mainSchedule));
				scheduleReadable.add(help.toString(scheduleForSemester.twoDimensionalSchedule));
				countOfBestSchedule++;
			}
			countOfExecutions++;
		}

		System.out.println("\nFound " + (countOfBestSchedule) + ((countOfBestSchedule == 1) ? (" optimal schedule") : (" optimal schedules")) + " in " + (System.currentTimeMillis() - time) + " ms");
		System.out.println("--------------------------------------------------------------------------------");
		for (int i = 0; i < countOfBestSchedule; i++) {
			System.out.println(scheduleReadable.get(i));
			System.out.println(bestLog.get(i));
			System.out.println("--------------------------------------------------------------------------------");

		}

		System.out.println("Score: " + bestScore);
		System.out.println("Schedules scored: " + countOfExecutions);

	}

	public static ArrayList<ArrayList<Section>> partitionSections(ArrayList<Section> sections) {

		if (sections.size() == 0) return null;

		ArrayList<ArrayList<Section>> partitionedSections = new ArrayList<ArrayList<Section>>();
		ArrayList<Section> tempSections = new ArrayList<Section>();
		tempSections.add(sections.get(0));
		partitionedSections.add(tempSections);

		for (int i = 1; i < sections.size(); i++) {
			for (int j = 0; j < partitionedSections.size(); j++) {

				ArrayList<Meeting> meetingsInPartitionedElement = partitionedSections.get(j).get(0).meetingsInSection;
				ArrayList<Meeting> meetingsInInputSections = sections.get(i).meetingsInSection;

				if (meetingsInPartitionedElement.size() == meetingsInInputSections.size()) {
					boolean equal = true;
					for (int k = 0; k < meetingsInInputSections.size(); k++) {

						if (!meetingsInInputSections.get(k).type.equals(meetingsInPartitionedElement.get(k).type)) {
							equal = false;
							break;
						}
					}
					if (equal) {
						partitionedSections.get(j).add(sections.get(i));
						break;
					}
				}

				if (j == partitionedSections.size() - 1) {

					ArrayList<Section> temp2 = new ArrayList<Section>();
					temp2.add(sections.get(i));
					partitionedSections.add(temp2);
					break;
				}
			}

		}

		return partitionedSections;
	}


	public static void updateSectionOptionsForCourses(ArrayList<Course> coursesTaken) {

		for (Course course : coursesTaken) {
			ArrayList<ArrayList<Section>> finalArray = new ArrayList<ArrayList<Section>>();
			if (course.hasLectureDiscussion) {
				for (Section lecDis : course.partitionedSectionsInCourse.get(course.partitionedSectionsInCourse.size() - 1)) {
					finalArray.add(new ArrayList<Section>(Arrays.asList(lecDis)));
				}
				course.partitionedSectionsInCourse.remove(course.partitionedSectionsInCourse.size() - 1);
				finalArray.addAll(scheduleBuilder.generateSectionCombinationsFromPartitions(course.partitionedSectionsInCourse));

			} else {
				finalArray.addAll(scheduleBuilder.generateSectionCombinationsFromPartitions(course.partitionedSectionsInCourse));
			}
			course.sectionOptionsForCourse = finalArray;
		}
	}

	public static void updateCourseSectionMeetingObjects(ArrayList<String> coursesTaken, ArrayList<Course> courseList) {

		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException pce) {
			System.out.println(pce.getMessage());
		}

		try {

			courseLoop:
			for (int courseCount = 0; courseCount < coursesTaken.size(); courseCount++) {
				String courseCodeNumber;
				ArrayList<String> sectionPreference = new ArrayList<String>();
				String[] tempCourseInfo = coursesTaken.get(courseCount).split("(?<=[a-zA-Z])(\\s)*(?=\\d)");
				String deptCode = tempCourseInfo[0];

				if (!coursesTaken.get(courseCount).contains(":")) {
					courseCodeNumber = tempCourseInfo[1];
				} else {
					courseCodeNumber = tempCourseInfo[1].substring(0, tempCourseInfo[1].indexOf(":")).trim();
					sectionPreference.add(tempCourseInfo[1].split("\\s*:\\s*")[1].trim());

				}

				Document courseInfo = dBuilder.parse("/Users/Sachin/workspace/Schedule Optimization/courseData.xml");
				NodeList deptList = courseInfo.getDocumentElement().getElementsByTagName("dept");
				for (int i = 0; i < deptList.getLength(); i++) {
					Element courseElement = null;
					if (((Element) deptList.item(i)).getAttribute("id").equals(deptCode)) {
						NodeList tempCourseList = ((Element) deptList.item(i)).getElementsByTagName("course");
						for (int j = 0; j < tempCourseList.getLength(); j++) {
							if (((Element) tempCourseList.item(j)).getAttribute("id").equals(courseCodeNumber)) {
								courseElement = (Element) tempCourseList.item(j);
								String courseName = courseElement.getAttribute("name");
								String courseCode = courseElement.getAttribute("id");
								String courseCreditHours = courseElement.getElementsByTagName("creditHours").item(0).getTextContent();
								courseList.add(new Course(courseName, courseCreditHours, courseCode));
								break;
							}
							if (j == tempCourseList.getLength() - 1) {
								System.out.println("\n" + coursesTaken.get(courseCount) + " does not exist or is not being offered in Spring 2015. Skipping over this course...");
								coursesTaken.remove(courseCount);

								break courseLoop;
							}

						}

						ArrayList<Section> prunedSectionList = new ArrayList<Section>();
						assert courseElement != null;
						NodeList sectionList = courseElement.getElementsByTagName("section");

						sectionLoop:
						for (int k = 0; k < sectionList.getLength(); k++) {
							Element sectionElement = (Element) sectionList.item(k);

							String sectionCode = sectionElement.getAttribute("sectionNumber");
							if (sectionPreference.size() != 0) {
								for (int l = 0; l < sectionPreference.size(); l++) {
									if (sectionCode.substring(0, 1).equals(sectionPreference.get(l).toUpperCase())) break;
									if (i == sectionPreference.size() - 1) continue sectionLoop;
								}
							}

							String sectionStatus = sectionElement.getAttribute("status");
							String sectionCRN = sectionElement.getAttribute("id");
							String sectionCreditHours = sectionElement.getElementsByTagName("creditHours").item(0).getTextContent();

							prunedSectionList.add(new Section(sectionCode, sectionCRN, sectionStatus, sectionCreditHours));

							NodeList meetingList = sectionElement.getElementsByTagName("meeting");

							for (int l = 0; l < meetingList.getLength(); l++) {
								Element meetingElement = (Element) meetingList.item(l);
								String type = meetingElement.getAttribute("type");

								if (type.equalsIgnoreCase("online")) {
									prunedSectionList.remove(prunedSectionList.size() - 1);
									continue sectionLoop;
								}
								if (type.equalsIgnoreCase("lecture-discussion")) courseList.get(courseCount).hasLectureDiscussion = true;

								String days = meetingElement.getElementsByTagName("daysOfTheWeek").item(0).getTextContent();
								String startTime = meetingElement.getElementsByTagName("startTime").item(0).getTextContent();
								String endTime = meetingElement.getElementsByTagName("endTime").item(0).getTextContent();
								String building = meetingElement.getElementsByTagName("building").item(0).getTextContent();
								String instructor = meetingElement.getElementsByTagName("instructor").item(0).getTextContent();
								String startDate = meetingElement.getElementsByTagName("startDate").item(0).getTextContent();
								String endDate = meetingElement.getElementsByTagName("endDate").item(0).getTextContent();
								prunedSectionList.get(prunedSectionList.size() - 1).meetingsInSection.add(new Meeting(prunedSectionList.get(prunedSectionList.size() - 1), type, days, startTime,
										endTime, building, instructor, startDate, endDate));

							}

						}
						courseList.get(courseCount).partitionedSectionsInCourse = partitionSections(prunedSectionList);

						break;
					}

				}

			}

		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		} catch (SAXException saxe) {
			System.out.println(saxe.getMessage());

		}
	}

	public static void getUserInput() {

		Scanner in = new Scanner(System.in);
		System.out.println("Enter the courses you wish to take in Spring 2015, separated by commas.\nIf you're taking any honors or Special Topics course, add \":<section>\" to the end of the course" +
				" code");
		String input = in.nextLine();
		coursesTaken = new ArrayList<String>(Arrays.asList(input.split("\\s*,\\s*")));

		for (int i = 0; i < coursesTaken.size(); i++) {
			coursesTaken.set(i, coursesTaken.get(i).trim().toUpperCase());
		}

		System.out.println("\nAre you particular about having lunch breaks every day?");
		pref.lunchBreak = Pattern.compile("[yY][{es}{ES}]?|1").matcher(in.nextLine()).find();


		System.out.println("\nWill you be walking between classes?");
		pref.walk = Pattern.compile("[yY][{es}{ES}]?|1").matcher(in.nextLine()).find();

		System.out.println("\nEnter the maximum minutes in class in a day");
		pref.maxMinutesInADay = Integer.valueOf(in.nextLine());

		System.out.println("\nEnter the maximum minutes in class in a row");
		pref.maxMinutesInARow = Integer.valueOf(in.nextLine());

		System.out.println("\nIf you have any preferred instructors, enter their last names");
		input = in.nextLine();

		if (!(input.length() == 0)) {
			pref.preferredInstructors = new ArrayList<String>(Arrays.asList(input.split("(\\s*,\\s*)")));
			for (int i = 0; i < pref.preferredInstructors.size(); i++) {
				pref.preferredInstructors.set(i, StringUtils.capitalize(pref.preferredInstructors.get(i)));
			}
		}

		System.out.println("\nWould you like to avoid classes at any time of day (morning, afternoon or evening)?");
		input = in.nextLine();
		if (Pattern.compile("[mM][{orning}{ORNING}]?|1").matcher(input).find()) pref.avoidTime = 1;
		else if (Pattern.compile("[aA][{fternoon}{FTERNOON}]?|1").matcher(input).find()) pref.avoidTime = 2;
		else if (Pattern.compile("[eE][{vening}{VENING}]?|1").matcher(input).find()) pref.avoidTime = 3;
		else pref.avoidTime = 0;


		System.out.println("\nWould you like to avoid breaks between classes (short breaks, long breaks, all breaks)?");
		input = in.nextLine();
		if (Pattern.compile("[sS][{hort}{HORT}]?|1").matcher(input).find()) pref.avoidBreaksBetweenClasses = 1;
		else if (Pattern.compile("[aA][{ll}{LL}]?|3").matcher(input).find()) pref.avoidBreaksBetweenClasses = 3;
		else if (Pattern.compile("[lL][{ong}{ONG}]?|2").matcher(input).find()) pref.avoidBreaksBetweenClasses = 2;

		else pref.avoidBreaksBetweenClasses = 0;

		System.out.println("\nWould you like us to try to extend your weekend?");
		pref.extendWeekend = Pattern.compile("[yY][{es}{ES}]?|1").matcher(in.nextLine()).find();


		System.out.println("\nWould you like us to try to fit in a day with fewer classes than normal(0, 1 or 2)?");
		input = in.nextLine();
		if (Pattern.compile("[nN][oO]?|0").matcher(input).find()) pref.dayWithMinimum = 3;
		else pref.dayWithMinimum = Integer.valueOf(input);

	}


}