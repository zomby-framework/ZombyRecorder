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

package de.inovex.android.framework.zomby;


import de.inovex.android.framework.zomby.core.CoreAVD;
import de.inovex.android.framework.zomby.core.CoreCDMA;
import de.inovex.android.framework.zomby.core.CoreEvent;
import de.inovex.android.framework.zomby.core.CoreGSM;
import de.inovex.android.framework.zomby.core.CoreGeo;
import de.inovex.android.framework.zomby.core.CoreNetwork;
import de.inovex.android.framework.zomby.core.CorePower;
import de.inovex.android.framework.zomby.core.CoreSMS;
import de.inovex.android.framework.zomby.core.CoreSensor;
import de.inovex.android.framework.zomby.player.ZombyPlayer;

/**
 * Factory class to get manipulator class instances 
 * @author Manuel Schmidt
 *
 */
public class Zomby {

	private static GSM gsm;
	private static Network network;
	private static CDMA cdma;
	private static Power power;
	private static Sensor sensor;
	private static Geo geo;
	private static Event event;
	private static AVD avd;
	
	private static CoreGSM coreGSM;
	private static CoreNetwork coreNetwork;
	private static CorePower corePower;
	private static CoreGeo coreGeo;
	private static CoreCDMA coreCDMA;
	private static CoreSensor coreSensor;
	private static CoreSMS coreSMS;
	private static CoreEvent coreEvent;
	private static CoreAVD coreAVD;
	
	private static ZombyPlayer zombyPlayer;
	
	public Zomby() {}

	public static CoreGSM getCoreGSM() {
		if(coreGSM == null)
			coreGSM = new CoreGSM();
		
		return coreGSM;
	}
	
	public static GSM getGSM() {
		if(gsm == null)
			gsm = new GSM();
		
		return gsm;
	}
	
	public static CoreNetwork getCoreNetwork() {
		if(coreNetwork == null)
			coreNetwork = new CoreNetwork();
		
		return coreNetwork;
	}

	public static Network getNetwork() {
		if(network == null)
			network = new Network();
		
		return network;
	}
	
	public static Event getEvent() {
		if(event == null)
			event = new Event();
		
		return event;
	}
	
	public static AVD getAVD() {
		if(avd == null)
			avd = new AVD();
		
		return avd;
	}
	
	public static CoreCDMA getCoreCDMA() {
		if(coreCDMA == null)
			coreCDMA = new CoreCDMA();
		
		return coreCDMA;
	}
	
	public static CDMA getCDMA() {
		if(cdma == null)
			cdma = new CDMA();
		
		return cdma;
	}
	
	public static CoreSMS getCoreSMS() {
		if(coreSMS == null)
			coreSMS = new CoreSMS();
		
		return coreSMS;
	}
	
	public static CoreSensor getCoreSensor() {
		if(coreSensor == null)
			coreSensor = new CoreSensor();
		
		return coreSensor;
	}
	
	public static Sensor getSensor() {
		if(sensor == null)
			sensor = new Sensor();
		
		return sensor;
	}
	
	public static CoreGeo getCoreGeo() {
		if(coreGeo == null)
			coreGeo = new CoreGeo();
		
		return coreGeo;
	}
	
	public static Geo getGeo() {
		if(geo == null)
			geo = new Geo();
		
		return geo;
	}
	
	public static CorePower getCorePower() {
		if(corePower == null)
			corePower = new CorePower();
		
		return corePower;
	}
	
	public static Power getPower() {
		if(power == null)
			power = new Power();
		
		return power;
	}
	
	public static CoreEvent getCoreEvent() {
		if(coreEvent == null)
			coreEvent = new CoreEvent();
		
		return coreEvent;
	}
	
	public static CoreAVD getCoreAVD() {
		if(coreAVD == null)
			coreAVD = new CoreAVD();
		
		return coreAVD;
	}
	
	public static ZombyPlayer getZombyPlayer() {
		if(zombyPlayer == null)
			zombyPlayer = new ZombyPlayer();
		
		return zombyPlayer;
	}
}
