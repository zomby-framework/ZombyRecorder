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

import java.io.Serializable;
import java.util.Observable;

/**
 * 
 * @author Manuel Schmidt
 *
 */
@SuppressWarnings("serial")
public class ZombyData extends Observable implements Serializable {
	
	private ZombyElement zombyElement;
	private Long timeStamp;
	
	public ZombyData(ZombyElement zombyElement, Long timeStamp) {
		super();
		this.zombyElement = zombyElement;
		this.timeStamp = timeStamp;
	}
	
	public ZombyElement getZombyElement() {
		return zombyElement;
	}
	
	public void setZombyElement(ZombyElement zombyElement) {
		this.zombyElement = zombyElement;
	}
	
	public Long getTimeStamp() {
		return timeStamp;
	}
	
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@Override
	public String toString() {
		return timeStamp + ";" + zombyElement.getStringToFile();
	}
	
	public String getStringToFile() {
		return timeStamp + ";" + zombyElement.getStringToFile();
	}
	
	// observable function
	public void changedZombyData() {
		setChanged();
		notifyObservers();
	}
}
