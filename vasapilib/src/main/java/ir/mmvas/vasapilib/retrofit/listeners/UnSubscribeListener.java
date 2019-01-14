package ir.mmvas.vasapilib.retrofit.listeners;

import ir.mmvas.vasapilib.helper.VasApiHelper;
import ir.mmvas.vasapilib.retrofit.models.Res;
import retrofit2.Call;
import retrofit2.Response;


public abstract class UnSubscribeListener extends BaseCallback<Res> {

    @Override
    public void onResponse(Call<Res> call, Response<Res> response) {
        if(response == null || response.body() == null) {
            failure("");
            return;
        }
        Res res = response.body();
        if(response.isSuccessful()) {
            if(res.status) {
                success(res.message);
            } else {
                failure(res.message);
            }
        } else {
            String errorText = errorText(response.errorBody());
            failure(errorText);
        }
    }

    @Override
    public void onFailure(Call<Res> call, Throwable t) {
        failure(VasApiHelper.error_in_connection);
    }

    public abstract void success(String message);
    public abstract void failure(String message);
    
}
