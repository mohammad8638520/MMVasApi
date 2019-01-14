package ir.mmvas.vasapilib.retrofit.listeners;

import ir.mmvas.vasapilib.helper.VasApiHelper;
import retrofit2.Call;
import retrofit2.Response;


public abstract class UnSubscribeListener extends BaseCallback<String> {

    @Override
    public void onResponse(Call<String> call, Response<String> response) {
        if(response == null || response.body() == null) {
            failure("");
            return;
        }
        if(response.isSuccessful()) {
            success(response.body());
        } else {
            String error = errorText(response.errorBody());
            failure(error);
        }
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        failure(VasApiHelper.error_in_connection);
    }

    public abstract void success(String message);
    public abstract void failure(String message);
    
}
