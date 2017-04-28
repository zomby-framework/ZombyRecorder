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

package de.inovex.android.zombyrecorder.util;

import de.inovex.android.zombyrecorder.model.ZombyData;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class ZombyDataList extends ObservableList<ZombyData>{

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(Object obj : toArray()){
			ZombyData zombyData = (ZombyData) obj;
			sb.append(zombyData.getStringToFile() + "\n");
		}
		return sb.toString();
	}

}
