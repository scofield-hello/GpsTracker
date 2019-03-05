package com.cdkj.android.gpstracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * @author Nick
 */
public class ServiceIntentBuilder {

    private static final String PREF_NAME = "gps_tracker";

    private String ak;

    private String api;

    private int command;

    private String extra;

    private Context mContext;

    private SharedPreferences mSharedPreferences;

    private float minDistance = 0.0F;

    private long minPeriod = 10000L;

    private String notificationContent = "正在跟踪您的位置...";

    private int notificationIcon = 0;

    private String notificationTitle = "位置服务";

    private String uid;

    public ServiceIntentBuilder(Context context) {
        this.mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public Intent build() {
        Preconditions.checkArgument(command == Command.START_SERVICE
                || command == Command.STOP_SERVICE || command == Command.START_GPS
                || command == Command.STOP_GPS, "不合法的command参数");
        Intent intent = new Intent(mContext, MyService.class);
        intent.putExtra(MyService.EXTRA_COMMAND, command);
        intent.putExtra(MyService.EXTRA_NOTIFICATION_ICON,
                mSharedPreferences.getInt("notificationIcon", notificationIcon));
        intent.putExtra(MyService.EXTRA_NOTIFICATION_TITLE,
                mSharedPreferences.getString("notificationTitle", notificationTitle));
        intent.putExtra(MyService.EXTRA_NOTIFICATION_CONTENT,
                mSharedPreferences.getString("notificationContent", notificationContent));
        intent.putExtra(MyService.EXTRA_AK, mSharedPreferences.getString("ak", ak));
        intent.putExtra(MyService.EXTRA_API, mSharedPreferences.getString("api", api));
        intent.putExtra(MyService.EXTRA_UID, mSharedPreferences.getString("uid", uid));
        intent.putExtra(MyService.EXTRA_EXTRA, mSharedPreferences.getString("extra", extra));
        intent.putExtra(MyService.EXTRA_MIN_PERIOD, mSharedPreferences.getLong("minPeriod", minPeriod));
        intent.putExtra(MyService.EXTRA_MIN_DISTANCE, mSharedPreferences.getFloat("minDistance", minDistance));
        return intent;
    }

    public ServiceIntentBuilder setAk(final String ak) {
        this.ak = ak;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("ak", ak);
        editor.apply();
        return this;
    }

    public ServiceIntentBuilder setApi(final String api) {
        this.api = api;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("api", api);
        editor.apply();
        return this;
    }

    public ServiceIntentBuilder setCommand(final int command) {
        this.command = command;
        return this;
    }

    public ServiceIntentBuilder setExtra(final String extra) {
        this.extra = extra;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("extra", extra);
        editor.apply();
        return this;
    }

    public ServiceIntentBuilder setMinDistance(final float minDistance) {
        this.minDistance = minDistance;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat("minDistance", minDistance);
        editor.apply();
        return this;
    }

    public ServiceIntentBuilder setMinPeriod(final long minPeriod) {
        this.minPeriod = minPeriod;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong("minPeriod", minPeriod);
        editor.apply();
        return this;
    }

    public ServiceIntentBuilder setNotificationContent(final String notificationContent) {
        this.notificationContent = notificationContent;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("notificationContent", notificationContent);
        editor.apply();
        return this;
    }

    public ServiceIntentBuilder setNotificationIcon(final int notificationIcon) {
        this.notificationIcon = notificationIcon;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("notificationIcon", notificationIcon);
        editor.apply();
        return this;
    }

    public ServiceIntentBuilder setNotificationTitle(final String notificationTitle) {
        this.notificationTitle = notificationTitle;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("notificationTitle", notificationTitle);
        editor.apply();
        return this;
    }

    public ServiceIntentBuilder setUid(final String uid) {
        this.uid = uid;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("uid", uid);
        editor.apply();
        return this;
    }
}
