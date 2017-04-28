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

package de.inovex.android.zombyrecorder.receiver;

import de.inovex.android.zombyrecorder.model.NetworkData;
import de.inovex.android.zombyrecorder.model.ZombyData;
import de.inovex.android.zombyrecorder.util.ZombyDataList;
import de.inovex.android.zombyrecorder.util.ZombyTime;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
	
	private final String DISCONNECTED = "DISCONNECTED";

	private ZombyDataList zombyDataList;
	private String oldSubTypeName;
	private String oldState;
	
	public NetworkChangeReceiver(ZombyDataList zombyDataList) {
		this.zombyDataList = zombyDataList;
		this.oldSubTypeName = "_";
		this.oldState = "_";
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		//NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobilInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		if (activeNetInfo != null) {
			String state = activeNetInfo.getState().toString();
			if(!state.contains(oldState)) 
			{
				zombyDataList.add(new ZombyData(new NetworkData(state), ZombyTime.getTimeStamp()));			
				oldState = state;
			}
		}
		else {
			if(oldState != DISCONNECTED)
			zombyDataList.add(new ZombyData(new NetworkData(DISCONNECTED), ZombyTime.getTimeStamp()));
			oldState = DISCONNECTED;
		}
		
		/*if (wifiInfo != null) {
			if(wifiInfo.isConnected()) {
				String subTypename = wifiInfo.getTypeName();
				if(!subTypename.contains(oldSubTypeName)) 
				{
					zombyDataList.add(new ZombyData(new NetworkData(subTypename), ZombyTime.getTimeStamp()));			
					oldSubTypeName = subTypename;
				}
			}
		}*/
		
		if (mobilInfo != null) {
			if(mobilInfo.isConnected()) {
				String subTypename = mobilInfo.getSubtypeName();
				if(!subTypename.contains(oldSubTypeName)) 
				{
					zombyDataList.add(new ZombyData(new NetworkData(subTypename), ZombyTime.getTimeStamp()));			
					oldSubTypeName = subTypename;
				}
			}
		}
	}
}
