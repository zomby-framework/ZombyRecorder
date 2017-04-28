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
public class CoreSMS {
	
	private static final String TAG = "Zomby Instrumentation";
	private String telnetCommand = "";
	
	/**
	 * allows you to simulate a new inbound sms message
	 * @param phonenumber
	 * @param text
	 * @throws ZombyException 
	 */
	public void send(String phonenumber, String message) throws ZombyException {
		telnetCommand = "sms send " + phonenumber + " " + message;			
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
	
	/**
	 * allows you to simulate a new inbound sms PDU<br>
	 * (used internally when one emulator sends SMS messages to another instance)<br>
	 * you probably don't want to play with this at all
	 * @param hexstring
	 * @throws ZombyException 
	 */
	public void pdu(String hexstring) throws ZombyException {
		telnetCommand = "sms send " + hexstring;		
		ZombyLog.logTelnetCommand(TAG, telnetCommand);
		new WebService().sendTelnetCommand(telnetCommand);
	}
}
