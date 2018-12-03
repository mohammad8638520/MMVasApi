package ir.mmvas.vasapilib.retrofit.models;


import com.google.gson.annotations.SerializedName;

public class SubscriptionResponse {
    @SerializedName("active")   private boolean active;

    public boolean isActive() {
        return active;
    }
}
