import java.io.IOException;
import java.net.MalformedURLException;
 

public class InfoSandbox2 {

	public static void main(String[] args) throws MalformedURLException, IOException {
		InfoRetriever2 ir = new InfoRetriever2();
		long time = System.currentTimeMillis();
		ir.getSessionCoursesInfo("spring", "2015");
		System.out.println(System.currentTimeMillis()-time);
	}
}
