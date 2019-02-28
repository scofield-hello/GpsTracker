package com.cdkj.android.gpstracker;

import android.content.Context;
import android.content.Intent;

/**
 * @author Nick
 */
public class ServiceIntentBuilder {

    private String ak;

    private String api;

    private int command;

    private String extra;

    private Context mContext;

    private float minDistance = 0.0F;

    private long minPeriod = 10000L;

    private String notificationContent = "正在跟踪您的位置...";

    private int notificationIcon = 0;

    private String notificationTitle = "位置服务";

    private String uid;

    public ServiceIntentBuilder(Context context) {
        this.mContext = context;
    }

    public Intent build() {
        Preconditions.checkArgument(command == Command.START_SERVICE
                || command == Command.STOP_SERVICE || command == Command.START_GPS
                || command == Command.STOP_GPS, "不合法的command参数");
        Intent intent = new Intent(mContext, MyService.class);
        intent.putExtra(MyService.EXTRA_COMMAND, command);
        intent.putExtra(MyService.EXTRA_NOTIFICATION_ICON, notificationIcon);
        intent.putExtra(MyService.EXTRA_NOTIFICATION_TITLE, notificationTitle);
        intent.putExtra(MyService.EXTRA_NOTIFICATION_CONTENT, notificationContent);
        intent.putExtra(MyService.EXTRA_AK, ak);
        intent.putExtra(MyService.EXTRA_API, api);
        intent.putExtra(MyService.EXTRA_UID, uid);
        intent.putExtra(MyService.EXTRA_EXTRA, extra);
        intent.putExtra(MyService.EXTRA_MIN_PERIOD, minPeriod);
        intent.putExtra(MyService.EXTRA_MIN_DISTANCE, minDistance);
        return intent;
    }

    public ServiceIntentBuilder setAk(final String ak) {
        this.ak = ak;
        return this;
    }

    public ServiceIntentBuilder setApi(final String api) {
        this.api = api;
        return this;
    }

    public ServiceIntentBuilder setCommand(final int command) {
        this.command = command;
        return this;
    }

    public ServiceIntentBuilder setExtra(final String extra) {
        this.extra = extra;
        return this;
    }

    public ServiceIntentBuilder setMinDistance(final float minDistance) {
        this.minDistance = minDistance;
        return this;
    }

    public ServiceIntentBuilder setMinPeriod(final long minPeriod) {
        this.minPeriod = minPeriod;
        return this;
    }

    public ServiceIntentBuilder setNotificationContent(final String notificationContent) {
        this.notificationContent = notificationContent;
        return this;
    }

    public ServiceIntentBuilder setNotificationIcon(final int notificationIcon) {
        this.notificationIcon = notificationIcon;
        return this;
    }

    public ServiceIntentBuilder setNotificationTitle(final String notificationTitle) {
        this.notificationTitle = notificationTitle;
        return this;
    }

    public ServiceIntentBuilder setUid(final String uid) {
        this.uid = uid;
        return this;
    }
}
