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
public class CoreNetwork {
	
	private static final String TAG = "Zomby Instrumentation";
	private String telnetCommand = "";
	
	/**
	 * network speed on the device, where it is one of the following
     *
  	 **/
	public static enum NetworkSpeed {
		/** no limit **/	FULL("full", 0), 
		/** GSM/CSD **/		GSM("gsm", 14400), 
		/** HSCSD **/		HSCSD("hscsd", 43200),
		/** GPRS **/		GPRS("gprs", 80000), 
		/** EDGE/EGPRS **/	EDGE("edge", 236800), 
		/** UMTS/3G **/		UMTS("umts", 1920000), 
		/** HSDPA **/		HSDPA("hsdpa", 14400000); 
		
		private String network;
		private int speed;

		private NetworkSpeed(String network, int speed) {
			this.network = network;
			this.speed = speed;
		}
		
		public String toString() {
			return network;
		}	
		
		public int getValue() {
			return speed;
		}	
	}
	
	/**
	 * latency of the network connection
     *
  	 **/
	public static enum Latency {
		/** no delay **/				NO_DELAY(0), 
		/** set the latecy of 35 **/	UMTS(35), 
		/** set the latecy of 80 **/	EDGE(80),
		/** set the latecy of 150 **/	GPRS(150); 
		
		private double speed;

		private Latency(double speed) {
			this.speed = speed;
		}
		
		public double getValue() {
			return speed;
		}	
	}
	
	/**
	 * allows you to dynamically change the speed of the emulated<br>
	 * network on the device, where <var>speed</var> is one of the following:<br>
     * {@link NetworkSpeed#GSM},
     * {@link NetworkSpeed#HSCSD},
     * {@link NetworkSpeed#HSCSD},
     * {@link NetworkSpeed#GPRS},
     * {@link NetworkSpeed#UMTS},
     * {@link NetworkSpeed#HSDPA},
     * {@link NetworkSpeed#FULL}
	 * @param speed selects both upload and download speed
	 * @throws ZombyException 
	 */
	public void speed(NetworkSpeed speed) throws ZombyException {
		telnetCommand = "network speed " + speed.toString();
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to dynamically change the speed of the emulated<br>
	 * <p>network on the device, where <var>speed</var> is one of the following<br>
     * {@link NetworkSpeed#GSM},
     * {@link NetworkSpeed#HSCSD},
     * {@link NetworkSpeed#HSCSD},
     * {@link NetworkSpeed#GPRS},
     * {@link NetworkSpeed#UMTS},
     * {@link NetworkSpeed#HSDPA},
     * {@link NetworkSpeed#FULL}
	 * @param upSpeed upload speed
	 * @param downSpeed download speed
	 * @throws ZombyException 
	 */
	public void speed(NetworkSpeed upSpeed, NetworkSpeed downSpeed) throws ZombyException {
		telnetCommand = "network speed " + upSpeed.getValue() + ":" + downSpeed.getValue();
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to dynamically change the latency of the emulated network on the device
	 * @param latency
	 * @throws ZombyException 
	 */
	public void delay(Latency latency) throws ZombyException {
		telnetCommand = "network delay " + latency.getValue();
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);	
	}
	
	/**	 
	 * starts a new capture of network packets into a specific {@code file}.<br>
	 * This will stop any capture already in progress.<br>
	 * The capture file can later be analyzed by tools like WireShark.<br>
	 * It uses the libpcap file format.
	 * @param file name of capture file
	 * @throws ZombyException 
	 */
	public void startCapture(String file) throws ZombyException {
		telnetCommand = "cdma capture start " + file;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * stops the capture
	 * @throws ZombyException
	 */
	public void stopCapture() throws ZombyException {
		telnetCommand = "cdma capture stop";
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);		
	}
}
