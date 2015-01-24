import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Created by Sachin
 */
public class DistanceRetriever {
	public static DistanceMatrix dm = new DistanceMatrix();

	public static double[] getTravelInfoBetweenBuildings(String fromBuilding, String toBuilding) {

		if (fromBuilding.equalsIgnoreCase(toBuilding)) return new double[]{0, 0};
		String fromAddress = null;
		String toAddress = null;
		Scanner in = null;
		try {
			in = new Scanner(new File("Buildings and Addresses.csv"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		while ((fromAddress == null || toAddress == null) && in.hasNext()) {
			String line = in.nextLine();
			if (fromAddress == null && line.split(",")[0].equalsIgnoreCase(fromBuilding)) fromAddress = line.split(",")[1];
			if (toAddress == null && line.split(",")[0].equalsIgnoreCase(toBuilding)) toAddress = line.split(",")[1];
		}

		String badBuilding = null;
		if (fromAddress == null) badBuilding = fromBuilding;
		else if (toAddress == null) badBuilding = toBuilding;

		if (badBuilding != null) {

			try {
				in = new Scanner(new File("Buildings and Addresses.csv"));

				int maxCommonCharacters = 0;

				String buildingMatch = in.nextLine().split(",")[0];

				while (in.hasNext()) {
					String line = in.nextLine();
					String buildingInLine = line.split(",")[0];

					int commonChars = countIdenticalCharsAfterSplittingString(buildingInLine, badBuilding);


					if (commonChars > maxCommonCharacters) {

						maxCommonCharacters = commonChars;
						buildingMatch = buildingInLine;

					}
				}

				PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("Buildings and Addresses.temp")));
				in = new Scanner(new File("Buildings and Addresses.csv"));

					while (in.hasNext()) {
						String line = in.nextLine();
						if (line.contains(buildingMatch)) {
							line = badBuilding + "," + line.split(",")[1];
						}
						writer.println(line);
					}
					writer.flush();

					writer.close();
					writer = null;
					in.close();
					in = null;
					System.gc();

					File realName = new File("Buildings and Addresses.csv");
					Files.delete(Paths.get("Buildings and Addresses.csv"));
					new File("Buildings and Addresses.temp").renameTo(realName);

					writer = new PrintWriter(new BufferedWriter(new FileWriter("log.temp")));
					in = new Scanner(new File("log.txt"));

					while (in.hasNext()) {
						String line = in.nextLine();
						writer.println(line);
					}
					writer.println(buildingMatch + " changed to " + badBuilding);
					writer.flush();
					writer.close();
					writer = null;
					in.close();
					in = null;
					System.gc();

					File realName2 = new File("log.txt");
					realName2.delete();
					new File("log.temp").renameTo(realName2);

					return getTravelInfoBetweenBuildings(fromBuilding, toBuilding);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return getTravelInfoBetweenAddresses(fromAddress, toAddress);
	}

	public static double[] getTravelInfoBetweenAddresses(String fromAddress, String toAddress) {

		if (fromAddress.equalsIgnoreCase(toAddress)) return new double[] {0.0, 0.0};
		Worker help = new Worker();
		double[] values = new double[2];

		JSONParser parser = new JSONParser();

		String urlString = "http://www.mapquestapi.com/directions/v2/route?key=Fmjtd%7Cluurn9u7ng%2Cbx%3Do5-9wznl0&outFormat=json&routeType=pedestrian&locale=en_US&from=" + help
				.parseLocationToURLFormat(fromAddress) + "&to=" + help.parseLocationToURLFormat(toAddress);

		try {
			URL url = new URL(urlString);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String s = reader.readLine();

			Object obj = parser.parse(s);

			JSONObject jObject = (JSONObject) obj;
			JSONObject route = (JSONObject) jObject.get("route");
			if (route.get("distance") instanceof Long)
			{
				values[0] = 0;
				values[1] = 0;
			}
			else{

				values[0] = (Double) route.get("distance");
				values[1] = ((Long) route.get("time")).doubleValue();
			}
			reader.close();
		} catch (ParseException pe) {
			System.out.println("position: " + pe.getPosition());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}


		return values;
	}


	public static int countIdenticalCharsIn2Strings(String s1, String s2) {
		int[] charsIn1 = new int[255];
		int[] charsIn2 = new int[255];

		int count = 0;
		char[] charArray;

		charArray = s1.toLowerCase().toCharArray();

		for (int i = 0; i < charArray.length; i++)
			charsIn1[charArray[i]]++;

		charArray = s2.toLowerCase().toCharArray();

		for (int i = 0; i < charArray.length; i++)
			charsIn2[charArray[i]]++;


		for (int i = 0; i < 255; i++) {
			if (charsIn1[i] != 0 && charsIn2[i] != 0) {
				count += Math.min(charsIn1[i], charsIn2[i]);
			}

		}
		return count;
	}

	public static int countIdenticalCharsAfterSplittingString(String s1, String s2){

		String[] a1 = s1.split(" ");
		String[] a2 = s2.split(" ");
		int count = 0;

		for (int i = 0;i < Math.min(a1.length, a2.length);i++){
			if (a1[i].equalsIgnoreCase(a2[i])) {
				count += 2 * a1[i].length();
			}
			else
				count += countIdenticalCharsIn2Strings(a1[i], a2[i]);
		}
		return count;
	}

	public static void main(String[] args) {


	}

}
