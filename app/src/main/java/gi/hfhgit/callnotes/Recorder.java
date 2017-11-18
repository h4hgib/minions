package gi.hfhgit.callnotes;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by vytautasdagilis on 18/11/2017.
 */

public class Recorder {

    private MediaRecorder callrecorder;
    boolean recording = false;

    public void init() {
        callrecorder = new MediaRecorder();
        callrecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        callrecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        callrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        callrecorder.setOutputFile(getFilePath());

    }

    private String getFilePath() {

        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, "MediaRecorderSample");

        if (!file.exists())
            file.mkdirs();

        Log.e("Recorder", "Drirectory status. Exists: " + file.exists() + " Can Write: " + file.canWrite() + " Path " + file.getAbsolutePath());
        String filePath = file.getAbsolutePath() + "/" + "test.3gp";
        File fileLocation = new File(filePath);
        if (fileLocation.exists()) {
            fileLocation.delete();
        }
        Log.e("Recorder", "File status. Exists: " + fileLocation.exists() + " Can Write: " + fileLocation.canWrite() + " Path " + fileLocation.getAbsolutePath());
        return fileLocation.getAbsolutePath();
    }

    public boolean isRecording() {
        return recording;
    }

    public void record() {
        recording = true;
        try {
            callrecorder.prepare();
            Thread.sleep(1000);
        } catch (IllegalStateException e) {
            Log.e("Recorder", "An IllegalStateException has occured in prepare!");
            e.printStackTrace();
        } catch (IOException e) {

            //throwing I/O Exception
            Log.e("Recorder", "An IOException has occured in prepare!");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            callrecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            //Here it is thorowing illegal State exception
            Log.e("Recorder", "An IllegalStateException has occured in start!");
        }
    }

    public void stop() {
        recording = false;
        callrecorder.stop();
        callrecorder.reset();
        callrecorder.release();
    }
}
