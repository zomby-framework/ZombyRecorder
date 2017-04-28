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

import de.inovex.android.framework.zomby.core.CoreGSM.GsmMode;
import de.inovex.android.framework.zomby.util.ZombyException;
import de.inovex.android.framework.zomby.util.ZombyLog;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class GSM {
	
	private static final String TAG = "Zomby GSM Instrumentation";
	
	/**
	 * allows you to simulate no data connection<br>
	 * @throws ZombyException 
	 * @deprecated This method maybe cause to  a separation of the adb connection (emulator bug)
	 */
	@Deprecated
	public void setNoDataConnection() throws ZombyException {
		ZombyLog.logMessage(TAG, "set no data connection");
		Zomby.getCoreGSM().data(GsmMode.OFF);
	}
	
	/**
	 * allows you to simulate no voice connection<br>
	 * @throws ZombyException 
	 */
	public void setNoVoiceConnection() throws ZombyException {
		ZombyLog.logMessage(TAG, "set no voice connection");
		Zomby.getCoreGSM().voice(GsmMode.OFF);
	}
	
	/**
	 * allows you to simulate no data and voice connection<br>
	 * @throws ZombyException 
	 * @deprecated This method maybe cause to  a separation of the adb connection (emulator bug)
	 */
	@Deprecated
	public void setDeadZone() throws ZombyException {
		ZombyLog.logMessage(TAG, "set no data and voice connection");
		setNoDataConnection();
		setNoVoiceConnection();
	}
}
