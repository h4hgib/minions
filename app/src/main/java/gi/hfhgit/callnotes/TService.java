package gi.hfhgit.callnotes;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by vytautasdagilis on 18/11/2017.
 */

public class TService extends Service {
    Context context;

    private Recorder recorderCustom = new Recorder();

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d("TService", "onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        recorderCustom.stop();
        Log.d("TService", "destroy");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        TelephonyManager telephony = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE); // TelephonyManager
        final PhoneStateListener customPhoneListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {

                if (state == 2) {
                    recorderCustom.init();
                    recorderCustom.record();
                    Log.e("TService", "Start recording");
                } else if (recorderCustom.isRecording() && state == 0) {
                    recorderCustom.stop();
                    Log.e("TService", "Stop Recording");
                }

            }
        };
        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        context = getApplicationContext();
        return START_NOT_STICKY;
    }
}