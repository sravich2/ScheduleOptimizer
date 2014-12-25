import java.io.IOException;
import java.net.MalformedURLException;
 

public class InfoSandbox {

	public static void main(String[] args) throws MalformedURLException, IOException {
		InfoRetriever ir = new InfoRetriever();
		long time = System.currentTimeMillis();
		ir.getSessionCoursesInfo("spring", "2015");
		System.out.println(System.currentTimeMillis()-time);
	}
}
