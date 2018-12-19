package ir.mmvas.vasapilib.retrofit.listeners;


import android.text.TextUtils;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Callback;

public abstract class BaseCallback<T> implements Callback<T> {

    public String errorText(ResponseBody errorBody) {
        if(errorBody == null){
            return "";
        }
        try {
            String str = errorBody.string();
            return !TextUtils.isEmpty(str) ? str : "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
