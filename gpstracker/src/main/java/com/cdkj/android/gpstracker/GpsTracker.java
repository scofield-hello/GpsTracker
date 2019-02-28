package com.cdkj.android.gpstracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

/**
 * Gps AbstractTracker.
 *
 * @author Nick
 */
public class GpsTracker implements LocationListener {

    public interface LocationCallback {

        /**
         * 当位置更新时回调.
         */
        void onLocationChanged(Location location);
    }

    private static final String TAG = "GpsTracker";

    private LocationCallback mCallback;

    private LocationManager mLocationManager;

    private String mProvider;

    private boolean mStart = false;

    public GpsTracker(Context context, LocationCallback callback) {
        Log.i(TAG, "GpsTracker: 创建实例");
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.mCallback = callback;
    }

    @Override
    public void onLocationChanged(final Location location) {
        if (mCallback != null) {
            mCallback.onLocationChanged(location);
        }
    }

    @Override
    public void onProviderDisabled(final String provider) {
        String newBestProvider = getBestProvider();
        if (!newBestProvider.equals(mProvider)) {
            if (mStart) {
                stop();
                start();
            }
        }
    }

    @Override
    public void onProviderEnabled(final String provider) {
        String newBestProvider = getBestProvider();
        if (!newBestProvider.equals(mProvider)) {
            if (mStart) {
                stop();
                start();
            }
        }
    }

    @Override
    public void onStatusChanged(final String provider, final int status, final Bundle extras) {
        if (provider.equals(mProvider)) {
            if (status == LocationProvider.OUT_OF_SERVICE
                    || status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
                if (mStart) {
                    stop();
                    start();
                }
            }
        }
    }

    String getBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setCostAllowed(true);
        return mLocationManager.getBestProvider(criteria, true);
    }

    boolean isStart() {
        return mStart;
    }

    @SuppressLint("MissingPermission")
    void start() {
        if (!mStart) {
            mProvider = getBestProvider();
            mLocationManager.requestLocationUpdates(mProvider, 10000L, 10.0F, this);
            Log.i(TAG, "start: 开始定位, 位置提供者" + mProvider);
            mStart = true;
        }
    }

    void stop() {
        if (mStart) {
            mLocationManager.removeUpdates(this);
            Log.i(TAG, "start: 停止定位, 位置提供者" + mProvider);
            mStart = false;
        }
    }
}