import java.util.ArrayList;
import java.util.Arrays;


public class sandbox2
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		DistanceTimeMatrix help = new DistanceTimeMatrix();
		TextIO.readFile("Buildings and Addresses.csv");
		TextIO.getln();
		ArrayList<String[]> buildingsAndAddresses = new ArrayList<String[]>();
		
		while (!TextIO.eof())
		{
			int i = 0;
			String line = TextIO.getln();
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
		TextIO.writeStandardOutput();
		
		for (int i = 0;i<buildingsAndAddresses.size();i++)
		{
			for (int j = 0;j<buildingsAndAddresses.size();j++)
			{
				if (i != j)
				{
					double[] distanceTimeMatrix = help.getTravelTimeAndDistance(buildingsAndAddresses.get(i)[1], buildingsAndAddresses.get(j)[1]);
					TextIO.put(buildingsAndAddresses.get(i)[0] + ",");
					TextIO.put(buildingsAndAddresses.get(j)[0] + ",");
					TextIO.put(distanceTimeMatrix[0] + ","); //miles
					TextIO.put(distanceTimeMatrix[1] + "\n"); //minutes
				}
			}
		}
		
	}

}
