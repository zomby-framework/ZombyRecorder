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

package de.inovex.android.zombyrecorder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import de.inovex.android.framework.zomby.core.CorePower.BatteryStatus;
import de.inovex.android.framework.zomby.player.ZombyPlayer.DataElement;
import de.inovex.android.framework.zomby.util.ZombyException;
import de.inovex.android.zombyrecorder.listener.GPSListener;
import de.inovex.android.zombyrecorder.listener.SensorListener;
import de.inovex.android.zombyrecorder.model.SensorProperty;
import de.inovex.android.zombyrecorder.model.ZombyData;
import de.inovex.android.zombyrecorder.receiver.BatteryChangeReceiver;
import de.inovex.android.zombyrecorder.receiver.NetworkChangeReceiver;
import de.inovex.android.zombyrecorder.util.ZombyDataList;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

/**
 *
 * @author Manuel Schmidt
 *
 */
public class ZombyRecorderActivity extends Activity implements OnClickListener, Observer {
	//permissions
	private static final String[] INITIAL_PERMS={
			Manifest.permission.ACCESS_FINE_LOCATION,

	};

	public static final int DIALOG_SAVE_PROGRESS = 0;
	private static final String TAG = "ZombyRecorderActivity";
	public static final String ZOMBY_POSTFIX = ".zf";
	private SensorManager sensorManager;
	private LocationManager locationManager;

	private ImageButton recButton;
	private ImageButton stopButton;
	private ImageButton pauseButton;
	private ImageButton playButton;
	private TextView textView;
	private Sensor[] propertySensor;
	private CheckBox[] propertyCheckBox;

	private boolean[] isPropertyCheckBoxChecked;

	private SensorListener sensorListener;
	private GPSListener gpsListener;

	private BatteryChangeReceiver batteryChangeReceiver;
	private NetworkChangeReceiver networkChangeReceiver;

	private ZombyDataList zombyDataList;
	private boolean isRecording;
	private boolean isGPSEnabled;
	private boolean isNetworkEnabled;

	private ProgressDialog progressDialog;

	protected String filename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		textView = (TextView) findViewById(R.id.text_view);
		textView.setMovementMethod(new ScrollingMovementMethod());

		recButton = (ImageButton) findViewById(R.id.rec_button);
		stopButton = (ImageButton) findViewById(R.id.stop_button);
		pauseButton = (ImageButton) findViewById(R.id.pause_button);
		playButton = (ImageButton) findViewById(R.id.play_button);

		recButton.setOnClickListener(this);
		stopButton.setOnClickListener(this);
		pauseButton.setOnClickListener(this);
		playButton.setOnClickListener(this);

		propertySensor = new Sensor[SensorProperty.values().length];
		propertyCheckBox = new CheckBox[SensorProperty.values().length];
		isPropertyCheckBoxChecked = new boolean[SensorProperty.values().length];

		for (SensorProperty property : SensorProperty.values()) {
			propertyCheckBox[property.getIndex()] = (CheckBox) findViewById(property.getId());
		}

