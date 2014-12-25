import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
 

public class InfoSandbox {

	public static void main(String[] args) throws MalformedURLException, IOException {
		InfoRetreiver ir = new InfoRetreiver();
		String url = "http://courses.illinois.edu/cisapp/explorer/schedule/2014/fall/MATH/241.xml";
		String re = "<section id=\"[1-9].*?\" href=\"(.*)\">(.*)<";
		re = "<section id=\"([1-9].*?)\" href=\"(.*?)\">(.*?)<";
		
		//System.out.println(checkDept("MATHs"));
		String contents = ir.getPageXML(url);
		String sections = contents.substring(contents.indexOf("<sections>")+10);
		//ir.getCourseInfo("HIST",241,2014,"fall");
		//ir.getCourseInfo("CS",241,2014,"fall");
		long currentTime = System.currentTimeMillis();
		ir.getCourseInfo("ENG",100,2014,"fall");
		System.out.println((System.currentTimeMillis()-currentTime));
	}
	//@author tushar
	public static boolean checkDept(String dept)
	{
		boolean check=false;
		TextIO.readFile("dept.txt");
		String[] list= new String[200];
		int i=0;
		while (!TextIO.eof())
		{
			String line=TextIO.getln();
			line=line.trim();
			if (line.equals("<td>"))
				list[i++]=TextIO.getln().trim();
			TextIO.getln();
			TextIO.getln();
		}
		
		for (int a=0; a<list.length;a++)
			if (dept.equals(list[a]))
				check=true;
		return check;
	}

}
