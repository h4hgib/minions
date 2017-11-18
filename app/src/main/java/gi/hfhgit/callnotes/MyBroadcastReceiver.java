package gi.hfhgit.callnotes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by vytautasdagilis on 19/11/2017.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MyBroadcastReceiver", "Received Intent!");
        //additional way how to catch incomming calls. Atm it seems not to be the problem.
        //but we can move everything from gi.hfhgit.callnotes.MyTService.MyPhoneStateListener
        //to this class - it *might* be executed on "better" thread.
    }
}