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

import de.inovex.android.framework.zomby.core.CorePower.BatteryStatus;
import de.inovex.android.framework.zomby.util.ZombyException;
import de.inovex.android.framework.zomby.util.ZombyLog;
import de.inovex.android.framework.zomby.util.ZombyTime;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class Power {
	
	private static final String TAG = "Zomby Power Instrumentation";
	
	/**
	 * allows you to set battery capacity in a critical area
	 * @throws ZombyException 
	 */
	public void setCriticalPower() throws ZombyException {
		ZombyLog.logMessage(TAG, "set critical power");
		Zomby.getCorePower().capacity(3);
	}
	
	/**
	 * allows you to set battery capacity in an uncritical area
	 * @throws ZombyException 
	 */
	public void setUncriticalPower() throws ZombyException {
		ZombyLog.logMessage(TAG, "set uncritical power");
		Zomby.getCorePower().capacity(30);
	}
	
	/**
	 * allows you to set battery capacity on 50 percent
	 * @throws ZombyException 
	 */
	public void setHalfPower() throws ZombyException {
		ZombyLog.logMessage(TAG, "set half power");
		Zomby.getCorePower().capacity(50);
	}
	
	/**
	 * allows you to set battery capacity on 100 percent
	 * @throws ZombyException 
	 */
	public void setFullPower() throws ZombyException {
		ZombyLog.logMessage(TAG, "set full power");
		Zomby.getCorePower().capacity(100);
	}
	
	/**
	 * allows you to set battery status on charging
	 * @throws ZombyException 
	 */
	public void batteryIsCharging() throws ZombyException {
		ZombyLog.logMessage(TAG, "set battery status on charging");
		Zomby.getCorePower().status(BatteryStatus.CHARGING);
	}
	
	/**
	 * allows you to set battery status on discharging
	 * @throws ZombyException 
	 */
	public void batteryIsDischarging() throws ZombyException {
		ZombyLog.logMessage(TAG, "set battery status on discharging");
		Zomby.getCorePower().status(BatteryStatus.DISCHARGING);
	}
	
	/**
	 * allows you to set battery status on full
	 * @throws ZombyException 
	 */
	public void batteryIsFull() throws ZombyException {
		ZombyLog.logMessage(TAG, "set battery status on full");
		Zomby.getCorePower().status(BatteryStatus.FULL);
	}
	
	/**
	 * allows you to set battery status on not charging
	 * @throws ZombyException 
	 */
	public void batteryIsNotCharging() throws ZombyException {
		ZombyLog.logMessage(TAG, "set battery status on not charging");
		Zomby.getCorePower().status(BatteryStatus.NOT_CHARGING);
	}
	
	/**
	 * allows you to simulate a whole battery life in <var>milliseconds</var><br>
	 * NOTE: method runs in a new thread
	 * @param milliseconds
	 * @throws ZombyException 
	 */
	public void simulateWholeBatteryLife(final int milliseconds) throws ZombyException {
		simulateBatteryLife(100, 1, milliseconds);
	}

	/**
	 * allows you to simulate the battery life from <var>startCapacity</var> to <var>endCapacity</var> in <var>milliseconds</var><br>
	 * NOTE: method runs in a new thread
	 * @param startCapacity valid range is 100-3
	 * @param endCapacity valid range is 99-1
	 * @param milliseconds minimum 500ms
	 * @throws ZombyException 
	 */
	public void simulateBatteryLife(final int startCapacity, final int endCapacity, final int milliseconds) throws ZombyException {
		if(startCapacity > 100 | startCapacity < 3)
			throw new ZombyException("invalid start capacity value");
		if(endCapacity > 99 | endCapacity < 1)
			throw new ZombyException("invalid end capacity value");
		if(startCapacity < endCapacity)
			throw new ZombyException("endCapacity is greater than startCapacity");
		if(milliseconds < 500)
			throw new ZombyException("milliseconds to low");
		
		Zomby.getCorePower().status(BatteryStatus.DISCHARGING);
		new Thread(new Runnable() {
			@Override
			public void run() {
				int totalConsumption = startCapacity - endCapacity;
				
				for(int batteryCapacity=startCapacity; batteryCapacity>=endCapacity; batteryCapacity--) {
					try {
						synchronized(this) {
							long start = ZombyTime.getTimeStamp();
							Zomby.getCorePower().capacity(batteryCapacity);
							long excecuteTime = ZombyTime.getTimeStamp() - start;
							
							wait((milliseconds / totalConsumption) - excecuteTime);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}				
			}			
		}).start();
	}
	
	/**
	 * allows you to simulate a whole battery life in <var>milliseconds</var><br>
	 * NOTE: method runs in a new thread
	 * @param milliseconds
	 * @throws ZombyException 
	 */
	public void simulateWholeBatteryLoading(final int milliseconds) throws ZombyException {
		simulateBatteryLoading(1, 100, milliseconds);
		Zomby.getCorePower().status(BatteryStatus.FULL);
	}
	
	/**
	 * allows you to simulate the battery loading from <var>startCapacity</var> to <var>endCapacity</var> in <var>milliseconds</var><br>
	 * NOTE: method runs in a new thread
	 * @param startCapacity valid range is 1-99
	 * @param endCapacity valid range is 2-100
	 * @param milliseconds minimum 100ms
	 * @throws ZombyException 
	 */
	public void simulateBatteryLoading(final int startCapacity, final int endCapacity, final int milliseconds) throws ZombyException {
		if(startCapacity > 99 | startCapacity < 0)
			throw new ZombyException("invalid start capacity value");
		if(endCapacity > 100 | endCapacity < 2)
			throw new ZombyException("invalid end capacity value");
		if(startCapacity > endCapacity)
			throw new ZombyException("startCapacity is greater than endCapacity");
		if(milliseconds < 100)
			throw new ZombyException("milliseconds to low");
		
		Zomby.getCorePower().status(BatteryStatus.CHARGING);
		new Thread(new Runnable() {
			@Override
			public void run() {
				int totalLoading = endCapacity - startCapacity;
				
				for(int batteryCapacity=startCapacity; batteryCapacity>=endCapacity; batteryCapacity++) {
					try {
						synchronized(this) {
							long start = ZombyTime.getTimeStamp();
							Zomby.getCorePower().capacity(batteryCapacity);
							long excecuteTime = ZombyTime.getTimeStamp() - start;
							
							wait((milliseconds / totalLoading) - excecuteTime);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}					
				}				
			}			
		}).start();
	}
}
