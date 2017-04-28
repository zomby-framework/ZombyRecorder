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

import de.inovex.android.framework.zomby.util.ZombyException;
import de.inovex.android.framework.zomby.util.ZombyLog;

/**
 * Allows you to manipulate the avd
 * @author Manuel Schmidt
 *
 */
public class AVD {
	
	private static final String TAG = "Zomby AVD Instrumentation";

	/**
	 * Allows you to start the avd
	 * @throws ZombyException
	 */
	public void startAVD() throws ZombyException {
		ZombyLog.logMessage(TAG, "start avd");
		Zomby.getCoreAVD().start();
	}

	/**
	 * Allows you to stop the avd
	 * @throws ZombyException
	 */
	public void stopAVD() throws ZombyException {
		ZombyLog.logMessage(TAG, "stop avd");
		Zomby.getCoreAVD().stop();
	}
}
