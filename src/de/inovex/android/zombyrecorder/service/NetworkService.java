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

package de.inovex.android.zombyrecorder.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class NetworkService extends Service {
	
	private boolean networkAvailable;
	
	public boolean isNetworkAvailable() {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo Info = connectivity.getActiveNetworkInfo();
			if (Info == null) {
				networkAvailable = false;
			}
			if (Info.getState() == NetworkInfo.State.CONNECTED)
				networkAvailable = true;
		} catch (Exception ex) {
			networkAvailable = false;
		}
		
		return networkAvailable;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
