package ir.mmvas.vasapilib.retrofit.models;

import com.google.gson.annotations.SerializedName;

public class VasUserModel {
    @SerializedName("mobile")           public String mobile;
    @SerializedName("serviceId")        public int serviceId;
    @SerializedName("subscribed")       public boolean subscribed;
    @SerializedName("token")            public String token;
    @SerializedName("scores")           public int[] scores;
}
