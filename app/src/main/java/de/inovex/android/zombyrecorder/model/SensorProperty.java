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

package de.inovex.android.zombyrecorder.model;

import android.hardware.Sensor;
import de.inovex.android.zombyrecorder.R;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public enum SensorProperty {
	LOCATION(0, R.id.locationCheckBox),
	BATTERY(1, R.id.batteryCheckBox),
	NETWORK(2, R.id.networkCheckBox),
	ACCELEROMETER(3, R.id.accelerometerCheckBox, Sensor.TYPE_ACCELEROMETER),
	MAGNETIC_FIELD(4, R.id.magneticFieldCheckBox, Sensor.TYPE_MAGNETIC_FIELD),
	ORIENTATION(5, R.id.orientationCheckBox, Sensor.TYPE_ORIENTATION),
	PROXIMITY(6, R.id.proximityCheckBox, Sensor.TYPE_PROXIMITY),
	TEMPERATURE(7, R.id.temperatureCheckBox, Sensor.TYPE_TEMPERATURE);
	
	private int index;
	private int id;
	private int sensortyp;

	private SensorProperty(int index, int id) {
		this.index = index;
		this.id = id;
	}

	private SensorProperty(int index, int id, int sensortyp) {
		this.index = index;
		this.id = id;
		this.sensortyp = sensortyp;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getId() {
		return id;
	}
	
	public int getSensortyp() {
		return sensortyp;
	}
};