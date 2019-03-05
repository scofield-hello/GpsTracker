package com.cdkj.android.gpstracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.Log;

/**
 * @author Nick
 */
public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ---------------收到广播:" + intent.getAction());
        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            startServiceCommand(context, Command.START_GPS);
        } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            startServiceCommand(context, Command.STOP_GPS);
        } else {
            startServiceCommand(context, Command.STOP_GPS);
        }
    }

    private void startServiceCommand(Context context, int command) {
        Intent intent = new ServiceIntentBuilder(context)
                .setCommand(command).build();
        if (VERSION.SDK_INT < VERSION_CODES.O) {
            context.startService(intent);
        } else {
            context.startForegroundService(intent);
        }
    }
}
