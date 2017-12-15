import java.util.ArrayList;;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class McAverage {
	private File currWeekDir = new File("currweek");
	private File lastWeekDir = new File("lastweek");
	private File weekFile;

	private boolean lastWeek;
	private int sensorID;

	/**
	 * args[0] specifies week, args[1] specifies sensor.
	 * example: args = {"LAST", "135"} or args = {"CURRENT", "135"}
	 */
	public static void main(String[] args) {
		String week = args[0];
		int sensorID = Integer.parseInt(args[1]);
		new McAverage(week, sensorID).calcAvg();
	}

	public McAverage(String week, int sensorID) {
		lastWeek = week.equals("LAST");
		this.sensorID = sensorID;

		if (lastWeek)
			weekFile = new File(lastWeekDir, sensorID + ".txt");
		else
			weekFile = new File(currWeekDir, sensorID + ".txt");
	}

	public void calcAvg() {
		ArrayList<Float> weekTemps = getWeekTemps(weekFile);
		float added = 0;
		for (float temp : weekTemps)
			added += temp;
		System.out.println(added / weekTemps.size());
	}

	private ArrayList<Float> getWeekTemps(File weekFile) {
		ArrayList<Float> ret = new ArrayList<>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(weekFile));
			String line;
			while ((line = reader.readLine()) != null) {
				
				String[] cols = line.split("\t");
				ret.add(Float.parseFloat(cols[1]));
			}
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}
}