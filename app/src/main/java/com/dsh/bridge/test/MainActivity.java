package com.dsh.bridge.test;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class MainActivity extends Activity {
    private TextView tvInfo;
    private TextView tvResult;
    private Button bCheck;
    private Button bBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = findViewById(R.id.tv_info);
        tvResult = findViewById(R.id.tv_result);
        bCheck = findViewById(R.id.btn_check);
        bBridge = findViewById(R.id.btn_bridge);
        tvInfo.setText(buildInfo());
        bCheck.setOnClickListener(v -> tvResult.setText(buildInfo()));
        bBridge.setOnClickListener(v -> runTest());
    }

    private String buildInfo() {
        String app = "";
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            app = "app=" + pi.packageName + "\nversion=" + pi.versionName;
        } catch (Exception ignored) {}
        return "Native Android Activity\n" + app + "\nsdk=" + Build.VERSION.SDK_INT + " android=" + Build.VERSION.RELEASE + "\nmanufacturer=" + Build.MANUFACTURER + " model=" + Build.MODEL;
    }

    private void runTest() {
        new Thread(() -> {
            StringBuilder sb = new StringBuilder();
            sb.append("network bridge test:");
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL("https://api.github.com/repos/Dggggfyvhi/dsh-native-bridge-test").openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                int code = conn.getResponseCode();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = br.readLine();
                sb.append("\nHTTP ").append(code).append("\n").append(line == null ? "" : line.substring(0, Math.min(200, line.length())));
            } catch (Exception e) {
                sb.append("\nexception=").append(e.getClass().getSimpleName()).append(':').append(e.getMessage());
            }
            runOnUiThread(() -> tvResult.setText(sb.toString()));
        }).start();
    }
}
