package ir.mmvas.vasapilib.retrofit.listeners;

import ir.mmvas.vasapilib.helper.VasApiHelper;
import retrofit2.Call;
import retrofit2.Response;


public abstract class UnSubscribeListener extends BaseCallback<Object> {

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        if(response == null || response.body() == null) {
            failure("");
            return;
        }
        if(response.isSuccessful()) {
            try {
                String msg = (String) response.body();
                success(msg);
            } catch (Exception e){
                success("");
            }
        } else {
            String error = errorText(response.errorBody());
            failure(error);
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {
        failure(VasApiHelper.error_in_connection);
    }

    public abstract void success(String message);
    public abstract void failure(String message);
    
}
