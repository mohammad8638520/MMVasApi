package ir.mmvas.vasapilib.retrofit;


import android.content.Context;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ir.mmvas.vasapilib.helper.Utils;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VasApiClient {



    private static final String VAS_API_BASE = "http://s1.mmvas.ir/api/vas/";

    private static Retrofit client;

    public static Retrofit client(Context context, long serviceId) {
        if(client == null) {
            Gson gson = new GsonBuilder().create();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(new VasInterceptor(context))
                    .cache(null)
                    .build();

            String baseUrl = VAS_API_BASE + serviceId + "/";
            client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return client;
    }

    private static class VasInterceptor implements Interceptor {
        private String packageName;
        private String appVersion;


        public VasInterceptor(Context context) {
            this.appVersion = Utils.getAppVersionName(context);
            this.packageName = context.getApplicationContext().getPackageName();
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Cache-Control", "no-cache")
                    .header("x-log-app", this.packageName)
                    .header("x-log-os", "android")
                    .header("x-log-app-version", this.appVersion)
                    .header("x-log-os-version", Build.VERSION.RELEASE)
                    .header("x-log-device", Build.MODEL)
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();
            return chain.proceed(request);
        }

    }


    public static enum VasPlat {
        IMI("imi"),
        PARDIS("pardis"),
        IMI_CHARKHUNE("imich-app");

        private final String name;

        private VasPlat(String s) {
            this.name = s;
        }

        public String getName(){
            return this.name;
        }
    }

}
