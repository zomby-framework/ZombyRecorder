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

package de.inovex.android.zombyrecorder.listener;

import java.util.List;

import de.inovex.android.zombyrecorder.model.LocationData;
import de.inovex.android.zombyrecorder.model.ZombyData;
import de.inovex.android.zombyrecorder.util.ZombyTime;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class GPSListener implements LocationListener {

	private final String TAG = "LocationSensorListener";
	private List<ZombyData> zombyDataList;
	
	public GPSListener(List<ZombyData> zombyDataList) {
		this.zombyDataList = zombyDataList;
	}

	@Override
	public void onLocationChanged(Location location) {
		double longitude = location.getLongitude();
		double latitude = location.getLatitude();
		LocationData geoData = new LocationData(longitude, latitude);
		
		zombyDataList.add(new ZombyData(geoData, ZombyTime.getTimeStamp()));
		Log.d(TAG , geoData.toString());		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
