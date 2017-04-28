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

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class GeoUtil {

	/**
	 * 
	 * @param startPointLongitude
	 * @param startPointLatitude
	 * @param endPointLongitude
	 * @param endPointLatitude
	 * @return distance between start point and end point in meters
	 */
	public static double calculateDistance(double startLongitude, double startLatitude, 
										   double endLongitude, double endLatitude) {
		double c = 	Math.sin(startLatitude/57.3) * Math.sin(endLatitude/57.3) +
					Math.cos(startLatitude/57.3) * Math.cos(endLatitude/57.3) *
					Math.cos(endLongitude/57.3 - startLongitude/57.3);
		
		double distanceInMiles = 3959 * Math.acos(c);
		double distanceInKm = distanceInMiles * 1.609344;
		
		return distanceInKm * 1000;
	}
}
