package com.cdkj.android.gpstracker;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.cdkj.android.gpstracker.GpsTracker.LocationCallback;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 定位服务
 *
 * @author Nick
 */
public class MyService extends Service implements LocationCallback {

    private static final String TAG = "MyService";

    public final static String EXTRA_AK = "ak";

    public final static String EXTRA_API = "api";

    public final static String EXTRA_UID = "uid";

    public final static String EXTRA_EXTRA = "extra";

    public final static String EXTRA_COMMAND = "command";

    public final static String EXTRA_NOTIFICATION_ICON = "notification_icon";

    public final static String EXTRA_NOTIFICATION_TITLE = "notification_title";

    public final static String EXTRA_NOTIFICATION_CONTENT = "notification_content";

    public final static String EXTRA_MIN_PERIOD = "min_period";

    public final static String EXTRA_MIN_DISTANCE = "min_distance";

    private final static int ONGOING_NOTIFICATION_ID = 0x111;

    private String ak;

    private String api;

    private float distanceInMi;

    private String extra;

    private GpsTracker mGpsTracker;

    private MyReceiver mReceiver = new MyReceiver();

    private String notificationContent;

    private int notificationIcon;

    private String notificationTitle;

    private long periodInMills;

    private ExecutorService threadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MINUTES,
            new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
        @Override
        public Thread newThread(final Runnable r) {
            return new Thread(r, "gps_upload_thread_1");
        }
    });

    private String uid;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGpsTracker = new GpsTracker(this, this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        threadPool.shutdown();
        unregisterReceiver(mReceiver);
        Log.i(TAG, "服务已关闭...");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(final Location location) {
        Log.d(TAG, "onLocationChanged: 获取到位置信息:" + location.toString());
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        String provider = location.getProvider();
        long timestamp = location.getTime();
        threadPool.submit(new GpsUploadTask(ak, api, uid, extra, latitude, longitude, provider, timestamp));
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        processCommand(intent);
        return START_REDELIVER_INTENT;
    }

    protected void createNotification(Service context, int icon, String title, String content) {
        if (VERSION.SDK_INT < VERSION_CODES.O) {
            createNotificationPreO(context, icon, title, content);
        } else {
            createNotificationO(context, icon, title, content);
        }
    }

    @TargetApi(26)
    protected void createNotificationO(Service context, int icon, String title, String content) {
        // Create a channel.
        String chanelId = String.valueOf(new Random().nextInt(10000));
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
        CharSequence channelName = "Playback channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel =
                new NotificationChannel(
                        chanelId, channelName, importance);
        notificationManager.createNotificationChannel(
                notificationChannel);
        Intent intent = new Intent("com.cdkj.android.gpstracker.start");
        PendingIntent piLaunchMainActivity = PendingIntent
                .getBroadcast(context, 10001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(context, chanelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(piLaunchMainActivity)
                .setStyle(new Notification.BigTextStyle())
                .build();
        context.startForeground(
                ONGOING_NOTIFICATION_ID, notification);
    }

    @TargetApi(25)
    protected void createNotificationPreO(Service context, int icon, String title, String content) {
        Intent intent = new Intent("com.cdkj.android.gpstracker.start");
        PendingIntent piLaunchMainActivity = PendingIntent
                .getBroadcast(context, 10001, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification mNotification =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(icon)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setContentIntent(piLaunchMainActivity)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .build();

        context.startForeground(
                ONGOING_NOTIFICATION_ID, mNotification);
    }

    protected void processCommand(final Intent intent) {
        int command = intent.getIntExtra(EXTRA_COMMAND, Command.START_SERVICE);
        Log.d(TAG, "processCommand: command=" + command);
        try {
            loadNewIntentArgs(intent);
        } catch (Exception e) {
            Log.e(TAG, "参数传递错误", e);
            track(false);
            stopForeground(true);
            stopSelf();
        }
        mGpsTracker.config(periodInMills, distanceInMi);
        switch (command) {
            case Command.START_SERVICE:
                Log.d(TAG, "processCommand: 切换到前台服务...");
                createNotification(this, notificationIcon, notificationTitle, notificationContent);
                track(true);
                break;
            case Command.STOP_SERVICE:
                Log.d(TAG, "processCommand: 停止当前服务...");
                track(false);
                stopForeground(true);
                stopSelf();
                break;
            case Command.START_GPS:
                Log.d(TAG, "processCommand: 开启位置跟踪...");
                createNotification(this, notificationIcon, notificationTitle, notificationContent);
                track(true);
                break;
            case Command.STOP_GPS:
                createNotification(this, notificationIcon, notificationTitle, notificationContent);
                Log.d(TAG, "processCommand: 关闭位置跟踪...");
                track(false);
                break;
            default:
                Log.d(TAG, "processCommand: 命令不正确，停止当前服务...");
                track(false);
                stopForeground(true);
                stopSelf();
                break;
        }
    }

    private void loadNewIntentArgs(final Intent intent) {
        ak = intent.getStringExtra(EXTRA_AK);
        api = intent.getStringExtra(EXTRA_API);
        uid = intent.getStringExtra(EXTRA_UID);
        extra = intent.getStringExtra(EXTRA_EXTRA);
        periodInMills = intent.getLongExtra(EXTRA_MIN_PERIOD, 10000L);
        distanceInMi = intent.getFloatExtra(EXTRA_MIN_DISTANCE, 0.0F);
        notificationIcon = intent.getIntExtra(EXTRA_NOTIFICATION_ICON, 0);
        notificationTitle = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE);
        notificationContent = intent.getStringExtra(EXTRA_NOTIFICATION_CONTENT);
        Preconditions.checkNotNull(ak);
        Preconditions.checkNotNull(api);
        Preconditions.checkNotNull(uid);
        Preconditions.checkNotNull(notificationTitle);
        Preconditions.checkNotNull(notificationContent);
        Preconditions.checkArgument(notificationIcon != 0);
        Preconditions.checkArgument(periodInMills >= 1000L);
        Preconditions.checkArgument(distanceInMi >= 0.0F);
    }

    private void track(boolean start) {
        if (start) {
            if (mGpsTracker.isStart()) {
                mGpsTracker.stop();
            }
            mGpsTracker.start();
        } else {
            if (mGpsTracker.isStart()) {
                mGpsTracker.stop();
            }
        }
    }
}
