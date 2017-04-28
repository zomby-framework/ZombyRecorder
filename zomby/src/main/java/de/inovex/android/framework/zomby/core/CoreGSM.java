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
public class CoreGSM {
	
	private static final String TAG = "Zomby Instrumentation";
	private String telnetCommand = "";

	public static enum GsmMode {
        UNREGISTERED("unregistered"),
        HOME("home"),
        ROAMING("roaming"),
        SEARCHING("searching"),
        DENIED("denied"),
        OFF("off"),
        ON("on");

        private final String gsmMode;

        GsmMode(String gsmMode) {
        	this.gsmMode = gsmMode;
        }

        public String getValue() {
            return gsmMode;
        }
    }
	
	
	/**
	 * allows you to simulate a new inbound call<br>
	 * IMPORTANT: this method caused to stop the test program
	 * @param phonenumber
	 * @throws ZombyException 
	 * @deprecated This method maybe cause to  a separation of the adb connection (emulator bug)
	 */
	@Deprecated
	public void call(String phonenumber) throws ZombyException {
		telnetCommand = "gsm call " + phonenumber;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * closes an outbound call, reporting the remote phone as busy<br>
	 * only possible if the call is 'waiting'
	 * @param remoteNumber
	 * @throws ZombyException 
	 */
	public void busy(String remoteNumber) throws ZombyException {
		telnetCommand = "gsm busy " + remoteNumber;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * change the state of a call to 'held'<br>
	 * this is only possible if the call in the 'waiting' or 'active' state
	 * @param remoteNumber
	 * @throws ZombyException 
	 */
	public void hold(String remoteNumber) throws ZombyException {
		telnetCommand = "gsm hold " + remoteNumber;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * change the state of a call to 'active'<br>
	 * this is only possible if the call is in the 'waiting' or 'held' state
	 * @param remoteNumber
	 * @throws ZombyException 
	 */
	public void accept(String remoteNumber) throws ZombyException {
		telnetCommand = "gsm accept " + remoteNumber;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to simulate the end of an inbound or outbound call
	 * @param phonenumber
	 * @throws ZombyException 
	 */
	public void cancel(String phonenumber) throws ZombyException {
		telnetCommand = "gsm busy " + phonenumber;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to change the state of your GPRS connection<br>
	 * valid values for <var>mode</var> are the following:<br>
	 * {@link GsmMode#UNREGISTERED},
	 * {@link GsmMode#HOME},
	 * {@link GsmMode#ROAMING},
	 * {@link GsmMode#SEARCHING},
	 * {@link GsmMode#DENIED},
	 * {@link GsmMode#OFF},
	 * {@link GsmMode#ON}<br>
  	 * IMPORTANT: 
  	 * @param state
	 * @throws ZombyException
	 * @deprecated The use from {@code unregistered}, {@code searching}, 
  	 * {@code denied}, {@code off} can cause to  a separation of the adb connection (emulator bug) 
	 */
	public void data(GsmMode mode) throws ZombyException {
		telnetCommand = "gsm data " + mode.getValue();
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to change the state of your GPRS connection<br>
	 * valid values for <var>mode</var> are the following:<br>
	 * {@link GsmMode#UNREGISTERED},
	 * {@link GsmMode#HOME},
	 * {@link GsmMode#ROAMING},
	 * {@link GsmMode#SEARCHING},
	 * {@link GsmMode#DENIED},
	 * {@link GsmMode#OFF},
	 * {@link GsmMode#ON}
	 * @param state
	 * @throws ZombyException 
	 */
	public void voice(GsmMode mode) throws ZombyException {
		telnetCommand = "gsm voice " + mode.getValue();
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);		
	}
	
	/**
	 * changes the reported strength and error rate on next (15s) update
	 * @param rssi range is 0..31 and 99 for unknown
	 * @throws ZombyException 
	 */
	public void signal(int rssi) throws ZombyException {
		telnetCommand = "gsm signal " + rssi;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * changes the reported strength and error rate on next (15s) update
	 * @param rssi range is 0..31 and 99 for unknown
	 * @param ber range is 0..7 percent and 99 for unknown
	 * @throws ZombyException 
	 */
	public void signal(int rssi, int ber) throws ZombyException {
		telnetCommand = "gsm signal " + rssi + " " + ber;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
}
