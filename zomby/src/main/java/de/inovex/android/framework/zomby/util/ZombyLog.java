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

package de.inovex.android.framework.zomby.util;

import android.util.Log;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class ZombyLog {
	
	public static void logTelnetCommand(String TAG, String telnetCommand) {
		String logMessage = "set " + telnetCommand;
		logMessage(TAG, logMessage);
	}

	public static void logMessage(String TAG, String logMessage) {
		Log.d(TAG, logMessage);
		System.out.println(logMessage);
	}

	public static void logErrorMessage(String TAG, String logMessage) {
		Log.e(TAG, logMessage);
		System.out.println(logMessage);
	}
}
