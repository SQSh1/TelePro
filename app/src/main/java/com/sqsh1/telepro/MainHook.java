package com.sqsh1.telepro;

import android.content.Context;
import android.content.SharedPreferences;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("org.telegram.messenger")) {
            return;
        }
        XposedBridge.log("TelePro: Hooked Telegram package");

        // Get SharedPreferences to check settings
        SharedPreferences prefs = lpparam.appInfo != null ?
            lpparam.appInfo.getSharedPreferences("TeleProPrefs", Context.MODE_PRIVATE) : null;

        if (prefs == null) {
            XposedBridge.log("TelePro: Failed to get SharedPreferences");
            return;
        }

        // Hook isPremium only if enabled in settings
        if (prefs.getBoolean("premium_enabled", true)) {
            XposedHelpers.findAndHookMethod(
                "org.telegram.messenger.UserConfig",
                lpparam.classLoader,
                "isPremium",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        param.setResult(true);
                        XposedBridge.log("TelePro: Premium status enabled");
                    }
                }
            );
        }

        // Hook to hide online status
        XposedHelpers.findAndHookMethod(
            "org.telegram.messenger.MessagesController",
            lpparam.classLoader,
            "setUserOnline",
            boolean.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(null);
                }
            }
        );
    }
}
