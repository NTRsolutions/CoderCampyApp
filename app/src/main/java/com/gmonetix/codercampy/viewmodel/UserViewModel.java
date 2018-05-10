package com.gmonetix.codercampy.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.gmonetix.codercampy.model.Name;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.NetworkConnectionUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gaurav Bordoloi on 4/2/2018.
 */
public class UserViewModel extends AndroidViewModel {

    private Application context;
    private APIInterface apiInterface;

    private MutableLiveData<Name> userName;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public LiveData<Name> getUserName(String uid) {
        if (userName == null) {
            userName = new MutableLiveData<>();
            loadUserName(uid);
        }
        return userName;
    }

    private void loadUserName(String uid) {

        if (NetworkConnectionUtil.isConnectedToInternet(context)) {

            apiInterface.getUserNameByUid(uid).enqueue(new Callback<Name>() {
                @Override
                public void onResponse(Call<Name> call, Response<Name> response) {
                    if (response.body() != null) {
                        userName.setValue(response.body());
                    } else {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Name> call, Throwable t) {
                    Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(context, "No Network Connection", Toast.LENGTH_SHORT).show();
        }

    }

}
