/**
 * 
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Sachin
 *
 */
//http://www.mkyong.com/java/how-to-create-xml-file-in-java-dom/
//http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
public class InfoRetriever {

	static final StringBuffer baseUrl = new StringBuffer("http://courses.illinois.edu/cisapp/explorer/schedule/");
	static DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	static DocumentBuilder dBuilder;

	public static void main(String[] args) throws IOException {
		final String semester = "spring";
		final String year = "2015";
		long time = System.currentTimeMillis();
		getSessionCoursesInfo(semester, year);
		System.out.println(System.currentTimeMillis()-time);
	}

	public static void getSessionCoursesInfo(String session, String year)
	{

		StringBuilder sessionUrl = new StringBuilder(baseUrl);
		sessionUrl.append(year).append("/").append(session).append(".xml");
		try
		{
			dBuilder = dbFactory.newDocumentBuilder();
			
			Document finalXML = dBuilder.newDocument();
			Element rootElement = finalXML.createElement(session + year);
			finalXML.appendChild(rootElement); 
			
			Document sessionInfo = dBuilder.parse((sessionUrl.toString()));
			NodeList deptList = sessionInfo.getElementsByTagName("subject");

			for (int i = 0; i < deptList.getLength(); i++) {
				Node dept = deptList.item(i);

				if (dept.getNodeType() == Node.ELEMENT_NODE) {
					Element currentDept = (Element) dept;
					String Dept = currentDept.getAttribute("id");
					System.out.println(Dept);
					if (Dept.equals("AAS"))
						getDepartmentCoursesInfo(finalXML, rootElement, Dept,
						        year, session);
				}
			}
		
           TransformerFactory transformerFactory = TransformerFactory.newInstance();
           Transformer transformer = transformerFactory.newTransformer();
           //for pretty print
           transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
           transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
           transformer.setOutputProperty(OutputKeys.INDENT, "yes");
           transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

           DOMSource source = new DOMSource(finalXML);

//         StreamResult console = new StreamResult(System.out);
           StreamResult file = new StreamResult(new File("/Users/Sachin/workspace/Schedule Optimization/courseData2.xml"));
           //transformer.transform(source, console);
           transformer.transform(source, file);
           System.out.println("DONE");	
			
		} catch (IOException ioe)
		{
			System.out.println(ioe.getMessage());
		} catch (SAXException saxe)
		{
			System.out.println(saxe.getMessage());
		} catch (ParserConfigurationException pce)
		{
			System.out.println(pce.getMessage());
		} catch (TransformerException te)
		{
			System.out.println(te.getMessage());
		}
	}

	public static void getDepartmentCoursesInfo(Document doc, Element root, String dept, String year,
			String session)
	{
		StringBuilder deptUrl = new StringBuilder(baseUrl);
		deptUrl.append(year).append("/").append(session).append("/").append(dept).append(".xml");
		
		try
		{
			dBuilder = dbFactory.newDocumentBuilder();
			Document deptInfo = dBuilder.parse(deptUrl.toString());
			
			String deptName = deptInfo.getElementsByTagName("unitName").item(0).getTextContent();
			Node deptNode = root.appendChild(createDept(doc, dept, deptName));
			
			NodeList coursesOffered = deptInfo.getElementsByTagName("course");

			for (int i = 0; i < coursesOffered.getLength(); i++) {
				Node course = coursesOffered.item(i);
				if (course.getNodeType() == Node.ELEMENT_NODE) {
					Element currentCourse = (Element) course;
					String courseNum = (currentCourse.getAttribute("id"));
					System.out.println(courseNum);
					getCourseInfo(doc, (Element) deptNode, dept, courseNum,
					        year, session);
				}
			}

		} catch (IOException ioe)
		{
			System.out.println(ioe.getMessage());
		} catch (SAXException saxe)
		{
			System.out.println(saxe.getMessage());
		} catch (ParserConfigurationException pce)
		{
			System.out.println(pce.getMessage());
		}

	}

