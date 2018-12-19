package ir.mmvas.vasapilib.retrofit.listeners;

import ir.mmvas.vasapilib.helper.VasApiHelper;
import ir.mmvas.vasapilib.retrofit.models.ChargeOtpResponse;
import retrofit2.Call;
import retrofit2.Response;


public abstract class VerifyOtpListener extends BaseCallback<ChargeOtpResponse> {

    @Override
    public void onResponse(Call<ChargeOtpResponse> call, Response<ChargeOtpResponse> response) {
        if(response == null || response.body() == null) {
            failure("empty response");
            return;
        }
        if(response.isSuccessful()) {
            ChargeOtpResponse res = response.body();
            if(res.isOk()){
                String token = res.getToken();
                VasApiHelper helper = VasApiHelper.getInstance();
                if(helper != null) {
                    helper.saveToken(token);
                }
                success(token);
            } else {
                failure(res.getMessage());
            }
        } else {
            String error = errorText(response.errorBody());
            failure(error);
        }
    }

    @Override
    public void onFailure(Call<ChargeOtpResponse> call, Throwable t) {
        failure(VasApiHelper.error_in_connection);
    }

    public abstract void success(String token);
    public abstract void failure(String message);

}
