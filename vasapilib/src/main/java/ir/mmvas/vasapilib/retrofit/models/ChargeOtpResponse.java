package ir.mmvas.vasapilib.retrofit.models;


import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class ChargeOtpResponse {
    @SerializedName("status")   private boolean status;
    @SerializedName("token")    private String token;
    @SerializedName("message")  private String message;

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public boolean isOk() {
        return status && !TextUtils.isEmpty(token);
    }


}
