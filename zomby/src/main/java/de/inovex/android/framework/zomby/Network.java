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

import java.util.Random;

import de.inovex.android.framework.zomby.core.CoreNetwork.Latency;
import de.inovex.android.framework.zomby.core.CoreNetwork.NetworkSpeed;
import de.inovex.android.framework.zomby.util.ZombyException;
import de.inovex.android.framework.zomby.util.ZombyLog;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class Network {
	
	private static final String TAG = "Zomby Network Instrumentation";
	
	/**
	 * allows you to simulate a slow data connection
	 * @throws ZombyException 
	 */
	public void setSlowDataConnection() throws ZombyException {
		ZombyLog.logMessage(TAG, "set slow data connection");
		Zomby.getCoreNetwork().speed(NetworkSpeed.EDGE);
		Zomby.getCoreNetwork().delay(Latency.GPRS);
	}

	/**
	 * allows you to simulate a fast data connection
	 * @throws ZombyException 
	 */
	public void setFastDataConnection() throws ZombyException {
		ZombyLog.logMessage(TAG, "set slow data connection");
		Zomby.getCoreNetwork().speed(NetworkSpeed.FULL);
		Zomby.getCoreNetwork().delay(Latency.NO_DELAY);
	}

	
	/**
	 * allows you to simulate a changing the data connection <var>countOfChanges</var> times in an interval of <var>milliseconds</var> seconds<br>
	 * (with the complete range of network speed values)<br>
	 * NOTE: method runs in a new thread
	 * @param countOfChanges how often the data connection is be changing
	 * @param milliseconds changing interval
	 */
	public void setChangingNetworkSpeed(int countOfChanges, int milliseconds) {
		NetworkSpeed [] speed = {
				NetworkSpeed.EDGE,
				NetworkSpeed.FULL,
				NetworkSpeed.GPRS,
				NetworkSpeed.GSM,
				NetworkSpeed.UMTS,
				NetworkSpeed.HSDPA,
				NetworkSpeed.HSCSD				
		};
		
		setChangingNetworkSpeed(countOfChanges, milliseconds, speed);
	}
	
	/**
	 * allows you to simulate a changing the data connection <var>countOfChanges</var> times in an interval of <var>milliseconds</var> seconds<br>
	 * NOTE: method runs in a new thread
	 * @param countOfChanges how often the data connection is be changing
	 * @param milliseconds changing interval
	 * @param speedValues individual field with network speed values
	 */
	public void setChangingNetworkSpeed(final int countOfChanges, final int milliseconds, final NetworkSpeed[] speedValues) {
		new Thread(new Runnable() {
			@Override
			public void run() {				
				int oldValue = -1;
				int randomIndex = 0;
				Random generator = new Random(); 

				for(int i=0; i<countOfChanges; i++) {
					do {
						randomIndex = generator.nextInt(speedValues.length);
					}while(oldValue == randomIndex);
					
					oldValue = randomIndex;
					try {
						Zomby.getCoreNetwork().speed(speedValues[randomIndex]);
					} catch (ZombyException ze) {
						ze.printStackTrace();
					}
					
					synchronized(this){ 
						try {
							wait(milliseconds);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
					} 
				}
			}
		}).start();		
	}
}
