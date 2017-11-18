package gi.hfhgit.callnotes;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.telephony.TelephonyManager.ACTION_PHONE_STATE_CHANGED;

/**
 * Created by vytautasdagilis on 18/11/2017.
 *
 */

public class MyTService extends Service {
    Context context;

    private BroadcastReceiver br_call;

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d("MyTService", "onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("MyTService", "destroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        TelephonyManager telephony = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE); // TelephonyManager

        if (checkSelfPermission(Manifest.permission.CAPTURE_AUDIO_OUTPUT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("MyTService", "CAPTURE_AUDIO_OUTPUT permission not granted!");
        }
        final PhoneStateListener customPhoneListener = new MyPhoneStateListener();
        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        context = getApplicationContext();

        final IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_PHONE_STATE_CHANGED);
        filter.addAction("ndroid.intent.action.NEW_OUTGOING_CALL");
        this.br_call = new MyBroadcastReceiver();
        this.registerReceiver(this.br_call, filter);

        return START_NOT_STICKY;
    }

    private static class MyPhoneStateListener extends PhoneStateListener {

        private static final Recorder recorderCustom = new Recorder();

        private boolean wasCallingActive = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (state == 1) {
                wasCallingActive = true;
            }
            if (state == 2 && wasCallingActive) {
                wasCallingActive = false;
                recorderCustom.init();
                recorderCustom.record();
                Log.e("MyTService", "Start recording");
            } else if (recorderCustom.isRecording() && state == 0) {
                recorderCustom.stop();
                Log.e("MyTService", "Stop Recording");
            }

        }
    }

}