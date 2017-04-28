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

import de.inovex.android.framework.zomby.util.GeoUtil;
import de.inovex.android.framework.zomby.util.ZombyException;
import de.inovex.android.framework.zomby.util.ZombyLog;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class Geo {
	
	private static final String TAG = "Zomby Geo Instrumentation";
	
	/**
	 * simulate a walking pedestrian with 6 km/h who goes from start- to end point<br>
	 * NOTE: method runs in a new thread
	 * @param startLongitude
	 * @param startLatitude
	 * @param endPointLongitude
	 * @param endPointLatitude
	 */
	public void simulateWalkingPedestrian(final double startPointLongitude, final double startPointLatitude, 
							   final double endPointLongitude, final double endPointLatitude) {
		ZombyLog.logMessage(TAG, "simulate a going walker");
		simulateMovingObject(startPointLongitude, startPointLatitude, endPointLongitude, endPointLatitude, 6);
	}

	/**
	 * simulate a running pedestrian with 15 km/h who goes from start- to end point<br>
	 * NOTE: method runs in a new thread
	 * @param startLongitude
	 * @param startLatitude
	 * @param endPointLongitude
	 * @param endPointLatitude
	 */
	public void simulateRunningPedestrian(final double startPointLongitude, final double startPointLatitude, 
			final double endPointLongitude, final double endPointLatitude) {
		ZombyLog.logMessage(TAG, "simulate a running man");
		simulateMovingObject(startPointLongitude, startPointLatitude, endPointLongitude, endPointLatitude, 15);
	}
	
	/**
	 * simulate a concorde with 2.405 km/h (Mach 2,23) which fly from start- to end point<br>
	 * NOTE: method runs in a new thread
	 * @param startLongitude
	 * @param startLatitude
	 * @param endPointLongitude
	 * @param endPointLatitude
	 */
	public void simulateFlyingConcorde(final double startPointLongitude, final double startPointLatitude, 
			final double endPointLongitude, final double endPointLatitude) {
		ZombyLog.logMessage(TAG, "simulate a flying concorde");
		simulateMovingObject(startPointLongitude, startPointLatitude, endPointLongitude, endPointLatitude, 2405);
	}
	
	/**
	 * simulate a moving object with <par>speedOfObject</par> km/h  from start- to end point<br>
	 * NOTE: method runs in a new thread
	 * @param startPointLongitude
	 * @param startPointLatitude
	 * @param endPointLongitude
	 * @param endPointLatitude
	 * @param speedOfObject
	 */
	public void simulateMovingObject(final double startPointLongitude, final double startPointLatitude, 
									   final double endPointLongitude, final double endPointLatitude, 
									   final int speedOfObject) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				double distance = GeoUtil.calculateDistance(startPointLongitude, startPointLatitude, endPointLongitude, endPointLatitude);
				double distanceInKm = distance / 1000;
				double timeForWalkInHours = distanceInKm / speedOfObject;
				double timeForWalkInMinutes = timeForWalkInHours * 60;
				double timeForWalkInSeconds = timeForWalkInMinutes * 60;
				
				ZombyLog.logMessage(TAG, "distance: " + distance + " meters");
				ZombyLog.logMessage(TAG, "time for distance: " + timeForWalkInSeconds + " seconds");
				
				double diffLongitude = endPointLongitude - startPointLongitude;
				double diffLatitude = endPointLatitude - startPointLatitude;
				
				double longitude = startPointLongitude; 
				double latitude = startPointLatitude; 
				
				double pauseInSeconds = 0.1;
				int countOfSteps = (int)(timeForWalkInSeconds / pauseInSeconds);
				
				ZombyLog.logMessage(TAG, "start of a running object simulation");
				for(int step=1; step <= countOfSteps; step++) {
					synchronized(this) {
						try {
							Zomby.getCoreGeo().fix(longitude, latitude);
							wait((long)(pauseInSeconds*1000));
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ZombyException e) {
							e.printStackTrace();
						}
					}
					longitude += diffLongitude / countOfSteps;
					latitude += diffLatitude / countOfSteps;
				}					
				ZombyLog.logMessage(TAG, "end of a running object simulation");
			}
		}).start();
	}
}
