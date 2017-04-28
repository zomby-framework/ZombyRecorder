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

import android.os.BatteryManager;
import de.inovex.android.framework.zomby.service.WebService;
import de.inovex.android.framework.zomby.util.ZombyException;
import de.inovex.android.framework.zomby.util.ZombyLog;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class CorePower {
	private static final String TAG = "Zomby Instrumentation";
	private String telnetCommand = "";

	/**
	 * battery status
	 * 
	 */
	public static enum BatteryStatus{
		UNKNOWN("unknown"),
		CHARGING("charging"), 
		DISCHARGING("discharging"), 
		NOT_CHARGING("not-charging"), 
		FULL("full");
	
		private String batteryStatus;

		private BatteryStatus(String batteryStatus) {
			this.batteryStatus = batteryStatus;
		}
		
		public String getValue() {
			return batteryStatus;
		}
		
		public static BatteryStatus getBatteryStatus(int batteryStatus) {
			switch(batteryStatus) {
			case BatteryManager.BATTERY_STATUS_CHARGING:		return CHARGING;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:		return DISCHARGING;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:	return NOT_CHARGING;
			case BatteryManager.BATTERY_STATUS_FULL:			return FULL;
			case BatteryManager.BATTERY_STATUS_UNKNOWN:			return UNKNOWN;
			default:											return UNKNOWN;
			}
		}
	}
	
	/**
	 * battery health state
	 *
	 */
	public static enum BatteryHealthState{
		UNKNOWN("unknown"),
		GOOD("good"), 
		OVERHEAT("overheat"), 
		DEAD("dead"), 
		OVERVOLTAGE("overvoltage"), 
		FAILURE("failure");
	
		private String batteryHealthState;

		private BatteryHealthState(String batteryHealthState) {
			this.batteryHealthState = batteryHealthState;
		}
		
		public String getValue() {
			return batteryHealthState;
		}
	}
	
	
	/**
	 * allows you to set the AC charging state to on
	 * @param chargingState true for on and false for off
	 * @throws ZombyException 
	 */
	public void ac(boolean chargingState) throws ZombyException {
		if(chargingState) {
			telnetCommand = "power ac on";
			ZombyLog.logTelnetCommand(TAG, telnetCommand);
			new WebService().sendTelnetCommand(telnetCommand);
		}
		else {
			telnetCommand = "power ac off";
			ZombyLog.logTelnetCommand(TAG, telnetCommand);
			new WebService().sendTelnetCommand(telnetCommand);
		}
	}
	
	/**
	 * allows you to set battery status<br>
	 * valid values for are the following:
	 * {@link BatteryStatus#UNKNOWN},
	 * {@link BatteryStatus#CHARGING},
	 * {@link BatteryStatus#DISCHARGING},
	 * {@link BatteryStatus#NOT_CHARGING},
	 * {@link BatteryStatus#FULL}
	 * @param batteryStatus
	 * @throws ZombyException 
	 */
	public void status(BatteryStatus batteryStatus) throws ZombyException {
		telnetCommand = "power status " + batteryStatus.getValue();
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to set battery present state to true or false
	 * @param batteryPresentState
	 * @throws ZombyException 
	 */
	public void present(boolean batteryPresentState) throws ZombyException {
		telnetCommand = "power present " + batteryPresentState;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to set battery health state<br>
	 * valid values for are the following:
	 * {@link BatteryHealthState#UNKNOWN},
	 * {@link BatteryHealthState#GOOD},
	 * {@link BatteryHealthState#OVERHEAT},
	 * {@link BatteryHealthState#DEAD},
	 * {@link BatteryHealthState#OVERVOLTAGE},
	 * {@link BatteryHealthState#FAILURE}
	 * @param batteryHealthState
	 * @throws ZombyException 
	 */
	public void health(BatteryHealthState batteryHealthState) throws ZombyException {
		telnetCommand = "power health " + batteryHealthState;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to set battery capacity 
	 * @param percentage value 0-100
	 * @throws ZombyException 
	 */
	public void capacity(int percentage) throws ZombyException {
		telnetCommand = "power capacity " + percentage;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
}
