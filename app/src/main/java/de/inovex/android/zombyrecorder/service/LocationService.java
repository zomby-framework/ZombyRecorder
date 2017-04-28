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

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class LocationService {

	private static Location location;
		
	public double getLongitude() {
		if(location != null)
			return location.getLongitude();
		else
			return 0;
	}

	public double getLatitude() {
		if(location != null)
			return location.getLatitude();
		else
			return 0;
	}
	
	public static Location getLocation(LocationManager locationManager) {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);
		String bestProvider = locationManager.getBestProvider(criteria, true);
		
		return locationManager.getLastKnownLocation(bestProvider);
		
		/*if(isNetworkAvailable())
			return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		else
			return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);*/
	}

	public boolean isGPSActivate() {
		//if(true)
			//Log.d(getClass().toString() + ".getCurrentPosition","gps is activate");
		//else
			//Log.d(getClass().toString() + ".getCurrentPosition","gps is deactivate");
		// ToDo: implement this method
		return true;
	}

	@Override
	public String toString() {
		return "Latitude " + getLatitude() + ", Longitude " + getLongitude();
	}
}
