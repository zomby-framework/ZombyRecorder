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

package de.inovex.android.zombyrecorder.listener;

import java.util.List;

import de.inovex.android.zombyrecorder.model.AccelerometerData;
import de.inovex.android.zombyrecorder.model.MagneticFieldData;
import de.inovex.android.zombyrecorder.model.OrientationData;
import de.inovex.android.zombyrecorder.model.ProximityData;
import de.inovex.android.zombyrecorder.model.TemperatureData;
import de.inovex.android.zombyrecorder.model.ZombyData;
import de.inovex.android.zombyrecorder.util.ZombyTime;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class SensorListener implements SensorEventListener{

	private final String TAG = "SensorListener";
	private List<ZombyData> zombyDataList;
	
	private String oldAccelerometerValue;
	private String oldMagneticFieldValue;
	private String oldOrientationValue;
	private String oldProximityValue;
	private String oldTemperatureValue;
	
	public SensorListener(List<ZombyData> zombyDataList) {
		this.zombyDataList = zombyDataList;
		oldAccelerometerValue = "";
		oldMagneticFieldValue = "";
		oldOrientationValue = "";
		oldProximityValue = "";
		oldTemperatureValue = "";
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		
		switch(event.sensor.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				AccelerometerData accelerometerData = new AccelerometerData(x, y, z);
				if(!oldAccelerometerValue.contains(accelerometerData.toString())) {
					zombyDataList.add(new ZombyData(accelerometerData, ZombyTime.getTimeStamp()));
					Log.d(TAG , accelerometerData.toString());
				}
				oldAccelerometerValue = accelerometerData.toString();
				break;
			
			case Sensor.TYPE_MAGNETIC_FIELD:
				MagneticFieldData magneticFieldData = new MagneticFieldData(x, y, z);
				if(!oldMagneticFieldValue.contains(magneticFieldData.toString())) {
					zombyDataList.add(new ZombyData(magneticFieldData, ZombyTime.getTimeStamp()));
					Log.d(TAG , magneticFieldData.toString());
				}
				oldMagneticFieldValue = magneticFieldData.toString();
				break;
				
			case Sensor.TYPE_ORIENTATION:
				OrientationData orientationData = new OrientationData(x, y, z);
				if(!oldOrientationValue.contains(orientationData.toString())) {
					zombyDataList.add(new ZombyData(orientationData, ZombyTime.getTimeStamp()));
					Log.d(TAG , orientationData.toString());
				}
				oldOrientationValue = orientationData.toString();
				break;
				
			case Sensor.TYPE_PROXIMITY:
				ProximityData proximityData = new ProximityData(x);
				if(!oldProximityValue.contains(proximityData.toString())) {
					zombyDataList.add(new ZombyData(proximityData, ZombyTime.getTimeStamp()));
					Log.d(TAG , proximityData.toString());
				}
				oldProximityValue = proximityData.toString();
				break;
				
			case Sensor.TYPE_TEMPERATURE:
				TemperatureData temperatureData = new TemperatureData(x);
				if(!oldTemperatureValue.contains(temperatureData.toString())) {
					zombyDataList.add(new ZombyData(temperatureData, ZombyTime.getTimeStamp()));
					Log.d(TAG , temperatureData.toString());
				}
				oldTemperatureValue = temperatureData.toString();
				break;
				
			default: 
				Log.d(TAG , "Invalid sensor typ");
		}		
	}
}
