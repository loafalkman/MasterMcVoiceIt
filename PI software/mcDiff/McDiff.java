import java.io.File;

public class McDiff {
	File currWeekDir = new File("currweek");
	File lastWeekDir = new File("lastweek");
	File lastweekFile;

	public static void main(String[] args) {
		String day = args[0];
		int sensorID = Integer.parseInt(args[1]);
		new McDiff(sensorID).compare(sensorID, day);
	}

	public McDiff(int sensorID) {
		lastweekFile = new File(sensorID + ".txt");
	}

	public void compare(int sensorID, String day) {
		float lastweekTemp = getValueFor(day);
		float sensorTemp = getSensorValue(sensorID);
		System.out.println(lastweekTemp - sensorTemp);
	}

	private float getValueFor(String day) {
		return 0;
	}

	private float getSensorValue(int sensorID) {
		return 0;
	}
}