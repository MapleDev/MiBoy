package com.xznn.miboy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    public static final String TAG = "MainActivity";

    private Switch sw_fuck_vivo;
    private EditText et_password ;
    private Switch sw_mi_boy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sw_fuck_vivo = findViewById(R.id.sw_fuck_vivo);
        et_password = findViewById(R.id.et_password);
        sw_mi_boy = findViewById(R.id.sw_mi_boy);

        findViewById(R.id.btn_start_settings).setOnClickListener(this);
        sw_fuck_vivo.setOnCheckedChangeListener(this);
        sw_mi_boy.setOnCheckedChangeListener(this);


        boolean isOpenFuckVivo = PreferencesUtils.getBoolean(this, PreferencesUtils.IS_OPEN_FUCK_VIVO);
        sw_fuck_vivo.setChecked(isOpenFuckVivo);
        if (!isOpenFuckVivo) {
            et_password.setVisibility(View.GONE);
        }
        String password = PreferencesUtils.getString(getApplicationContext(), PreferencesUtils.KEY_PASSWORD);
        if (!TextUtils.isEmpty(password)) {
            et_password.setText(password);
        }


        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    savePassword(v);
                }
                return false;
            }
        });

        boolean isOpenMiBoy = PreferencesUtils.getBoolean(this, PreferencesUtils.IS_OPEN_MI_BOY);
        sw_mi_boy.setChecked(isOpenMiBoy);
    }

    private void savePassword(TextView view) {
        String password = view.getText().toString().trim();
        PreferencesUtils.saveString(this, PreferencesUtils.KEY_PASSWORD, password);
        Toast.makeText(this, R.string.toast_save_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 打开辅助功能
            case R.id.btn_start_settings:
                startActivity(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            // FuckVivo-自动填充安装密码
            case R.id.sw_fuck_vivo:
                PreferencesUtils.saveBoolean(this, PreferencesUtils.IS_OPEN_FUCK_VIVO, b);
                et_password.setVisibility(b ? View.VISIBLE : View.GONE);
                break;
            case R.id.sw_mi_boy:
                PreferencesUtils.saveBoolean(this, PreferencesUtils.IS_OPEN_MI_BOY, b);

                if (Build.VERSION.SDK_INT >= 23) {
                    if (Settings.canDrawOverlays(MainActivity.this)) {
                        Intent intent = new Intent(MainActivity.this, FloatService.class);
                        Toast.makeText(MainActivity.this,"已开启Toucher",Toast.LENGTH_SHORT).show();
                        startService(intent);
                        finish();
                    } else {
                        //若没有权限，提示获取.
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        Toast.makeText(MainActivity.this,"需要取得权限以使用悬浮窗",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                } else {
                    //SDK在23以下，不用管.
                    Intent intent = new Intent(MainActivity.this, FloatService.class);
                    startService(intent);
                    finish();
                }
                break;
        }
    }
}
