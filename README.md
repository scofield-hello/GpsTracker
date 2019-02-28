# GpsTracker

## Usage

```java
 if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(this, new String[]{permission.ACCESS_FINE_LOCATION},
                    ACTION_REQUEST_PERMISSION);
 } else {
    startService(); 
 }
 
 /**
 *开启服务
 */
 private void startService() {
    Intent intent = new ServiceIntentBuilder(this)
            .setNotificationIcon(R.drawable.ic_stat_name)
            .setNotificationTitle("位置上报")
            .setNotificationContent("服务正在运行中...")
            .setAk(ak)
            .setApi(apiUrl)
            .setUid(userId)
            .setCommand(Command.START_SERVICE)
            .build();
    if (VERSION.SDK_INT < VERSION_CODES.O) {
        startService(intent);
    } else {
        startForegroundService(intent);
    }
  }
 
```
