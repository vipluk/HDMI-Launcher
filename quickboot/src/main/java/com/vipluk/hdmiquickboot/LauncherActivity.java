package com.vipluk.hdmiquickboot;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LauncherActivity extends Activity {

    private static final int REQUEST_ROLE_HOME = 1001;

    private CountDownTimer autoBootTimer;
    private TextView tvStatus;
    private Button btnHdmi;
    private Button btnStock;
    private Button btnSetDefault;
    private boolean timerCanceled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        tvStatus = findViewById(R.id.tvStatus);
        btnHdmi = findViewById(R.id.btnHdmi);
        btnStock = findViewById(R.id.btnStockLauncher);
        btnSetDefault = findViewById(R.id.btnSetDefault);

        btnHdmi.setOnClickListener(v -> {
            cancelTimer();
            launchHdmi();
        });

        btnStock.setOnClickListener(v -> {
            cancelTimer();
            launchStockLauncher();
        });

        btnSetDefault.setOnClickListener(v -> {
            cancelTimer();
            requestDefaultLauncher();
        });

        boolean isDefault = isDefaultLauncher();
        if (isDefault) {
            btnSetDefault.setVisibility(View.GONE);
            btnHdmi.requestFocus();
            startCountdown();
        } else {
            timerCanceled = true;
            tvStatus.setText("Ustaw HDMI QuickBoot jako domyślny pulpit:");
            btnSetDefault.requestFocus();
            requestDefaultLauncher();
        }
    }

    private boolean isDefaultLauncher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            RoleManager roleManager = getSystemService(RoleManager.class);
            if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_HOME)) {
                return roleManager.isRoleHeld(RoleManager.ROLE_HOME);
            }
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo != null && resolveInfo.activityInfo != null && getPackageName().equals(resolveInfo.activityInfo.packageName);
    }

    private void requestDefaultLauncher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            RoleManager roleManager = getSystemService(RoleManager.class);
            if (roleManager != null && roleManager.isRoleAvailable(RoleManager.ROLE_HOME)) {
                if (!roleManager.isRoleHeld(RoleManager.ROLE_HOME)) {
                    try {
                        Intent roleIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME);
                        startActivityForResult(roleIntent, REQUEST_ROLE_HOME);
                        return;
                    } catch (Exception ignored) {}
                }
            }
        }

        // Fallback dla starszych wersji Androida lub nakładek producenta
        try {
            Intent homeSettings = new Intent(Settings.ACTION_HOME_SETTINGS);
            homeSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeSettings);
        } catch (Exception e1) {
            try {
                Intent manageApps = new Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS);
                manageApps.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(manageApps);
            } catch (Exception e2) {
                try {
                    Intent settings = new Intent(Settings.ACTION_SETTINGS);
                    settings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(settings);
                } catch (Exception ignored) {}
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ROLE_HOME) {
            if (isDefaultLauncher()) {
                tvStatus.setText("Pomyślnie ustawiono jako domyślny pulpit!");
                btnSetDefault.setVisibility(View.GONE);
                btnHdmi.requestFocus();
            }
        }
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
