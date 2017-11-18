package gi.hfhgit.callnotes;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by vytautasdagilis on 18/11/2017.
 */

public class DeviceAdminDemo extends DeviceAdminReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("DeviceAdminDemo", "onReceive");
    }

    public void onEnabled(Context context, Intent intent) {
        Log.d("DeviceAdminDemo", "onEnabled");
    }


    public void onDisabled(Context context, Intent intent) {
        Log.d("DeviceAdminDemo", "onDisabled");
    }

}