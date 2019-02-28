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

    private String latitude;

    private String longitude;

    private String uid;

    public GpsUploadTask(String ak, String api, String uid, String latitude, String longitude) {
        this.ak = ak;
        this.api = api;
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void run() {
        Map<String, Object> params = new HashMap<>(8);
        params.put("ak", ak);
        params.put("uid", uid);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("platform", "android");
        params.put("brand", Build.BRAND);
        params.put("model", Build.MODEL);
        params.put("androidSdk", VERSION.SDK_INT);
        boolean isSuccess = HttpUtils.post(api, params);
        Log.i(TAG, "位置上传结果:" + (isSuccess ? "成功" : "失败"));
    }
}
