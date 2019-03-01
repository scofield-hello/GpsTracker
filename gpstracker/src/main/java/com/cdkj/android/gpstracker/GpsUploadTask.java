package com.cdkj.android.gpstracker;

import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传位置Task.
 *
 * @author Nick
 */
public class GpsUploadTask implements Runnable {

    private static final String TAG = "GpsUploadTask";

    private String ak;

    private String api;

    private String extra;

    private String latitude;

    private String longitude;

    private String provider;

    private long timestamp;

    private String uid;

    public GpsUploadTask(String ak, String api, String uid, String extra, String latitude, String longitude,
            String provider, long timestamp) {
        this.ak = ak;
        this.api = api;
        this.uid = uid;
        this.extra = extra;
        this.latitude = latitude;
        this.longitude = longitude;
        this.provider = provider;
        this.timestamp = timestamp;
    }

    @Override
    public void run() {
        Map<String, Object> params = new HashMap<>(11);
        params.put("ak", ak);
        params.put("uid", uid);
        params.put("extra", extra);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("provider", provider);
        params.put("timestamp", timestamp);
        params.put("platform", "android");
        params.put("brand", Build.BRAND);
        params.put("model", Build.MODEL);
        params.put("androidSdk", VERSION.SDK_INT);
        boolean isSuccess = HttpUtils.post(api, params);
        Log.i(TAG, "位置上传结果:" + (isSuccess ? "成功" : "失败"));
    }
}
