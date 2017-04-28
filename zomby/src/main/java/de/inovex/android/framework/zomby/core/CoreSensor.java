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

package de.inovex.android.framework.zomby.core;

import de.inovex.android.framework.zomby.service.WebService;
import de.inovex.android.framework.zomby.util.ZombyException;
import de.inovex.android.framework.zomby.util.ZombyLog;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class CoreSensor {

	private static final String TAG = "Zomby Instrumentation";
	private String telnetCommand = "";

	/**
	 * name of sensor
	 */
	public static enum Sensorname{
		ACCELERATION("acceleration"),
		MAGNETIC_FIELD("magnetic-field"), 
		ORIENTATION("orientation"), 
		TEMPERATURE("temperature"), 
		PROXIMITY("proximity");
	
		private String sensorname;

		private Sensorname(String sensorname) {
			this.sensorname = sensorname;
		}
		
		public String getValue() {
			return sensorname;
		}
	}
	
	/**
	 * set the values of a given sensor, this are 
	 * {@link Sensorname#ACCELERATION},
	 * {@link Sensorname#MAGNETIC_FIELD},
	 * {@link Sensorname#ORIENTATION},
	 * {@link Sensorname#TEMPERATURE},
	 * {@link Sensorname#PROXIMITY}
	 * @param sensor name of sensor
	 * @param value
	 * @throws ZombyException 
	 */
	public void set(Sensorname sensor, double value) throws ZombyException {
		telnetCommand = "sensor set " + sensor.getValue() + " " + value;		
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * set the values of a given sensor, this are 
	 * {@link Sensorname#ACCELERATION},
	 * {@link Sensorname#MAGNETIC_FIELD},
	 * {@link Sensorname#ORIENTATION},
	 * {@link Sensorname#TEMPERATURE},
	 * {@link Sensorname#PROXIMITY}
	 * @param sensor name of sensor
	 * @param firstValue
	 * @param secondValue
	 * @throws ZombyException 
	 */
	public void set(Sensorname sensor, double firstValue, double secondValue) throws ZombyException {
		telnetCommand = "sensor set " + sensor.getValue() + " " + firstValue + ":" + secondValue;		
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * set the values of a given sensor, this are 
	 * {@link Sensorname#ACCELERATION},
	 * {@link Sensorname#MAGNETIC_FIELD},
	 * {@link Sensorname#ORIENTATION},
	 * {@link Sensorname#TEMPERATURE},
	 * {@link Sensorname#PROXIMITY}
	 * @param sensor name of sensor
	 * @param firstValue
	 * @param secondValue
	 * @param thirdValue
	 * @throws ZombyException 
	 */
	public void set(Sensorname sensor, double firstValue, double secondValue, double thirdValue) throws ZombyException {
		telnetCommand = "sensor set " + sensor.getValue() + " " + firstValue + ":" + secondValue + ":" + thirdValue;		
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
}
