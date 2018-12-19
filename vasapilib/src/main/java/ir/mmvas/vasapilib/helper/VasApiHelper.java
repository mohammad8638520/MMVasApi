package ir.mmvas.vasapilib.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import ir.mmvas.vasapilib.R;
import ir.mmvas.vasapilib.retrofit.VasApiClient;
import ir.mmvas.vasapilib.retrofit.VasApiService;
import ir.mmvas.vasapilib.retrofit.listeners.PushOtpListener;
import ir.mmvas.vasapilib.retrofit.listeners.SubscriptionCheckListener;
import ir.mmvas.vasapilib.retrofit.listeners.UserModelListener;
import ir.mmvas.vasapilib.retrofit.listeners.VerifyOtpListener;
import ir.mmvas.vasapilib.retrofit.models.VasUserModel;

public class VasApiHelper {
    private static VasApiHelper instance;
    private static VasApiService vasApiService;

    private final long serviceId;
    private VasApiClient.VasPlat platform;
    private final String appVersionName;
    private SharedPreferences prefs;

    public static String error_in_connection = "WebService Error";

    public static VasApiHelper getInstance(Context context, VasApiClient.VasPlat platform, long serviceId) {
        if(instance == null) {
            instance = new VasApiHelper(context, platform, serviceId);
        }
        return instance;
    }

    public VasApiHelper withSrvKey(String srvkey) {
        if(!TextUtils.isEmpty(srvkey)){
            this.prefs.edit().putString("srvkey", srvkey).apply();
        }
        return this;
    }


    public static VasApiHelper getInstance(){
        return instance;
    }

    private VasApiHelper(Context context, VasApiClient.VasPlat platform, long serviceId) {
        prefs = context.getSharedPreferences("VasApiHelper", Context.MODE_PRIVATE);
        this.platform = platform;
        this.serviceId = serviceId;
        appVersionName = getAppVersionName(context);
        VasApiHelper.error_in_connection = context.getString(R.string.vas_apilib_error_in_connection);
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

    public void getUserDetails(UserModelListener listener) {
        if(listener == null) return;
        String mobile = getMobileNumber();
        if(!hasToken() || TextUtils.isEmpty(mobile)) {
            listener.failure("Please Login First");
            return;
        }
        String srvkey = srvkey();
        if(TextUtils.isEmpty(srvkey)) {
            listener.failure("srv key not defined");
            return;
        }
        getApiService().getUser(mobile, srvkey, getToken()).enqueue(listener);
    }

    public void incrementUserScore(int index, int value, UserModelListener listener) {
        if(listener == null) return;
        String mobile = getMobileNumber();
        if(!hasToken() || TextUtils.isEmpty(mobile)) {
            listener.failure("Please Login First");
            return;
        }
        String srvkey = srvkey();
        if(TextUtils.isEmpty(srvkey)) {
            listener.failure("srv key not defined");
            return;
        }
        if(index < 0 || value <= 0) {
            listener.failure("'index' should be non-negative and 'value' should be positive");
            return;
        }
        getApiService().addUserScore(mobile, index, value, srvkey, getToken()).enqueue(listener);
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

    public String srvkey(){
        return this.prefs.getString("srvkey", "");
    }

    public void saveUser(VasUserModel user) {
        String u_scores = Utils.intArrayValuesToString(user.scores);
        prefs.edit()
                .putString("u_mobile", TextUtils.isEmpty(user.mobile) ? getMobileNumber() : user.mobile)
                .putInt("u_serviceId", user.serviceId)
                .putBoolean("u_subscribed", user.subscribed)
                .putString("u_scores", u_scores)
                .putString("u_token", TextUtils.isEmpty(user.token) ? getToken() : user.token)
            .apply();
    }

    public VasUserModel getCurrentUser() {
        VasUserModel user = new VasUserModel();
        user.scores = Utils.stringToIntArray(prefs.getString("u_scores", ""));
        user.serviceId = prefs.getInt("u_serviceId", (int) this.serviceId);
        user.mobile = prefs.getString("u_mobile", getMobileNumber());
        user.token = prefs.getString("u_token", getToken());
        user.subscribed = prefs.getBoolean("u_subscribed", true);
        return user;
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
