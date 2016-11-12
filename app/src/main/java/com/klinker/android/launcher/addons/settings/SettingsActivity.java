/*
 * Copyright 2014 Klinker Apps Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klinker.android.launcher.addons.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.klinker.android.launcher.R;
import com.klinker.android.launcher.addons.settings.card_picker.CardPickerActivity;
import com.klinker.android.launcher.addons.settings.page_picker.PagePickerActivity;
import com.klinker.android.launcher.addons.utils.Item;
import com.klinker.android.launcher.addons.utils.Utils;
import com.klinker.android.launcher.launcher3.Utilities;


public class SettingsActivity extends Activity {

    public Context context;
    public static boolean prefChanged;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        if (Build.VERSION.SDK_INT >= 21) {
            int color = getResources().getColor(R.color.black);
            int transparent = adjustAlpha(color, .65f);

            getWindow().setStatusBarColor(transparent);
            getWindow().setNavigationBarColor(transparent);
        }

        prefChanged = false;
        context = this;

        setContentView(R.layout.settings_activity);

        findViewById(R.id.background).setAlpha(55/100f);

        getViews();
        setFeedback();
        setClicks();
    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public void onStop() {
        if (SettingsActivity.prefChanged) {
            // Manually kill this process
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            super.onStop();
        }
    }

    public TextView layout;
    public TextView visuals;
    public TextView dock;

    public void getViews() {
        layout = (TextView) findViewById(R.id.screen_layout_button);
        visuals = (TextView) findViewById(R.id.visuals_button);
        dock = (TextView) findViewById(R.id.dock_button);

        Utilities.applyTypeface(layout);
        Utilities.applyTypeface(visuals);
        Utilities.applyTypeface(dock);

    }

    public void setFeedback() {
        View.OnTouchListener mHapticFeedbackTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                    v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }
                return false;
            }
        };

        layout.setOnTouchListener(mHapticFeedbackTouchListener);
        visuals.setOnTouchListener(mHapticFeedbackTouchListener);
        dock.setOnTouchListener(mHapticFeedbackTouchListener);
    }

    public static final int PAGE_LAYOUT = 1;
    public static final int PAGE_VISUALS = 2;
    public static final int PAGE_DOCK = 3;
    public static final int PAGE_EXPERIMENTAL = 4;
    public static final int PAGE_BACKUP = 5;
    public static final int PAGE_GESTURES = 6;

    public void setClicks() {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent popupSetting = new Intent(context, SettingsPopupActivity.class);
                popupSetting.putExtra("page", PAGE_LAYOUT);
                startActivity(popupSetting);
            }
        });

        visuals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent popupSetting = new Intent(context, SettingsPopupActivity.class);
                popupSetting.putExtra("page", PAGE_VISUALS);
                startActivity(popupSetting);
            }
        });

        dock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent popupSetting = new Intent(context, SettingsPopupActivity.class);
                popupSetting.putExtra("page", PAGE_DOCK);
                startActivity(popupSetting);
            }
        });

    }

}
