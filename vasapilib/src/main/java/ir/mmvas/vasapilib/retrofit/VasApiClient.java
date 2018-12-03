package ir.mmvas.vasapilib.retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VasApiClient {



    private static final String VAS_API_BASE = "http://s1.mmvas.ir/api/";

    private static Retrofit client;
    
    public static Retrofit client(VasPlat plat, long serviceId) {
        if(client == null) {
            Gson gson = new GsonBuilder().create();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(new VasInterceptor())
                    .cache(null)
                    .build();
            String baseUrl = VAS_API_BASE + plat.getName() + "/" + serviceId + "/";
            client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return client;
    }

    private static class VasInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Cache-Control", "no-cache")
                    .build();
            return chain.proceed(request);
        }
    }


    public static enum VasPlat {
        IMI("imi"),
        PARDIS("pardis");

        private VasPlat(String s) {
            name = s;
        }

        private final String name;

        public String getName(){
            return this.name;
        }
    }
}
