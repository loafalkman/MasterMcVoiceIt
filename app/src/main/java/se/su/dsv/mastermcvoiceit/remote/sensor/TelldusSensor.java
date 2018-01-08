package se.su.dsv.mastermcvoiceit.remote.sensor;

import android.util.ArrayMap;
import android.util.Log;

import java.util.Collection;

import se.su.dsv.mastermcvoiceit.remote.SSHConnDetails;
import se.su.dsv.mastermcvoiceit.remote.SSHUtil;

/**
 * Created by felix on 2017-11-24.
 */

public class TelldusSensor implements Sensor {
    private int id;
    private String name;
    private SensorType[] types;
    private float[] sensorValues = new float[SensorType.values().length];

    public TelldusSensor(int id, String name, SensorType[] supportedTypes) {
        this.id = id;
        this.name = name;
        this.types = supportedTypes;
    }

    public float[] getSensorValues() {
        return sensorValues;
    }

    public int getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SensorType[] getSupportedTypes() {
        return types;
    }


    public static class TelldusSensorBatch implements Batch<TelldusSensor> {
        private SSHConnDetails connDetails;
        private ArrayMap<Integer, TelldusSensor> batch = new ArrayMap<>();

        public TelldusSensorBatch(SSHConnDetails connDetails) {
            this.connDetails = connDetails;
            initBatch();
        }

        @Override
        public void updateBatch() {
            String list = SSHUtil.runCommand("tdtool -l", connDetails);
            String[] deviceLines = getDeviceLines(list);

            if (deviceLines == null)
                throw new IllegalStateException("SSH command 'tdtool -l' is faulty (is telldus installed?)");

            for (String devLine : deviceLines) {
                String[] cols = devLine.split("\t");
                updateSensor(cols);
            }
        }

        private void updateSensor(String[] cols) {
            String typeCol = cols[1].trim();
            int id = Integer.parseInt(cols[2].trim());
            TelldusSensor sensor = batch.get(id);

            if (sensor == null)
                throw new IllegalStateException("Remote added sensor with id " + id + ", please restart app");

            int tempIndx = SensorType.TEMPERATURE.ordinal();
            int humidIndx = SensorType.HUMIDITY.ordinal();
            switch (typeCol) {
                case "temperature":
                    sensor.sensorValues[tempIndx] =
                            Float.parseFloat(cols[3].replace('°', ' ').trim());
                    sensor.types = new SensorType[]{SensorType.TEMPERATURE};
                    break;

                case "temperaturehumidity":
                    sensor.sensorValues[tempIndx] =
                            Float.parseFloat(cols[3].replace('°', ' ').trim());
                    sensor.sensorValues[humidIndx] =
                            Float.parseFloat(cols[4].replace('%', ' ').trim());
                    sensor.types = new SensorType[]{SensorType.TEMPERATURE, SensorType.HUMIDITY};
                    break;

                default:
                    Log.v("TelldusSensor", "Unknown sensortype: " + typeCol);
            }
        }

        @Override
        public Collection<TelldusSensor> getSensors() {
            return batch.values();
        }

        private void initBatch() {
            String list = SSHUtil.runCommand("tdtool -l", connDetails);
            String[] deviceLines = getDeviceLines(list);

            if (deviceLines == null)
                throw new IllegalStateException("SSH command 'tdtool -l' is faulty (is telldus installed?)");

            for (String devLine : deviceLines) {
                String[] cols = devLine.split("\t");
                makeSensor(cols);
            }
        }

        // TODO: How to handle if there are no actuators, lines[0] = "SENSORS:\n" then?
        private String[] getDeviceLines(String input) {
            String[] lines = input.split("\\n");

            if (!input.isEmpty() && lines[0].contains("Number of devices")) {
                String[] line0 = lines[0].split(" ");
                int actSize = Integer.valueOf(line0[line0.length - 1]);

                int wantedLines = lines.length - (actSize + 6);
                String[] sensorLines = new String[wantedLines];
                System.arraycopy(lines, actSize + 6, sensorLines, 0, wantedLines);
                return sensorLines;
            }
            return null;
        }

        private void makeSensor(String[] cols) {
            String typeCol = cols[1].trim();
            int id = Integer.parseInt(cols[2].trim());
            String name = "id " + id; //TODO: store name and id in a file? or program something on the raspberry to send name aswell?

            switch (typeCol) {
                case "temperature":
                    SensorType[] types = new SensorType[]{SensorType.TEMPERATURE};
                    batch.put(id, new TelldusSensor(id, name, types));
                    break;

                case "temperaturehumidity":
                    SensorType[] typesA = new SensorType[]{SensorType.TEMPERATURE, SensorType.HUMIDITY};
                    batch.put(id, new TelldusSensor(id, name, typesA));
                    break;

                default:
                    Log.v("TelldusSensor", "Unknown sensortype: " + typeCol);
            }
        }
    }

}
