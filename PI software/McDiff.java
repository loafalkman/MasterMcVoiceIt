import java.util.ArrayList;;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class McDiff {
	private File currWeekDir = new File("currweek");
	private File lastWeekDir = new File("lastweek");
	private File lastweekFile;

	/**
	 * In args: put weekday (three capital letters only) in first index, sensor id in second
	 * Example: args = {"MON", "135"} , returns currentTemp - lastMondayTemp.
	 * To compare weeks average, put "WEEKS" at first index.
	 * Example: args = {"WEEKS", "135"} , returns currentWeekAvg - lastWeekAvg.
	 */
	public static void main(String[] args) {
		String firstArg = args[0];
		int sensorID = Integer.parseInt(args[1]);

		if (firstArg.equals("WEEKS")) {
			McAverage last = new McAverage("LAST", sensorID);
			McAverage curr = new McAverage("CURRENT", sensorID);
			System.out.println(curr.calcAvg() - last.calcAvg());
		} else {
			new McDiff(sensorID).compare(sensorID, firstArg);
		}
	}

	public McDiff(int sensorID) {
		lastweekFile = new File(lastWeekDir, sensorID + ".txt");
	}

	public void compare(int sensorID, String day) {
		float lastweekTemp = getValueFor(day);
		float sensorTemp = getSensorValue(sensorID);
		System.out.println(sensorTemp - lastweekTemp);
	}

	private float getValueFor(String day) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(lastweekFile));
			String line;
			while ((line = reader.readLine()) != null) {

				String[] cols = line.split("\t");
				if (cols[0].equals(day)) {
					reader.close();
					return Float.parseFloat(cols[1]);
				}
			}
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;
	}

	private float getSensorValue(int sensorID) {
		return Float.parseFloat(getSensorLine(sensorID)[3].replace('°', ' ').trim());
	}

	private String[] getSensorLine(int sensorID) {
		for (String line : getDeviceLines(execCommand("tdtool -l"))) {
			String[] cols = line.split("\t");
			if (Integer.parseInt(cols[2].trim()) == sensorID) {
				return cols;
			}
		}

		return null;
	}

	private String[] getDeviceLines(String[] lines) {
		if (lines.length != 0 && lines[0].contains("Number of devices")) {
			String[] line0 = lines[0].split(" ");
			int actSize = Integer.valueOf(line0[line0.length - 1]);

			int wantedLines = lines.length - (actSize + 6);
			String[] sensorLines = new String[wantedLines];
			System.arraycopy(lines, actSize + 6, sensorLines, 0, wantedLines);
			return sensorLines;
		}
		return null;
	}

	private String[] execCommand(String cmd) {
		Runtime rt = Runtime.getRuntime();
		ArrayList<String> result = new ArrayList<>();

		try {
			Process pr = rt.exec(cmd);
			BufferedReader stdInput = new BufferedReader(
					new InputStreamReader(pr.getInputStream()));
			String s;
			while ((s = stdInput.readLine()) != null) {
				result.add(s);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toArray(new String[0]);
	}
}