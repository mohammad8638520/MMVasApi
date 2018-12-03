package ir.mmvas.vasapilib.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import ir.mmvas.vasapilib.retrofit.VasApiClient;
import ir.mmvas.vasapilib.retrofit.VasApiService;
import ir.mmvas.vasapilib.retrofit.listeners.PushOtpListener;
import ir.mmvas.vasapilib.retrofit.listeners.SubscriptionCheckListener;
import ir.mmvas.vasapilib.retrofit.listeners.VerifyOtpListener;

public class VasApiHelper {
    private static VasApiHelper instance;
    private static VasApiService vasApiService;

    private final long serviceId;
    private VasApiClient.VasPlat platform;
    private final String appVersionName;
    private SharedPreferences prefs;


    public static VasApiHelper getInstance(Context context, VasApiClient.VasPlat platform, long serviceId) {
        if(instance == null) {
            instance = new VasApiHelper(context, platform, serviceId);
        }
        return instance;
    }

    public static VasApiHelper getInstance(){
        return instance;
    }

    private VasApiHelper(Context context, VasApiClient.VasPlat platform, long serviceId) {
        prefs = context.getSharedPreferences("VasApiHelper", Context.MODE_PRIVATE);
        this.platform = platform;
        this.serviceId = serviceId;
        appVersionName = getAppVersionName(context);
    }

    public void pushOtp(String mobile, PushOtpListener pushOtpListener){
        if(TextUtils.isEmpty(mobile)) {
            return;
        }
        saveMobileNumber(mobile);
        getApiService().pushOtp(mobile, appVersionName).enqueue(pushOtpListener);
    }

    public void verifyOtp(String pin, VerifyOtpListener verifyOtpListener) {
        if (TextUtils.isEmpty(pin)) {
            return;
        }
        String mobile = getMobileNumber();
        getApiService().chargeOtp(mobile, pin, appVersionName).enqueue(verifyOtpListener);
    }

    public void checkSubscription(SubscriptionCheckListener subCheckListener){
        checkSubscription(null, null, subCheckListener);
    }

    public void checkSubscription(String mobile, String token, SubscriptionCheckListener subCheckListener) {
        if(TextUtils.isEmpty(mobile)){
            mobile = getMobileNumber();
        }
        if(TextUtils.isEmpty(token)){
            token = getToken();
        }

        getApiService().isSubscribed(mobile, token).enqueue(subCheckListener);
    }

    public String getMobileNumber() {
        return prefs.getString("mobile", null);
    }

    private void saveMobileNumber(String mob){
        prefs.edit().putString("mobile", mob).commit();
    }

    public void saveToken(String token){
        prefs.edit().putString("token", token).commit();
    }

    public String getToken(){
        return prefs.getString("token", "");
    }

    public boolean hasToken(){
        String token = getToken();
        return !TextUtils.isEmpty(token) && token.length() > 10;
    }

    public void clear(){
        this.prefs.edit().clear().apply();
    }

    private VasApiService getApiService() {
        if(vasApiService == null) {
            vasApiService = VasApiClient.client(platform, serviceId).create(VasApiService.class);
        }
        return vasApiService;
    }

    private static String getAppVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

}
