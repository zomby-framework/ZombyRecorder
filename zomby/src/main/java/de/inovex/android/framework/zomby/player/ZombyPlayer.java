/* Copyright 2013 inovex GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.inovex.android.framework.zomby.player;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;

import android.util.Log;
import de.inovex.android.framework.zomby.Zomby;
import de.inovex.android.framework.zomby.core.CoreGSM.GsmMode;
import de.inovex.android.framework.zomby.core.CoreNetwork.NetworkSpeed;
import de.inovex.android.framework.zomby.core.CorePower.BatteryStatus;
import de.inovex.android.framework.zomby.core.CoreSensor.Sensorname;
import de.inovex.android.framework.zomby.util.ZombyException;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class ZombyPlayer {

	private static final String TAG = "ZombyPlayer";
	
	public static enum DataElement {
		LOCATION("LOCATION"),
		BATTERY("BATTERY"),
		NETWORK("NETWORK"),
		ACCELEROMETER("ACCELEROMETER"),
		MAGNETIC_FIELD("MAGNETIC_FIELD"),
		ORIENTATION("ORIENTATION"),
		PROXIMITY("PROXIMITY"),
		TEMPERATURE("TEMPERATURE");
		
		private String dataElement;

		private DataElement(String dataElement) {
			this.dataElement = dataElement;
		}
		
		public String getValue() {
			return dataElement;
		}
	};
	
	/** 
	 * simulate the properties of sensors from a real device in real time
	 * NOTE: NOTE: method runs in a new thread
	 * @param inputStream expected an input stream of a zomby file (*.zf)
	 * @throws ZombyException
	 * @throws InterruptedException
	 */
	public void playMyRecord(InputStream inputStream) throws ZombyException, InterruptedException {
		playMyRecord(inputStream, null, 1);
	}

	public void playMyRecord(InputStream inputStream, List<DataElement> dataElementList) throws ZombyException, InterruptedException {
		playMyRecord(inputStream, dataElementList, 1);
	}

	/**
	 * simulate the properties of sensors from a real device in <var>timeFactor</var> x real time
	 * NOTE: method runs in a new thread
	 * @param inputStream inputStream expected an input stream of a zomby file (*.zf)
	 * @param timeFactor factor of real time
	 * @throws ZombyException
	 * @throws InterruptedException
	 */
	public void playMyRecord(final InputStream inputStream, final List<DataElement> dataElementList, final double timeFactor) throws ZombyException, InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					InputStreamReader isr = new InputStreamReader(inputStream);
					BufferedReader br = new BufferedReader(isr);
					String dataString;
					long oldTimeStamp = 0;
					while ((dataString = br.readLine()) != null) {
						String [] field = dataString.split(Pattern.quote(";"));
						if(field.length != 3)
							throw new ZombyException("invalid values in record file");
						
						long timeStamp = Long.valueOf(field[0]);
						String dataTag = field[1];
						String parameter = field[2];
						
						// 
						if(dataElementList == null);
						else if(!dataElementList.contains(DataElement.valueOf(dataTag)))
							continue;
					
						if(oldTimeStamp != 0) {
							long timeToWait = Math.round((timeStamp - oldTimeStamp) * timeFactor);
							Log.d(TAG, "i am waiting for " + timeToWait + "ms");
							synchronized(this) {
								if(timeToWait > 10)
									wait(timeToWait);
							}
						}
						
						oldTimeStamp = timeStamp;
						
						switch(DataElement.valueOf(dataTag)) {
						case BATTERY:	
							String [] batteryData = parameter.split(Pattern.quote(","));
							Log.d(TAG, "case BATTERY");
							if(batteryData.length == 2) {
								int capacityValue = Integer.valueOf(batteryData[0]);
								int batteryChargingState = Integer.valueOf(batteryData[1]);
								BatteryStatus batteryState = BatteryStatus.getBatteryStatus(batteryChargingState);
								Log.d(TAG, "split: " + capacityValue + ", " + batteryState);
								Zomby.getCorePower().capacity(capacityValue);	
								Zomby.getCorePower().status(batteryState);	
							} else
								Log.d(TAG, "invalid location data length: " + batteryData.length);
							break;
							
						case NETWORK:	
							Log.d(TAG, "case NETWORK");
							if(parameter.contains("CONNECTED"))
									Zomby.getCoreGSM().data(GsmMode.ON);	
							//else if(parameter.contains("DISCONNECTED"))
								//Zomby.getCoreGSM().data(GsmMode.OFF);
							else
								Zomby.getCoreNetwork().speed(NetworkSpeed.valueOf(parameter));	
							break;
							
						case LOCATION:
							String [] locationData = parameter.split(Pattern.quote(","));
							Log.d(TAG, "case GEO");
							if(locationData.length == 2) {
								double longitude = Double.valueOf(locationData[0]);
								double latitude = Double.valueOf(locationData[1]);
								Log.d(TAG, "split: " + longitude + ", " + latitude);
								Zomby.getCoreGeo().fix(longitude, latitude);
							} else
								Log.d(TAG, "invalid location data length: " + locationData.length);
							break;
							
						case ACCELEROMETER:
							String [] accelerometerData = parameter.split(Pattern.quote(","));
							Log.d(TAG, "case ACCELEROMETER");
							if(accelerometerData.length == 3) {
								float x = Float.valueOf(accelerometerData[0]);
								float y = Float.valueOf(accelerometerData[1]);
								float z = Float.valueOf(accelerometerData[2]);
								Log.d(TAG, "split: " + x + ", " + y + ", " + z);
								Zomby.getCoreSensor().set(Sensorname.ACCELERATION, x, y, z);
							} else
								Log.d(TAG, "invalid accelerometer data length: " + accelerometerData.length);
							break;
							
						case MAGNETIC_FIELD:
							String [] magneticFieldData = parameter.split(Pattern.quote(","));
							Log.d(TAG, "case MAGNETIC_FIELD");
							if(magneticFieldData.length == 3) {
								float x = Float.valueOf(magneticFieldData[0]);
								float y = Float.valueOf(magneticFieldData[1]);
								float z = Float.valueOf(magneticFieldData[2]);
								Log.d(TAG, "split: " + x + ", " + y + ", " + z);
								Zomby.getCoreSensor().set(Sensorname.MAGNETIC_FIELD, x, y, z);
							} else
								Log.d(TAG, "invalid magnetic field data length: " + magneticFieldData.length);
							break;
							
							
						case ORIENTATION:
							String [] orientationData = parameter.split(Pattern.quote(","));
							Log.d(TAG, "case ORIENTATION");
							if(orientationData.length == 3) {
								float x = Float.valueOf(orientationData[0]);
								float y = Float.valueOf(orientationData[1]);
								float z = Float.valueOf(orientationData[2]);
								Log.d(TAG, "split: " + x + ", " + y + ", " + z);
								Zomby.getCoreSensor().set(Sensorname.ORIENTATION, x, y, z);
							} else
								Log.d(TAG, "invalid orientation data length: " + orientationData.length);
							break;
							
						case PROXIMITY:
							Log.d(TAG, "case PROXIMITY");
							Zomby.getCoreSensor().set(Sensorname.PROXIMITY, Double.valueOf(parameter));
							break;
							
						case TEMPERATURE:
							Log.d(TAG, "case TEMPERATURE");
							Zomby.getCoreSensor().set(Sensorname.TEMPERATURE, Double.valueOf(parameter));
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
