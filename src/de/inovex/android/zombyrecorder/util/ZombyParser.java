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

package de.inovex.android.zombyrecorder.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;

import de.inovex.android.framework.zomby.core.CorePower.BatteryStatus;
import de.inovex.android.framework.zomby.player.ZombyPlayer.DataElement;
import de.inovex.android.framework.zomby.util.ZombyException;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class ZombyParser {
	
	public static String fromZombyFileToJavaFile(InputStream inputStream, List<DataElement> dataElementList, double timeFactor) {
		String javaFile = "";
		String space = "   ";
		
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
				
				if(dataElementList == null);
				else if(!dataElementList.contains(DataElement.valueOf(dataTag)))
					continue;
				
				if(oldTimeStamp != 0) {
					long timeToWait = Math.round((timeStamp - oldTimeStamp) * timeFactor);
					
					if(timeToWait > 10)
						javaFile += "  waitForRealtime(" + timeToWait + ");\n";
					else
						javaFile += "\n";
				}
						
				oldTimeStamp = timeStamp;
				
				switch(DataElement.valueOf(dataTag)) {
					case BATTERY:
						String [] batteryData = parameter.split(Pattern.quote(","));
						if(batteryData.length == 2) {
							int capacityValue = Integer.valueOf(batteryData[0]);
							BatteryStatus batteryStatus = BatteryStatus.valueOf(batteryData[1]);
							javaFile += space + "Zomby.getCorePower().capacity(" + capacityValue + ");\n";
							javaFile += space + "Zomby.getCorePower().status(BatteryState." + batteryStatus + ");";
						} 
						break;
						
					case NETWORK:	
						javaFile += space + "Zomby.getCoreNetwork().speed(NetworkSpeed.valueOf(\"" + parameter + "\"));";
						break;
						
					case LOCATION:
						String [] locationData = parameter.split(Pattern.quote(","));
						if(locationData.length == 2) {
							double longitude = Double.valueOf(locationData[0]);
							double latitude = Double.valueOf(locationData[1]);
							javaFile += space + "Zomby.getCoreGeo().fix(" + longitude + ", " + latitude + ");";
						} 
						break;
						
					case ACCELEROMETER:
						String [] accelerometerData = parameter.split(Pattern.quote(","));
						if(accelerometerData.length == 3) {
							float x = Float.valueOf(accelerometerData[0]);
							float y = Float.valueOf(accelerometerData[1]);
							float z = Float.valueOf(accelerometerData[2]);
							javaFile += space + "Zomby.getCoreSensor().set(Sensorname.ACCELERATION, " + x + ", " + y + ", " + z + ");";
						} 
						break;
					
					case MAGNETIC_FIELD:
						String [] magneticFieldData = parameter.split(Pattern.quote(","));
						if(magneticFieldData.length == 3) {
							float x = Float.valueOf(magneticFieldData[0]);
							float y = Float.valueOf(magneticFieldData[1]);
							float z = Float.valueOf(magneticFieldData[2]);
							javaFile += space + "Zomby.getCoreSensor().set(Sensorname.MAGNETIC_FIELD, " + x + ", " + y + ", " + z + ");";
						} 
						break;
					
					
					case ORIENTATION:
						String [] orientationData = parameter.split(Pattern.quote(","));
						if(orientationData.length == 3) {
							float x = Float.valueOf(orientationData[0]);
							float y = Float.valueOf(orientationData[1]);
							float z = Float.valueOf(orientationData[2]);
							javaFile += space + "Zomby.getCoreSensor().set(Sensorname.ORIENTATION, " + x + ", " + y + ", " + z + ");";
						} 
						break;
				
					case PROXIMITY:
						javaFile += space + "Zomby.getCoreSensor().set(Sensorname.PROXIMITY, " + parameter + ");";
						break;
					
					case TEMPERATURE:
						javaFile += space + "Zomby.getCoreSensor().set(Sensorname.TEMPERATURE, " + parameter + ");";
						break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return javaFile;
	}
}
