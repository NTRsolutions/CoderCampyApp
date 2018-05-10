package com.gmonetix.codercampy.notification;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gaurav Bordoloi on 3/12/2018.
 */

public class MyFirebaseIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();

        updateTokenToServer(token);

    }

    private void updateTokenToServer(String token) {

        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Map map = new HashMap();
            map.put(auth.getCurrentUser().getUid(),token);

            FirebaseDatabase.getInstance().getReference("fcmTokens").setValue(map);
        }

    }

}
