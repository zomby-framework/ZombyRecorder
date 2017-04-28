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

package de.inovex.android.framework.zomby;

import de.inovex.android.framework.zomby.core.CoreSensor.Sensorname;
import de.inovex.android.framework.zomby.util.ZombyException;
import de.inovex.android.framework.zomby.util.ZombyLog;
import de.inovex.android.framework.zomby.util.ZombyTime;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class Sensor {
	
	private static final String TAG = "Zomby Sensor Instrumentation";

	/**
	 * Set the view to portrait mode
	 * @throws ZombyException
	 */
	public void setPortraitMode() throws ZombyException {
		ZombyLog.logMessage(TAG, "set portait mode");
		Zomby.getCoreSensor().set(Sensorname.ACCELERATION, 0, 9);
	}
	
	/**
	 * Set the view to landscape mode
	 * @throws ZombyException
	 */
	public void setLandscapeMode() throws ZombyException {
		ZombyLog.logMessage(TAG, "set landscape mode");
		Zomby.getCoreSensor().set(Sensorname.ACCELERATION, -9, 0);
	}
	
	/**
	 * Set the view to north
	 * @throws ZombyException
	 */
	public void showToNorth() throws ZombyException {
		ZombyLog.logMessage(TAG, "show to north");
		Zomby.getCoreSensor().set(Sensorname.ORIENTATION, 0);
	}
	
	/**
	 * Set the view to north-east
	 * @throws ZombyException
	 */
	public void showToNorthEast() throws ZombyException {
		ZombyLog.logMessage(TAG, "show to north-east");
		Zomby.getCoreSensor().set(Sensorname.ORIENTATION, 45);
	}
	
	/**
	 * Set the view to east
	 * @throws ZombyException
	 */
	public void showToEast() throws ZombyException {
		ZombyLog.logMessage(TAG, "show to east");
		Zomby.getCoreSensor().set(Sensorname.ORIENTATION, 90);
	}
	
	/**
	 * Set the view to south-east
	 * @throws ZombyException
	 */
	public void showToSouthEast() throws ZombyException {
		ZombyLog.logMessage(TAG, "show to south-east");
		Zomby.getCoreSensor().set(Sensorname.ORIENTATION, 135);
	}
	
	/**
	 * Set the view to south
	 * @throws ZombyException
	 */
	public void showToSouth() throws ZombyException {
		ZombyLog.logMessage(TAG, "show to south");
		Zomby.getCoreSensor().set(Sensorname.ORIENTATION, 180);
	}
	
	/**
	 * Set the view to south-west
	 * @throws ZombyException
	 */
	public void showToSouthWest() throws ZombyException {
		ZombyLog.logMessage(TAG, "show to south-west");
		Zomby.getCoreSensor().set(Sensorname.ORIENTATION, 225);
	}
	
	/**
	 * Set the view to west
	 * @throws ZombyException
	 */
	public void showToWest() throws ZombyException {
		ZombyLog.logMessage(TAG, "show to west");
		Zomby.getCoreSensor().set(Sensorname.ORIENTATION, 270);
	}
	
	/**
	 * Set the view to north-west
	 * @throws ZombyException
	 */
	public void showToNorthWest() throws ZombyException {
		ZombyLog.logMessage(TAG, "show to north-west");
		Zomby.getCoreSensor().set(Sensorname.ORIENTATION, 315);
	}
	
	/**
	 * Set the view to north and turns around about 360 degree in <var>milliseconds</var>
	 * Note: method runs in a new thread 
	 * @throws ZombyException
	 */
	public void turnAroundInACircle(final double milliseconds) {
		ZombyLog.logMessage(TAG, "turn around in a circle");
		new Thread(new Runnable() {
			@Override
			public void run() {
				long timeToWait = Math.round(milliseconds / 360);
				for(int degree=0; degree<=360; degree++) {
					synchronized(this) {
						try {
							Zomby.getCoreSensor().set(Sensorname.ORIENTATION, degree);
							wait(timeToWait);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}					
				}
			}
		}).start();
	}
	
	/**
	 * allows you to simulate a temperature drifting from <var>temperatureBegin</var> to <var>temperatureEnd</var> in <var>duration</var><br>
	 * NOTE: method runs in a new thread
	 * @param 
	 * @param milliseconds changing interval
	 * @param speedValues individual field with network speed values
	 */
	public void simulateTemperaturDrifting(final double temperatureBegin, final double temperatureEnd, final double duration) {
		if(duration < 100)
			return;
		
		ZombyLog.logMessage(TAG, "simulate a temperatur drifting");
		new Thread(new Runnable() {
			@Override
			public void run() {				
		
				int timeInterval = 20;
				double countOfIntervals = duration/timeInterval;
				double temperatureDrift = (temperatureEnd - temperatureBegin) / countOfIntervals;
				
				ZombyLog.logMessage(TAG, "countOfIntervals: " + countOfIntervals);
				ZombyLog.logMessage(TAG, "temperatureDrift: " + temperatureDrift);
				
				boolean isIncrease = temperatureBegin < temperatureEnd;
				
				for(double temperature = temperatureBegin; (temperature <= temperatureEnd && isIncrease) | (temperature >= temperatureEnd && !isIncrease); temperature+=temperatureDrift) {
					double value = temperature * 10;
					value = Math.round(value);
					value = value / 10;
					
					long timeToRun = ZombyTime.getTimeStamp();
					try {
						Zomby.getCoreSensor().set(Sensorname.TEMPERATURE, value);
					} catch (ZombyException ze) {
						ze.printStackTrace();
					}
					timeToRun = ZombyTime.getTimeStamp() - timeToRun;
					
					long timeToWait = timeInterval - timeToRun;
					if(timeToWait-10 > 0) {
						synchronized(this){ 
							try {
								wait(timeToWait);
							} catch (InterruptedException ie) {
								ie.printStackTrace();
							}
						} 
					}
				}
			}
		}).start();		
	}
}
