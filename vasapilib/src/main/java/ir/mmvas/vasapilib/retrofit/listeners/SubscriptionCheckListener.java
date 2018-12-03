package ir.mmvas.vasapilib.retrofit.listeners;

import ir.mmvas.vasapilib.retrofit.models.SubscriptionResponse;
import retrofit2.Call;
import retrofit2.Response;


public abstract class SubscriptionCheckListener extends BaseCallback<SubscriptionResponse> {

    @Override
    public void onResponse(Call<SubscriptionResponse> call, Response<SubscriptionResponse> response) {
        if(response == null || response.body() == null) {
            failure("empty response");
            return;
        }
        if(response.isSuccessful()) {
            SubscriptionResponse res = response.body();
            onResult(res.isActive());
        } else {
            String error = errorText(response.errorBody());
            failure(error);
        }
    }

    @Override
    public void onFailure(Call<SubscriptionResponse> call, Throwable t) {
        failure(t.getMessage());
    }

    public abstract void onResult(boolean subscribed);
    public abstract void failure(String message);
    
}
