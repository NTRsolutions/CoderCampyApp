package com.gmonetix.codercampy.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gmonetix.codercampy.App;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Response;
import com.gmonetix.codercampy.model.User;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.CoderCampy;
import com.gmonetix.codercampy.util.DesignUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import java.util.Arrays;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class SignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    @BindView(R.id.toolbar) Toolbar toolbar;

    private static final String TAG = "Login Activity";
    private static final int GOOGLE_SIGN_IN = 191;

    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> callback;

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        DesignUtil.applyFontForToolbarTitle(this);
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        apiInterface = APIClient.getClient().create(APIInterface.class);

        facebook();
        google();

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    updateUI(user);

                }
            }
        };

    }

    private void google() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_oauth2))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void facebook() {
        callbackManager = CallbackManager.Factory.create();
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                AccessToken token = loginResult.getAccessToken();
                final AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

                App.getAuth().signInWithCredential(credential)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = task.getResult().getUser();

                                    User user = new User(firebaseUser.getEmail(),firebaseUser.getDisplayName(),firebaseUser.getPhotoUrl().toString(), CoderCampy.AUTH_PROVIDER_FACEBOOK,firebaseUser.getUid());

                                    apiInterface.createUser(user).enqueue(new Callback<Response>() {
                                        @Override
                                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                            updateUI(App.getAuth().getCurrentUser());
                                            if (response.body().code.equals("success")) {
                                                Toast.makeText(SignUpActivity.this, "success", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(SignUpActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Response> call, Throwable t) {
                                            updateUI(App.getAuth().getCurrentUser());
                                            Toast.makeText(SignUpActivity.this, "failed - " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(SignUpActivity.this, "error - " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
                e.printStackTrace();
            }
        };

        LoginManager.getInstance().registerCallback(callbackManager,callback);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e(TAG, e.toString());
                // ...
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        App.getAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = task.getResult().getUser();

                            User user = new User(firebaseUser.getEmail(),firebaseUser.getDisplayName(),firebaseUser.getPhotoUrl().toString(), CoderCampy.AUTH_PROVIDER_GOOGLE,firebaseUser.getUid());

                            apiInterface.createUser(user).enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    updateUI(App.getAuth().getCurrentUser());
                                    if (response.body().code.equals("success")) {
                                        Toast.makeText(SignUpActivity.this, "success", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {
                                    updateUI(App.getAuth().getCurrentUser());
                                    Toast.makeText(SignUpActivity.this, "failed - " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(SignUpActivity.this, "error - " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(SignUpActivity.this, ""+connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        App.getAuth().removeAuthStateListener(mAuthListener);
    }

    @OnClick(R.id.loginBtn)
    void login() {
        startActivity(new Intent(this,LoginActivity.class));
    }

    @OnClick(R.id.googleBtn)
    void googleRegister() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @OnClick(R.id.fbBtn)
    void fbRegister() {
        LoginManager.getInstance().logInWithReadPermissions(SignUpActivity.this,
                Arrays.asList("email","public_profile"));
    }

    @OnClick(R.id.emailBtn)
    void registerWithEmail() {
        startActivity(new Intent(this,SignUpWithEmailActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(App.getAuth().getCurrentUser());

        App.getAuth().addAuthStateListener(mAuthListener);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startHomeActivity();
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.skip,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_skip) {
            startHomeActivity();
        }
        return super.onOptionsItemSelected(item);
    }

}