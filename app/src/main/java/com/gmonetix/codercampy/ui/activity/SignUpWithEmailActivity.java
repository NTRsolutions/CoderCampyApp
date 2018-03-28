package com.gmonetix.codercampy.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gmonetix.codercampy.App;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.Response;
import com.gmonetix.codercampy.model.User;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.CoderCampy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class SignUpWithEmailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.input_name) TextInputLayout ttlName;
    @BindView(R.id.input_email) TextInputLayout ttlEmail;
    @BindView(R.id.input_password) TextInputLayout ttlPassword;
    @BindView(R.id.btn_create_accnt) AppCompatButton createAccntBtn;

    private APIInterface apiInterface;

    private String name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_with_email);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        apiInterface = APIClient.getClient().create(APIInterface.class);

        createAccntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    App.getAuth().createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseUser firebaseUser = task.getResult().getUser();

                                        User user = new User(email,name,null, CoderCampy.AUTH_PROVIDER_EMAIL,firebaseUser.getUid());

                                        apiInterface.createUser(user).enqueue(new Callback<Response>() {
                                            @Override
                                            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                                if (response.body().code.equals("success")) {
                                                    Toast.makeText(SignUpWithEmailActivity.this, "success", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(SignUpWithEmailActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Response> call, Throwable t) {
                                                Toast.makeText(SignUpWithEmailActivity.this, "failed - " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        Toast.makeText(SignUpWithEmailActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });

    }

    public boolean validate() {
        boolean valid = true;

        name = ttlName.getEditText().getText().toString();
        email = ttlEmail.getEditText().getText().toString();
        password = ttlPassword.getEditText().getText().toString();

        if (name.isEmpty() || name.length() < 4) {
            ttlName.setError("This field is required");
            valid = false;
        } else {
            ttlName.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ttlName.setError("This field is required");
            valid = false;
        } else {
            ttlName.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            ttlPassword.setError("This field is required");
            valid = false;
        } else {
            ttlPassword.setError(null);
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(App.getAuth().getCurrentUser());
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
