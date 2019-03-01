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

    private float mDistance = 0.0F;

    private LocationManager mLocationManager;

    private long mPeriod = 10000L;

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

    @SuppressLint("MissingPermission")
    public void start() {
        if (!mStart) {
            mProvider = getBestProvider();
            mLocationManager.requestLocationUpdates(mProvider, mPeriod, mDistance, this);
            Log.i(TAG, "start: 开始定位, 位置提供者" + mProvider);
            mStart = true;
        }
    }

    public void stop() {
        if (mStart) {
            mLocationManager.removeUpdates(this);
            Log.i(TAG, "start: 停止定位, 位置提供者" + mProvider);
            mStart = false;
        }
    }

    void initConfiguration(long period, float distance) {
        mPeriod = period;
        mDistance = distance;
    }

    boolean isStart() {
        return mStart;
    }

    private String getBestProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        return mLocationManager.getBestProvider(criteria, true);
    }
}