	public static void getCourseInfo(Document doc, Element deptNode, String dept, String courseNum,
			String year, String session)
	{
		
		StringBuilder courseUrl = new StringBuilder(baseUrl);
		courseUrl.append(year).append("/").append(session).append("/").append(dept).append("/").append(courseNum);
		StringBuffer url = new StringBuffer(courseUrl);
		url.append(".xml");

		ArrayList<String> CRNList = new ArrayList<String>();
		ArrayList<String> sectionName = new ArrayList<String>();
		
		Node courseNode = null;
		
		String creditHours = "";
		
		try {
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();

			Document courseInfo = dBuilder.parse(url.toString());
			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			// courseInfo.getDocumentElement().normalize();

			creditHours = courseInfo.getElementsByTagName("creditHours").item(0).getTextContent();
			
			if (creditHours.contains("."))
				creditHours = creditHours.substring(0, creditHours.length()-1);
			
			String courseName = courseInfo.getElementsByTagName("label").item(0).getTextContent();
			
			courseNode = deptNode.appendChild(createCourse(doc, courseNum, courseName,
					creditHours));
			
			NodeList sectionList = courseInfo.getElementsByTagName("section");

			for (int i = 0; i < sectionList.getLength(); i++) {
				Node currentSection = sectionList.item(i);

				if (currentSection.getNodeType() == Node.ELEMENT_NODE) {
					Element sectionElement = (Element) currentSection;
					CRNList.add(sectionElement.getAttribute("id"));
					sectionName.add(sectionElement.getTextContent());
				}
			}

		} catch (IOException ioe)
		{
			System.out.println(ioe.getLocalizedMessage());
		} catch (ParserConfigurationException pce)
		{
			System.out.println(pce.getMessage());
		} catch (SAXException saxe)
		{
			System.out.println(saxe.getMessage());
		}

		for (String CRN : CRNList) {
			url = new StringBuffer(courseUrl);
			url.append("/").append(CRN).append(".xml");
			try {
				Document currentSectionInfo = dBuilder.parse(url.toString());

				String startDate = "";
				String endDate = "";
				String name = "";
				String status = "";
				String sectionCreditHours;

				NodeList statusNL;
				NodeList nameNL;
				NodeList creditHoursNL;

				if ((currentSectionInfo.getElementsByTagName("startDate")).getLength() != 0)
					startDate = currentSectionInfo.getElementsByTagName("startDate").item(0).getTextContent();
				if ((currentSectionInfo.getElementsByTagName("endDate")).getLength() != 0)
					endDate = currentSectionInfo.getElementsByTagName("endDate").item(0).getTextContent();
				if ((nameNL = currentSectionInfo.getElementsByTagName("sectionNumber")).getLength() != 0)
					name = nameNL.item(0).getTextContent().trim();
				if ((statusNL = currentSectionInfo.getElementsByTagName("enrollmentStatus")).getLength() != 0)
					status = statusNL.item(0).getTextContent();

				if ((creditHoursNL = currentSectionInfo.getElementsByTagName("creditHours")).getLength() != 0)
					sectionCreditHours = creditHoursNL.item(0).getTextContent();
				else
					sectionCreditHours = creditHours;

				assert courseNode != null;
				Node sectionNode = courseNode.appendChild(createSection(doc, name, CRN, status,
						sectionCreditHours));

				NodeList meetingList = currentSectionInfo.getElementsByTagName("meeting");

				for (int j = 0; j < meetingList.getLength(); j++) {
					Element current = (Element) meetingList.item(j);

					String type = "";
					String startTime = "";
					String endTime = "";
					String daysOfTheWeek = "";
					String instructor = "";
					String building = "";

					NodeList startNL;
					NodeList endNL;
					NodeList typeNL;
					NodeList daysOfTheWeekNL;
					NodeList instructorNL;
					NodeList buildingNL;

					if ((typeNL = current.getElementsByTagName("type")).getLength() != 0)
						type = typeNL.item(0).getTextContent();
					if ((startNL = current.getElementsByTagName("start")).getLength() != 0)
						startTime = startNL.item(0).getTextContent();
					if ((endNL = current.getElementsByTagName("end")).getLength() != 0)
						endTime = endNL.item(0).getTextContent();
					if ((daysOfTheWeekNL = current.getElementsByTagName("daysOfTheWeek")).getLength() != 0)
						daysOfTheWeek = daysOfTheWeekNL.item(0).getTextContent().trim();
					if ((buildingNL = current.getElementsByTagName("buildingName")).getLength() != 0)
						building = buildingNL.item(0).getTextContent();
					if ((instructorNL = current.getElementsByTagName("instructor")).getLength() != 0)
						instructor = instructorNL.item(0).getTextContent();

					sectionNode.appendChild(createMeeting(doc, type, daysOfTheWeek, startDate, endDate,
							startTime, endTime, instructor, building));
				}
			} catch (SAXException saxe) {
				System.out.println(saxe.getMessage());
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		}
	}

	private static Node createCourse(Document doc, String code, String name,
	        String creditHours) {
		Element course = doc.createElement("course");

		course.setAttribute("id", code);
		course.setIdAttribute("id", true);
		course.setAttribute("name", name);

		course.appendChild(createElement(doc, "creditHours", creditHours));
		
		return course;
	}
	
	private static Node createDept(Document doc, String abbr, String name)
	{
		Element dept = doc.createElement("dept");

		dept.setAttribute("id", abbr);
		dept.setIdAttribute("id", true);
		dept.setAttribute("name", name);
		
		return dept;
	}
	

	private static Node createSection(Document doc, String name, String CRN, String status, String creditHours)
	{
		Element section = doc.createElement("section");

		section.setAttribute("id", CRN);
		section.setIdAttribute("id", true);
		section.setAttribute("sectionNumber", name);
		section.setAttribute("status", status);
		
		section.appendChild(createElement(doc, "creditHours", creditHours));

		return section;
	}
	
	private static Node createMeeting(Document doc, String type, String daysOfTheWeek, String startDate, String endDate,
			String startTime, String endTime, String instructor, String building)
	{
		Element meeting = doc.createElement("meeting");
		
		meeting.setAttribute("type", type);
		
		meeting.appendChild(createElement(doc, "daysOfTheWeek", daysOfTheWeek));
		meeting.appendChild(createElement(doc, "startDate", startDate));
		meeting.appendChild(createElement(doc, "endDate", endDate));
		meeting.appendChild(createElement(doc, "startTime", startTime));
		meeting.appendChild(createElement(doc, "endTime", endTime));
		meeting.appendChild(createElement(doc, "instructor", instructor));
		meeting.appendChild(createElement(doc, "building", building));
		
		return meeting;
	}
	

	//utility method to create text node
	private static Node createElement(Document doc, String name, String value)
	{
		Element node = doc.createElement(name);
		
		node.appendChild(doc.createTextNode(value));
		
		return node;
	}

}