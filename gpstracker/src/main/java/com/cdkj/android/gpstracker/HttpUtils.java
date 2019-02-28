package com.cdkj.android.gpstracker;

import android.util.Log;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpUtils {

    private static final String TAG = "HttpUtils";

    /**
     * 发送携带参数的post请求
     *
     * @return 输入流
     */
    public static boolean post(String path, Map<String, Object> params) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setReadTimeout(6000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.write(sb.toString().getBytes());
            dos.flush();
            dos.close();
            Log.i(TAG, "post: http response code:" + conn.getResponseCode());
            return conn.getResponseCode() == HttpURLConnection.HTTP_OK;
            /*if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                byte[] data = new byte[1024];
                InputStream inputStream = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int length;
                while ((length = inputStream.read(data)) != -1) {
                    bos.write(data, 0, length);
                }
                data = bos.toByteArray();
                String response = new String(data, Charset.forName("UTF-8"));
                JSONObject jsonObject = new JSONObject(response);
                return jsonObject;
            } else {
                throw new Exception("接口访问失败，http response code:" + conn.getResponseCode());
            }*/
        } catch (Exception e) {
            Log.e(TAG, "post:", e);
            return false;
        }
    }
}
