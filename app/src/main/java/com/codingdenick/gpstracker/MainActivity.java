package com.codingdenick.gpstracker;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.cdkj.android.gpstracker.Command;
import com.cdkj.android.gpstracker.ServiceIntentBuilder;

/**
 * @author Nick
 */
public class MainActivity extends AppCompatActivity {

    private final static int ACTION_REQUEST_PERMISSION = 0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission.ACCESS_FINE_LOCATION},
                    ACTION_REQUEST_PERMISSION);
        } else {
            startService();
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
            @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSION) {
            boolean grantResult = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    grantResult = false;
                    break;
                }
            }
            if (grantResult) {
                startService();
            } else {
                Toast.makeText(this, "授权失败！", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startService() {
        Intent intent = new ServiceIntentBuilder(this)
                .setNotificationIcon(R.drawable.ic_stat_name)
                .setNotificationTitle("在线教育服务")
                .setNotificationContent("GPS服务正在跟踪...")
                .setAk("456ftygygbubhj7uyh7k9089")
                .setApi("http://zjsf.lookmap.net/sqjz/jhsaijs")
                .setUid("Userid")
                .setExtra("extra")
                .setCommand(Command.START_SERVICE)
                .build();
        if (VERSION.SDK_INT < VERSION_CODES.O) {
            startService(intent);
        } else {
            startForegroundService(intent);
        }
    }
}