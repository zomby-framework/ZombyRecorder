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
public class CoreGeo {
	
	private static final String TAG = "Zomby Instrumentation";
	private String telnetCommand = "";
	
	
	public static enum Sentence {
		GPGGA("$GPGGA"), 
		GPRCM("$GPRCM");
		
		private String sentence;
		
		private Sentence(String sentence) {
			this.sentence = sentence;
		}
		
		public String getValue() {
			return sentence;
		}
	};
	
	
	/**
	 *  Sends a NMEA 0183 sentence to the emulated device, as if it came from an emulated GPS modem
	 * @param sentence only {@link Sentence#GPGGA} and {@link Sentence#GPRCM} sentences are supported at the moment
	 * @throws ZombyException 
	 */
	public void nmea(Sentence sentence, String sentenceParameter) throws ZombyException {
		telnetCommand = "geo nmea " + sentence.getValue() + sentenceParameter;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	} 
	
	/**
	 * allows you to send a simple GPS fix to the emulated system
	 * @param longitude in decimal degrees
	 * @param latitude in decimal degrees
	 * @throws ZombyException 
	 */
	public void fix(double longitude, double latitude) throws ZombyException {
		telnetCommand = "geo fix " + longitude + " " + latitude;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to send a simple GPS fix to the emulated system
	 * @param longitude in decimal degrees
	 * @param latitude in decimal degrees
	 * @param altitude in meters
	 * @throws ZombyException 
	 */
	public void fix(double longitude, double latitude, double altitude) throws ZombyException {
		telnetCommand = "geo fix " + longitude + " " + latitude + " " + altitude;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to send a simple GPS fix to the emulated system
	 * @param longitude in decimal degrees
	 * @param latitude in decimal degrees
	 * @param altitude in meters
	 * @param satellites number of satellites being tracked (1-12)
	 * @throws ZombyException 
	 */
	public void fix(double longitude, double latitude, double altitude, double satellites) throws ZombyException {
		telnetCommand = "geo fix " + longitude + " " + latitude + " " + altitude + " " + satellites;
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
}
