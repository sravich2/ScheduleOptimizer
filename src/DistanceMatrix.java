import java.io.*;
import java.util.*;


import com.google.common.collect.*;
import org.apache.commons.lang3.StringUtils;

public class DistanceMatrix {

	public HashSet<String> buildingList = new HashSet<String>();
	public static Table<String, String, double[]> matrix = HashBasedTable.create();
	public int order = buildingList.size();

	public String toString() {
		StringBuilder output = new StringBuilder();
		String[] rowKeys = matrix.rowKeySet().toArray(new String[matrix.rowKeySet().size()]);
		for (String row : rowKeys){

			output.append("(");
			for (String column : matrix.columnKeySet()){
				output.append(Arrays.toString(matrix.get(row, column))).append(", ");
			}
			output = output.delete(output.length()-2, output.length());
			output.append(")\n");
		}
		return output.toString();
	}

	public void addBuilding(String buildingToAdd) {

		String[] rowKeys = matrix.rowKeySet().toArray(new String[matrix.rowKeySet().size()]);

		for (String building : rowKeys){
			double[] info = DistanceRetriever.getTravelInfoBetweenBuildings(building, buildingToAdd);
			matrix.put(building, buildingToAdd, info);
			matrix.put(buildingToAdd, building, info);
		}
		matrix.put(buildingToAdd, buildingToAdd, new double[] {0.0, 0.0});
		buildingList.add(buildingToAdd);
	}

	public double[] getInfo(String fromBuilding, String toBuilding) {
		return matrix.get(fromBuilding, toBuilding);
	}

}
