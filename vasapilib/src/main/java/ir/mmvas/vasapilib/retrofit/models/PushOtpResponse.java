package ir.mmvas.vasapilib.retrofit.models;


import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class PushOtpResponse {
    @SerializedName("status")           private boolean status;
    @SerializedName("message")          private String message;
    @SerializedName("isCharkhuneUser")  private boolean isCharkhuneUser;
    @SerializedName("charkhuneSku")     private String charkhuneSku;

    public String getMessage() {
        return message;
    }

    public boolean isOk() {
        return status;
    }

    public boolean useCharkhunePayment() {
        return isCharkhuneUser && !TextUtils.isEmpty(charkhuneSku);
    }

    public boolean isCharkhuneUser(){
        return isCharkhuneUser;
    }

    public String getCharkhuneSku() {
        return charkhuneSku;
    }

}
