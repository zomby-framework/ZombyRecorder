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

package de.inovex.android.framework.zomby.service;

import java.util.ArrayList;
import java.util.List;

//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
import de.inovex.android.framework.zomby.util.ZombyException;
import de.inovex.android.framework.zomby.util.ZombyLog;

/**
 * 
 * @author Manuel Schmidt
 *
 */
public class WebService  {
	
	private final static String HOST = "http://10.0.2.2:8080/ZombyWebservice";
	private final static String TELNET_SERVLET = "/ZombyServlet";
	private final static String TAG = "Zomby Webservice";
	private final static String TELNET_COMMAND_WAS_SUCCESSFUL = "TELNET_COMMAND_WAS_SUCCESSFUL";
	private final static String RESET_TELNET_ANSWER = "";
	
	private String telnetAnswer = RESET_TELNET_ANSWER;
	private String httpAnswer = RESET_TELNET_ANSWER;
	private boolean telnetError = false;
	private boolean httpError = false;
	
	private long timeOut = 6000;
	boolean isTimeOut = false;
	Object obj = new Object();
	Object thisObject = this;
	
	public String sendTelnetCommand(final String telnetCommand) throws ZombyException {
		
		if(telnetCommand == null)
			throw new ZombyException("No telnet command found");	
		
		// time out of webservice
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					synchronized(obj) {
						obj.wait(timeOut);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				synchronized(thisObject) {
					isTimeOut = true;
					thisObject.notify();					
				}
			}
		}).start();
		
		new Thread(new Runnable() {					
			@Override
			public void run() {
				synchronized(this) {
					try {
						wait(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				sendTelnetCommandsAboutHttpPostRequest(telnetCommand + "\n");						
			}
		}).start();
		
		ZombyLog.logMessage(TAG, "Wait for http response");
		synchronized(this) {
			try {				
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
				
		if(isTimeOut)
			throw new ZombyException("Time Out: Webservice just not working");
		
		throwExceptionIfAnErrorOccurred();				
		ZombyLog.logMessage(TAG, "Have received http response");
			
		return telnetAnswer;		
	}
	
	private void throwExceptionIfAnErrorOccurred() throws ZombyException {
		if(httpError) {
			ZombyLog.logErrorMessage(TAG, "It is occurred an http error");
			throw new ZombyException(httpAnswer);
		}
		if(telnetError) {
			ZombyLog.logErrorMessage(TAG, "It is occurred an telnet error");
			throw new ZombyException(telnetAnswer);
		}
	}
	
	private Void sendTelnetCommandsAboutHttpPostRequest(String telnetCommand) {
		
//		// Create a new HttpClient and Post Header
//		HttpClient httpclient = new DefaultHttpClient();
//		HttpPost httppost = new HttpPost(HOST+TELNET_SERVLET);
//		HttpResponse response = null;
//
//		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//		nameValuePairs.add(new BasicNameValuePair("telnet", telnetCommand));
//
//		// Execute HTTP Post Request
//		try {
//			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//			response = httpclient.execute(httppost);
//			telnetAnswer = EntityUtils.toString(response.getEntity());
//
//			if(telnetAnswer.contains(TELNET_COMMAND_WAS_SUCCESSFUL))
//				ZombyLog.logMessage(TAG, "Telnet command '" + telnetCommand.replace("\n", "") + "' was excecuted");
//			else {
//				telnetError = true;
//				ZombyLog.logMessage(TAG, "Telnet command '" + telnetCommand.replace("\n", "") + "' was not excecuted!!!");
//			}
//		} catch (Exception e) {
//			httpError = true;
//			httpAnswer = e.getLocalizedMessage();
//		}
//
//		synchronized(this) {
//			notify();
//		}
//
		return null;
	}
}
