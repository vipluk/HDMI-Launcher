package com.vipluk.hdmilauncher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lista potencjalnych pakietów HDMI na Allwinnerze
        String[][] candidates = {
            {"com.softwinner.awlivetv", "com.softwinner.awlivetv.MainActivity"},
            {"com.softwinner.awsource", "com.softwinner.awsource.MainActivity"}
        };

        boolean started = false;

        for (String[] target : candidates) {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(target[0], target[1]));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                started = true;
                break;
            } catch (Exception ignored) {
                // Jeśli dana aktywność nie istnieje na tym modelu, próbuje następnej
            }
        }

        finish();
    }
}
