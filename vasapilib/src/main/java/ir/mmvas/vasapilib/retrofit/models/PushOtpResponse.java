package ir.mmvas.vasapilib.retrofit.models;


import com.google.gson.annotations.SerializedName;

public class PushOtpResponse {
    @SerializedName("status")       private boolean status;
    @SerializedName("message")      private String message;


    public String getMessage() {
        return message;
    }

    public boolean isOk() {
        return status;
    }


}
