package com.vipluk.hdmilauncher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Runnable launchRunnable = () -> {
            // Lista potencjalnych pakietów HDMI na Allwinnerze
            String[][] candidates = {
                {"com.softwinner.awlivetv", "com.softwinner.awlivetv.MainActivity"},
                {"com.softwinner.awsource", "com.softwinner.awsource.MainActivity"}
            };

            for (String[] target : candidates) {
                try {
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(target[0], target[1]));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                } catch (Exception ignored) {
                    // Jeśli dana aktywność nie istnieje na tym modelu, próbuje następnej
                }
            }

            finish();
        };

        if (BuildConfig.DELAY_MS > 0) {
            new Handler(Looper.getMainLooper()).postDelayed(launchRunnable, BuildConfig.DELAY_MS);
        } else {
            launchRunnable.run();
        }
    }
}
