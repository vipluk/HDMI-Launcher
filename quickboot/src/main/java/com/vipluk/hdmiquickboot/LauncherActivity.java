package com.vipluk.hdmiquickboot;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

public class LauncherActivity extends Activity {

    private CountDownTimer autoBootTimer;
    private TextView tvStatus;
    private boolean timerCanceled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        tvStatus = findViewById(R.id.tvStatus);
        Button btnHdmi = findViewById(R.id.btnHdmi);
        Button btnStock = findViewById(R.id.btnStockLauncher);

        btnHdmi.requestFocus();

        btnHdmi.setOnClickListener(v -> {
            cancelTimer();
            launchHdmi();
        });

        btnStock.setOnClickListener(v -> {
            cancelTimer();
            launchStockLauncher();
        });

        startCountdown();
    }

    private void startCountdown() {
        autoBootTimer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (!timerCanceled) {
                    tvStatus.setText("Uruchamianie HDMI za " + (millisUntilFinished / 1000 + 1) + "s...");
                }
            }

            public void onFinish() {
                if (!timerCanceled) {
                    launchHdmi();
                }
            }
        }.start();
    }

    private void cancelTimer() {
        if (!timerCanceled && autoBootTimer != null) {
            autoBootTimer.cancel();
            timerCanceled = true;
            tvStatus.setText("Wybierz opcję:");
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Zatrzymuje automatyczny start, jeśli użytkownik kliknie dowolny przycisk na pilocie
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            cancelTimer();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        cancelTimer();
        launchStockLauncher();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (autoBootTimer != null) {
            autoBootTimer.cancel();
        }
    }

    private void launchHdmi() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.softwinner.awlivetv", "com.softwinner.awlivetv.MainActivity"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            // Fallback na starsze lub alternatywne firmware Allwinner
            try {
                Intent fallback = new Intent();
                fallback.setComponent(new ComponentName("com.softwinner.awsource", "com.softwinner.awsource.MainActivity"));
                fallback.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(fallback);
            } catch (Exception ignored) {}
        }
    }

    private void launchStockLauncher() {
        try {
            // Oryginalny pulpit z listy pakietów projektora
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.htc.htclauncherhighenglishd08");
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
        } catch (Exception ignored) {}

        // Fallback: próba uruchomienia dowolnego innego zainstalowanego launchera HOME
        try {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            for (ResolveInfo ri : getPackageManager().queryIntentActivities(homeIntent, 0)) {
                if (ri.activityInfo != null && !getPackageName().equals(ri.activityInfo.packageName)) {
                    homeIntent.setComponent(new ComponentName(ri.activityInfo.packageName, ri.activityInfo.name));
                    startActivity(homeIntent);
                    return;
                }
            }
        } catch (Exception ignored) {}
    }
}
