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

import java.io.InputStream;
import java.io.StringReader;

import org.xml.sax.InputSource;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class RecorderUtil {
	
	public static InputStream stringToInputStream(String text) {
		return (new InputSource(new StringReader(text))).getByteStream();
	}
}
