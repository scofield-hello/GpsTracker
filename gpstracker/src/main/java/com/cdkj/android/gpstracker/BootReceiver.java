package com.cdkj.android.gpstracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

/**
 * @author Nick
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new ServiceIntentBuilder(context)
                    .setCommand(Command.START_SERVICE)
                    .build();
            if (VERSION.SDK_INT < VERSION_CODES.O) {
                context.startService(serviceIntent);
            } else {
                context.startForegroundService(serviceIntent);
            }
        }
    }
}
