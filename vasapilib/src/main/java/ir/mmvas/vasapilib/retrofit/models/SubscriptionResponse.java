package ir.mmvas.vasapilib.retrofit.models;


import com.google.gson.annotations.SerializedName;

public class SubscriptionResponse {
    @SerializedName("active")           private boolean active;
    @SerializedName("isCharkhuneUser")  private Boolean isCharkhuneUser;

    public boolean isActive() {
        return active;
    }

    public Boolean isCharkhuneUser(){
        return this.isCharkhuneUser;
    }

}
