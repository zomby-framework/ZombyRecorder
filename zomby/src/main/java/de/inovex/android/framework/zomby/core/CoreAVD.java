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
 * allows you to control (e.g. start/stop) the execution of the virtual device
 * @author Manuel Schmidt
 *
 */
public class CoreAVD {
	
	private static final String TAG = "Zomby Instrumentation";
	private String telnetCommand = "";
	
	/**
	 * allows you to stop the virtual device
	 * @throws ZombyException 
	 */
	public void stop() throws ZombyException {
		telnetCommand = "avd stop";			
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to start/restart the virtual device
	 * @throws ZombyException 
	 */
	public void start() throws ZombyException {
		telnetCommand = "avd start";			
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to save snapshot commands
	 * @throws ZombyException 
	 */
	public void  saveSnapshot(String filename) throws ZombyException {
		telnetCommand = "avd snapshot save " + filename;			
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to save snapshot commands
	 * @throws ZombyException 
	 */
	public void  loadSnapshot(String filename) throws ZombyException {
		telnetCommand = "avd snapshot load " + filename;			
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to save snapshot commands
	 * @throws ZombyException 
	 */
	public void  deleteSnapshot(String filename) throws ZombyException {
		telnetCommand = "avd snapshot del " + filename;			
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
}
