package ir.mmvas.vasapilib.retrofit;


import ir.mmvas.vasapilib.retrofit.models.ChargeOtpResponse;
import ir.mmvas.vasapilib.retrofit.models.PushOtpResponse;
import ir.mmvas.vasapilib.retrofit.models.SubscriptionResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface VasApiService {


    @FormUrlEncoded
    @POST("otp/request")
    Call<PushOtpResponse> pushOtp(@Field("mobile") String mobile, @Field("campaign") String campaign);

    @FormUrlEncoded
    @POST("otp/charge")
    Call<ChargeOtpResponse> chargeOtp(@Field("mobile") String mobile, @Field("pin") String pin, @Field("campaign") String campaign);

    @FormUrlEncoded
    @POST("subscription/check")
    Call<SubscriptionResponse> isSubscribed(@Field("mobile") String mobile, @Header("token")String token);

    @FormUrlEncoded
    @POST("unsubscribe")
    Call<Object> unsubscribe(@Header("token")String token);

}