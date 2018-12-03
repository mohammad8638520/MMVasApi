package ir.mmvas.vasapilib.retrofit.listeners;

import ir.mmvas.vasapilib.retrofit.models.PushOtpResponse;
import retrofit2.Call;
import retrofit2.Response;


public abstract class PushOtpListener extends BaseCallback<PushOtpResponse> {

    @Override
    public void onResponse(Call<PushOtpResponse> call, Response<PushOtpResponse> response) {
        if(response == null || response.body() == null) {
            failure("empty response");
            return;
        }
        if(response.isSuccessful()) {
            PushOtpResponse res = response.body();
            if(res.isOk()){
                success(res.getMessage());
            } else {
                failure(res.getMessage());
            }
        } else {
            String error = errorText(response.errorBody());
            failure(error);
        }
    }

    @Override
    public void onFailure(Call<PushOtpResponse> call, Throwable t) {
        failure(t.getMessage());
    }

    public abstract void success(String message);
    public abstract void failure(String message);
}
