import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class sandbox
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		DistanceTimeMatrix help = new DistanceTimeMatrix();
		try {
			Scanner in = new Scanner(new File("Buildings and Addresses.csv"));


		in.nextLine();
		ArrayList<String[]> buildingsAndAddresses = new ArrayList<String[]>();
		
		while (!in.hasNext())
		{
			int i = 0;
			String line = in.nextLine();
			if (line.indexOf("\"") == -1)
				buildingsAndAddresses.add(line.split(","));
			else
			{
				int splitIndex = line.indexOf("\"", 2);
				line = line.replace("\"", "");
				buildingsAndAddresses.add(new String[] {line.substring(0, splitIndex-1), line.substring(splitIndex+1, line.length())});
			}
			i++;
		}


		for (int i = 0;i<buildingsAndAddresses.size();i++)
		{
			for (int j = 0;j<buildingsAndAddresses.size();j++)
			{
				if (i != j)
				{
					double[] distanceTimeMatrix = help.getTravelTimeAndDistance(buildingsAndAddresses.get(i)[1], buildingsAndAddresses.get(j)[1]);
					System.out.println(buildingsAndAddresses.get(i)[0] + ",");
					System.out.println(buildingsAndAddresses.get(j)[0] + ",");
					System.out.println(distanceTimeMatrix[0] + ","); //miles
					System.out.println(distanceTimeMatrix[1] + "\n"); //minutes
				}
			}
		}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