		// fetch sensor managers
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == recButton.getId()) {
			zombyDataList = new ZombyDataList();
			zombyDataList.addObserver(this);

			Log.d("TAG", "recorder is started");

			// init BroadcastReceiver
			batteryChangeReceiver = new BatteryChangeReceiver(zombyDataList);
			networkChangeReceiver = new NetworkChangeReceiver(zombyDataList);

			// init listener
			sensorListener = new SensorListener(zombyDataList);
			gpsListener = new GPSListener(zombyDataList);

			registerSelectedProperties();

			setAllCheckBoxes(View.GONE);
			textView.setVisibility(View.VISIBLE);

			recButton.setVisibility(View.GONE);
			pauseButton.setVisibility(View.VISIBLE);
			stopButton.setVisibility(View.VISIBLE);

			isRecording = true;
		} else if (view.getId() == pauseButton.getId()) {

			if (isRecording) {
				Log.d("TAG", "recorder is paused");
				unregisterSelectedProperties();
				isRecording = false;
				playButton.setVisibility(View.VISIBLE);
				pauseButton.setBackgroundResource(R.drawable.play);
			} else {
				Log.d("TAG", "recorder is continued");
				registerSelectedProperties();
				isRecording = true;
				playButton.setVisibility(View.GONE);
				pauseButton.setVisibility(View.VISIBLE);
				pauseButton.setBackgroundResource(R.drawable.pause);
			}
		} else if (view.getId() == stopButton.getId()) {
			Log.d("TAG", "recorder is stoped");

			if (isRecording)
				unregisterSelectedProperties();

			// save generated data in file
			saveDialog();

			isRecording = false;
		}
	}

	private void setAllCheckBoxes(int attribute) {
		for (SensorProperty property : SensorProperty.values()) {
			CheckBox checkbox = propertyCheckBox[property.getIndex()];
			checkbox.setVisibility(attribute);
		}
	}

	private void registerSelectedProperties() {
		for (SensorProperty property : SensorProperty.values()) {
			int index = property.getIndex();
			CheckBox checkbox = propertyCheckBox[index];

			if (isPropertyCheckBoxChecked[index] = checkbox.isChecked()) {
				switch (property) {
					// register listener
					case ACCELEROMETER:
						propertySensor[index] = sensorManager.getDefaultSensor(property.getSensortyp());
						sensorManager.registerListener(sensorListener, propertySensor[index], SensorManager.SENSOR_DELAY_UI);
						break;

					case MAGNETIC_FIELD:
						propertySensor[index] = sensorManager.getDefaultSensor(property.getSensortyp());
						sensorManager.registerListener(sensorListener, propertySensor[index], SensorManager.SENSOR_DELAY_NORMAL);
						break;

					case ORIENTATION:
						propertySensor[index] = sensorManager.getDefaultSensor(property.getSensortyp());
						sensorManager.registerListener(sensorListener, propertySensor[index], SensorManager.SENSOR_DELAY_NORMAL);
						break;

					case PROXIMITY:
						propertySensor[index] = sensorManager.getDefaultSensor(property.getSensortyp());
						sensorManager.registerListener(sensorListener, propertySensor[index], SensorManager.SENSOR_DELAY_NORMAL);
						break;

					case TEMPERATURE:
						propertySensor[index] = sensorManager.getDefaultSensor(property.getSensortyp());
						sensorManager.registerListener(sensorListener, propertySensor[index], SensorManager.SENSOR_DELAY_NORMAL);
						break;

					// register broadcast receiver
					case BATTERY:
						registerReceiver(batteryChangeReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
						break;

					case NETWORK:
						registerReceiver(networkChangeReceiver, new IntentFilter("android.intent.action.ANY_DATA_STATE"));
						break;

					// request location updates
					case LOCATION:
						int minTime = 0;
						int minDistance = 10;

						locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

						// getting GPS status
						isGPSEnabled = locationManager
								.isProviderEnabled(LocationManager.GPS_PROVIDER);

						// getting network status
						isNetworkEnabled = locationManager
								.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

						if (!isGPSEnabled && !isNetworkEnabled) {
							// no network provider is enabled
						} else {
							if (isNetworkEnabled) {
								locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, gpsListener);
								Log.d("Network", "Network Enabled");
			            }
			            if (isGPSEnabled) {
							locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
							Log.d("GPS", "GPS Enabled");
			            }
			        }
					break;
				}
				Log.d("TAG", "Property " + property + " is registed");
			}
		}
	}
	
	private void unregisterSelectedProperties() {
		for(SensorProperty property : SensorProperty.values()) {
			if(isPropertyCheckBoxChecked[property.getIndex()]) {
				switch(property) {
				// unregister listener					
				case ACCELEROMETER:
				case MAGNETIC_FIELD:
				case ORIENTATION:
				case PROXIMITY:
				case TEMPERATURE:
					sensorManager.unregisterListener(sensorListener); break;			
					// unregister broadcast receiver
				case BATTERY:
					unregisterReceiver(batteryChangeReceiver); break;
				case NETWORK:
					unregisterReceiver(networkChangeReceiver); break;
					// remove location updates
				case LOCATION:
					locationManager.removeUpdates(gpsListener); break;
				}	
				Log.d("TAG", "Property " + property + " is unregisted");
			}
		}
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (observable instanceof ZombyDataList) {
			ZombyDataList list = (ZombyDataList) observable;
			
			Collections.reverse(list);			
			
			int countOfEntries = 20;
			if(list.size() < countOfEntries)
				textView.setText(list.subList(0, list.size()).toString());
			else
				textView.setText(list.subList(0, countOfEntries).toString());

			Collections.reverse(list);
		}
	}
	
	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}
	
	private void saveDialog() {
		if(zombyDataList.isEmpty())
			return;
		
		final EditText input = new EditText(this);
		input.setText("MyTestRecord");
		
		new AlertDialog.Builder(this)
	    .setTitle("Saving record...")
	    .setIcon(android.R.drawable.ic_menu_save)

	    .setView(input)
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int whichButton) {	        	
	        	filename = input.getText().toString();
	        	String filePath = getFilesDir() + "/" + filename + ZOMBY_POSTFIX;
	        	Log.d(TAG, "filePath: " + filePath);
	        	File file = new File(filePath);
	        	if(file.exists())
	        		existingFileDialog();
	        	else {
	        		Log.d("SaveDialog", "save record in file " + filename);
	        		new ZombyFileAsync(filename).execute();
	    			setStartMenu();
	        	}
	        }
	    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	setStartMenu();
	        }
	    }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog, int whichButton) {	    		
	    	}
	    }).show();
	}

	protected void existingFileDialog() {
		new AlertDialog.Builder(this)
	    .setTitle("File '" + filename + "' already exists!")
	    .setIcon(android.R.drawable.ic_dialog_alert)
	    .setMessage("Do you want to overwrite this?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	Log.d("SaveDialog", "save record in existing file '" + filename);
	        	new ZombyFileAsync(filename).execute();	    		
	    		setStartMenu();
	        }
	    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	saveDialog();
	        }
	    }).show();
	}

	private void setStartMenu() {
		recButton.setVisibility(View.VISIBLE);
		pauseButton.setVisibility(View.GONE);			
		stopButton.setVisibility(View.GONE);	
		textView.setText("");
		setAllCheckBoxes(View.VISIBLE);
	}
	
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_SAVE_PROGRESS:
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Save record...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
            return progressDialog;
        default:
            return null;
        }
    }

	class ZombyFileAsync extends AsyncTask<String, Integer, String> {
	
		private String filename;
		private FileOutputStream outputStream;
		private InputStream inputStream;
		private String TAG = "ZombyFileAsync";

		File sdcard = Environment.getExternalStorageDirectory();
		File dir = new File(sdcard.getAbsolutePath(), "Zomby Recorder");
		
		public ZombyFileAsync(String filename) {
			this.filename = filename;
		}
		
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        showDialog(DIALOG_SAVE_PROGRESS);
	    }
	
	    @Override
	    protected String doInBackground(String... aurl) {
	    	int lengthOfFile = zombyDataList.size()*2;
	    	progressDialog.setMax(lengthOfFile);
	    	int total = 0;
		    Log.d(TAG , "Lenght of file: " + lengthOfFile);
		
		    // write as zomby file
	 		try {
				File path = new File(dir, filename + ZOMBY_POSTFIX);
	 			outputStream = new FileOutputStream(path.getAbsoluteFile());
	 			for(ZombyData zombyData : zombyDataList) {
	 				outputStream.write(zombyData.toString().getBytes());
	 				outputStream.write(new String("\n").getBytes());
	 				total++;
	 				publishProgress(total);
	 			}
	 			outputStream.flush();
	 			outputStream.close();
	 		} catch(Exception e) {
	 			e.printStackTrace();
	 		}
	 		
	 		// write as java file
	 		try {
	 			String space = "   ";
	 			String javaFile = "";

				File path = new File(dir, filename + ".java");
				outputStream = new FileOutputStream(path.getAbsoluteFile());
	 			
	 			String zombyFileInput = "public void test" + filename + "() throws Exception {\n";
	 			outputStream.write(zombyFileInput.getBytes());	 		
	 			
	 			try {
	 				String dataString;
	 				long oldTimeStamp = 0;
	 				
	 				for(ZombyData zombyData : zombyDataList) {
	 					javaFile = "";
	 					
	 					dataString = zombyData.toString();
		 				String [] field = dataString.split(Pattern.quote(";"));
	 					if(field.length != 3)
	 						throw new ZombyException("invalid values in record file");
	 					
	 					long timeStamp = Long.valueOf(field[0]);
	 					String dataTag = field[1];
	 					String parameter = field[2];
	 					
	 					if(oldTimeStamp != 0) {
	 						long timeToWait = Math.round((timeStamp - oldTimeStamp));
	 						
	 						if(timeToWait > 5)
	 							javaFile += "  waitForRealtime(" + timeToWait + ");\n";
	 						else
	 							javaFile += "\n";
	 					}
	 							
	 					oldTimeStamp = timeStamp;
	 					
	 					switch(DataElement.valueOf(dataTag)) {
	 						case BATTERY:
	 							String [] batteryData = parameter.split(Pattern.quote(","));
	 							if(batteryData.length == 2) {
	 								int capacityValue = Integer.valueOf(batteryData[0]);
	 								BatteryStatus batteryStatus = BatteryStatus.valueOf(batteryData[1]);
	 								javaFile += space + "Zomby.getCorePower().capacity(" + capacityValue + ");\n";
	 								javaFile += space + "Zomby.getCorePower().status(BatteryState." + batteryStatus + ");";
	 							} 
	 							break;
	 							
	 						case NETWORK:	
	 							javaFile += space + "Zomby.getCoreNetwork().speed(NetworkSpeed.valueOf(\"" + parameter + "\"));";
	 							break;
	 							
	 						case LOCATION:
	 							String [] locationData = parameter.split(Pattern.quote(","));
	 							if(locationData.length == 2) {
	 								double longitude = Double.valueOf(locationData[0]);
	 								double latitude = Double.valueOf(locationData[1]);
	 								javaFile += space + "Zomby.getCoreGeo().fix(" + longitude + ", " + latitude + ");";
	 							} 
	 							break;
	 							
	 						case ACCELEROMETER:
	 							String [] accelerometerData = parameter.split(Pattern.quote(","));
	 							if(accelerometerData.length == 3) {
	 								float x = Float.valueOf(accelerometerData[0]);
	 								float y = Float.valueOf(accelerometerData[1]);
	 								float z = Float.valueOf(accelerometerData[2]);
	 								javaFile += space + "Zomby.getCoreSensor().set(Sensorname.ACCELERATION, " + x + ", " + y + ", " + z + ");";
	 							} 
	 							break;
	 						
	 						case MAGNETIC_FIELD:
	 							String [] magneticFieldData = parameter.split(Pattern.quote(","));
	 							if(magneticFieldData.length == 3) {
	 								float x = Float.valueOf(magneticFieldData[0]);
	 								float y = Float.valueOf(magneticFieldData[1]);
	 								float z = Float.valueOf(magneticFieldData[2]);
	 								javaFile += space + "Zomby.getCoreSensor().set(Sensorname.MAGNETIC_FIELD, " + x + ", " + y + ", " + z + ");";
	 							} 
	 							break;
	 						
	 						
	 						case ORIENTATION:
	 							String [] orientationData = parameter.split(Pattern.quote(","));
	 							if(orientationData.length == 3) {
	 								float x = Float.valueOf(orientationData[0]);
	 								float y = Float.valueOf(orientationData[1]);
	 								float z = Float.valueOf(orientationData[2]);
	 								javaFile += space + "Zomby.getCoreSensor().set(Sensorname.ORIENTATION, " + x + ", " + y + ", " + z + ");";
	 							} 
	 							break;
	 					
	 						case PROXIMITY:
	 							javaFile += space + "Zomby.getCoreSensor().set(Sensorname.PROXIMITY, " + parameter + ");";
	 							break;
	 						
	 						case TEMPERATURE:
	 							javaFile += space + "Zomby.getCoreSensor().set(Sensorname.TEMPERATURE, " + parameter + ");";
	 							break;
	 					}
	 					outputStream.write(javaFile.getBytes());
	 	
	 					total++;
		 				publishProgress(total);
		 			}
	 			} catch (Exception e) {
	 				e.printStackTrace();
	 			}	 			
	 			
	 			String end = "\n}\n\n";
	 			outputStream.write(end.getBytes());	 							
	 			

	 			String waitForRealtime = "private void waitForRealtime(long time) throws InterruptedException {\n" +
	 					"   double realtimeFactor = 1.0;\n" +
	 					"   long timeToWait = Math.round(time*realtimeFactor);\n" +
	 					"   synchronized(this) {\n" +
	 					"      if(timeToWait>10)\n" +
	 					"         wait(timeToWait);\n" +
	 					"   }\n}";
	 			outputStream.write(waitForRealtime.getBytes());
	 			
	 			outputStream.flush();
	 			outputStream.close();
	 			inputStream.close();
		
	    } catch (Exception e) {}
	    return null;
	
	    }
	    protected void onProgressUpdate(Integer... progress) {
	         progressDialog.setProgress(progress[0]);
	    }
	
	    @Override
	    protected void onPostExecute(String unused) {
	        dismissDialog(DIALOG_SAVE_PROGRESS);
	    }
	}
}
