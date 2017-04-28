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

package de.inovex.android.zombyrecorder.model;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class SensorData {
	private float x;
	private float y;
	private float z;
	
	public SensorData(float x) {
		this.x = x;
	}
	
	public SensorData(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public SensorData(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	@Override
	public String toString() {
		return x + "," + y + "," + z;
	}
}
