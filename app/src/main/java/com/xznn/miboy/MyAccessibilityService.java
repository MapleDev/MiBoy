package com.xznn.miboy;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MapleDev on 19/03/08 16:45
 */
public class MyAccessibilityService extends AccessibilityService {
    public static final String TAG = "MyAccessibilityService";

    private HashMap<String, Boolean> mBtnPerformMap = new HashMap<>();
    private boolean mIsOpenFuckVivo;
    private boolean mIsOpenMiBoy;

    @Override
    public void onCreate() {
        super.onCreate();

        mIsOpenFuckVivo = PreferencesUtils.getBoolean(this, PreferencesUtils.IS_OPEN_FUCK_VIVO);
        mIsOpenMiBoy = PreferencesUtils.getBoolean(this, PreferencesUtils.IS_OPEN_MI_BOY);


        mBtnPerformMap.put("加入购物车", false);
        mBtnPerformMap.put("2899元", false);
        mBtnPerformMap.put("全息幻彩蓝", false);
        mBtnPerformMap.put("黑色", false);
        mBtnPerformMap.put("确定", false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBtnPerformMap.clear();
        mBtnPerformMap = null;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        if (rootNode == null) return;

        CharSequence packageName = accessibilityEvent.getPackageName();
//        Log.w(TAG, "packageName = " + packageName);

        // FuckVivo-自动填充安装密码
        if (mIsOpenFuckVivo) {
            if ("com.vivo.secime.service".equals(packageName)) {
                String password = PreferencesUtils.getString(getApplicationContext(), PreferencesUtils.KEY_PASSWORD);
                if (!TextUtils.isEmpty(password)) {
                    fillPassword(rootNode, password);
                }
            } else if ("com.android.packageinstaller".equals(packageName)) {
                installConfirm(rootNode);
            }
        }

        // 抢小米
        if (mIsOpenMiBoy) {
            if ("com.xiaomi.shop".contentEquals(packageName)) {
                performAction(rootNode);
            }
        }
    }

    private void performAction(AccessibilityNodeInfo rootNode) {
        List<AccessibilityNodeInfo> nodeInfoList = new ArrayList<>();

        for (Map.Entry<String, Boolean> entry : mBtnPerformMap.entrySet()) {
            nodeInfoList.addAll(rootNode.findAccessibilityNodeInfosByText(entry.getKey()));
        }

        for (final AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            String btnTextStr = nodeInfo.getText().toString();
            Boolean isClicked = mBtnPerformMap.get(btnTextStr);
            if (isClicked == null || isClicked) {
                return;
            }
            mBtnPerformMap.put(btnTextStr, true);
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.w(TAG, "=== performAction() ==== 点击了: " + btnTextStr);
            Toast.makeText(getApplicationContext(), "点击了: " + btnTextStr, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onInterrupt() {
    }

    private void fillPassword(AccessibilityNodeInfo rootNode, String password) {
        AccessibilityNodeInfo editText = rootNode.findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
        if (editText == null) return;

        if (editText.getPackageName().equals("com.bbk.account") && editText.getClassName().equals("android.widget.EditText")) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, password);
            editText.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }

        List<AccessibilityNodeInfo> nodeInfoList = rootNode.findAccessibilityNodeInfosByText("确定");
        for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    private void installConfirm(AccessibilityNodeInfo rootNode) {
        List<AccessibilityNodeInfo> nodeInfoList = new ArrayList<>();
        nodeInfoList.addAll(rootNode.findAccessibilityNodeInfosByText("继续安装"));
        nodeInfoList.addAll(rootNode.findAccessibilityNodeInfosByText("安装"));
        nodeInfoList.addAll(rootNode.findAccessibilityNodeInfosByText("打开"));

        for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Toast.makeText(getApplicationContext(), "点击了: " + nodeInfo.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
