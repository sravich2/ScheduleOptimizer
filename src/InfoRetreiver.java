/**
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author akshay
 *
 */
public class InfoRetreiver{
	public int val;
	public ArrayList<String> getPageSource() throws MalformedURLException, IOException{
		String url = "http://courses.illinois.edu/cisapp/explorer/schedule/2014/fall/MATH/241.xml";
		//url = "http://javarevisited.blogspot.com/2011/05/example-of-arraylist-in-java-tutorial.html";
		String charset = "UTF-8";
		String param1 = "value1";
		String param2 = "value2";
		
		ArrayList<String> stringList = new ArrayList<String>();
		
		InputStream response = new URL(url).openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(response));
		
		String line;
		while ((line = br.readLine()) != null) {
            stringList.add(line);
        }
	return stringList;
	}
	public String getPageXML(String url){
		//url = "http://javarevisited.blogspot.com/2011/05/example-of-arraylist-in-java-tutorial.html";	
		InputStream response;
		BufferedReader br;
		try {
			response = new URL(url).openStream();
			br = new BufferedReader(new InputStreamReader(response));
			return br.readLine();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	public String findInXml(String query, String xml){
		String open = String.format("<%s>", query);
		String close = String.format("</%s>", query);
		//int startIndex = open.length()+ xml.indexOf(open);
		int startIndex = xml.indexOf('>', xml.indexOf(query))+1;
		int endIndex = xml.indexOf('<',startIndex);
		return xml.substring(startIndex, endIndex);
	}
	public void getCourseInfo(String dept, int courseNum, int year, String session){
		String re = "<section id=\"([1-9].*?)\" href=\"(.*?)\">(.*?)<";
		String baseUrl = "http://courses.illinois.edu/cisapp/explorer/schedule/";
		
		String url = String.format("%s%d/%s/%s/%d.xml", baseUrl, year, session, dept, courseNum);
		String page= getPageXML(url);
		final Pattern courseP = Pattern.compile(re);
		final Matcher courseM = courseP.matcher(page);
		while (courseM.find()) {
		    String crn = courseM.group(1).trim();
		    String sUrl = courseM.group(2).trim();
		    String sCode = courseM.group(3).trim();
		    String sPage = getPageXML(sUrl);
		    Worker w = new Worker();
		    //sPage = sPage.substring(sPage.indexOf("<enrollmentStatus>"));
		    //<enrollmentStatus>(.*?)<.*?<startDate>(.*?)<.*?endDate>(.*?)</endDate>.*?code="(.*?)".*?<start>(.*?)</start><end>(.*?)</end><daysOfTheWeek>(.*?)<.*?buildingName>(.*?)>
		    String sRE = "(...)(...)";//<.*?<startDate>(.*?)<.*?endDate>(.*?)</endDate>.*?code=\"(.*?)\".*?<start>(.*?)</start><end>(.*?)</end><daysOfTheWeek>(.*?)<.*?buildingName>(.*?)<";
		    final Pattern sectionP = Pattern.compile(re);
			final Matcher sectionM = sectionP.matcher(page);
			String section = findInXml("sectionNumber", sPage);
			String status = findInXml("enrollmentStatus", sPage);
			String startDate = findInXml("startDate", sPage);
			String endDate = findInXml("endDate", sPage);
			String startTime = findInXml("start>", sPage);
			String endTime = findInXml("end>", sPage);
			String building = findInXml("buildingName", sPage);
			String days = findInXml("daysOfTheWeek",sPage);
			String type = findInXml("type",sPage);
			
			
			System.out.printf("\n\n\nSection: %s\n\n",section);
			System.out.printf("CRN: %s\n",crn);
			System.out.printf("Type: %s\n",type);
			System.out.printf("Status: %s\n",status);
			System.out.printf("Time range: %s - %s\n",startTime,endTime);
//			int startTime10 = w.c(startTime);
//			int endTime10 = w.convertTimeBase60To10(endTime);
//			System.out.printf("Time range (Base 10): %s - %s\n",startTime10,endTime10);
			System.out.printf("Date range: %s - %s\n",startDate,endDate);
			System.out.printf("Days: %s\n", days);
			System.out.printf("Building name: %s\n", building);
		    
		    //System.out.println(String.format("section id [%s]\n page [%s]\n section code[%s]", sId, sPage, sCode));
		    //System.out.println(sPage);
		}
	}
}
