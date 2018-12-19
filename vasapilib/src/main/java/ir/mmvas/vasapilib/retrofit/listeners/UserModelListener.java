package ir.mmvas.vasapilib.retrofit.listeners;

import ir.mmvas.vasapilib.helper.VasApiHelper;
import ir.mmvas.vasapilib.retrofit.models.VasUserModel;
import retrofit2.Call;
import retrofit2.Response;


public abstract class UserModelListener extends BaseCallback<VasUserModel> {

    @Override
    public void onResponse(Call<VasUserModel> call, Response<VasUserModel> response) {
        if(response == null || response.body() == null) {
            failure("empty response");
            return;
        }
        if(response.isSuccessful()) {
            VasUserModel user = response.body();
            VasApiHelper vasHelper = VasApiHelper.getInstance();
            if(vasHelper != null) {
                vasHelper.saveUser(user);
            }
            success(user);
        } else {
            String error = errorText(response.errorBody());
            failure(error);
        }
    }

    @Override
    public void onFailure(Call<VasUserModel> call, Throwable t) {
        failure(VasApiHelper.error_in_connection);
    }

    public abstract void success(VasUserModel user);
    public abstract void failure(String message);
}
