import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DistanceTimeMatrix {

	public static double[] getTravelTimeAndDistance(String fromAddress, String toAddress) {
		Worker help = new Worker();
		double[] values = new double[2];

		JSONParser parser = new JSONParser();

		String urlString = "http://www.mapquestapi.com/directions/v2/route?key=Fmjtd%7Cluurn9u7ng%2Cbx%3Do5-9wznl0&outFormat=json&routeType=pedestrian&locale=en_US&from="
		        + help.parseLocationToURLFormat(fromAddress)
		        + "&to="
		        + help.parseLocationToURLFormat(toAddress);
		long time = System.currentTimeMillis();

		try {
			URL url = new URL(urlString);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String s = reader.readLine();

			Object obj = parser.parse(s);

			JSONObject jObject = (JSONObject) obj;
			JSONObject route = (JSONObject) jObject.get("route");
			//values[0] = (long)route.get("distance");
			//values[1] = (long)(route.get("time"));
			reader.close();
		} catch (ParseException pe) {
			System.out.println("position: " + pe.getPosition());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}


		return values;
	}

	public static void main(String[] args) {
		Worker help = new Worker();

		double[] matrix = getTravelTimeAndDistance("607 S. MATHEWS URBANA IL",
		        "1110 W. MAIN URBANA IL");
		System.out.println(Arrays.toString(matrix));

	}

}