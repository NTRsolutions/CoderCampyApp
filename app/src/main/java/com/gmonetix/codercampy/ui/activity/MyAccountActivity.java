package com.gmonetix.codercampy.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.gmonetix.codercampy.App;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.model.User;
import com.gmonetix.codercampy.util.CoderCampy;
import com.gmonetix.codercampy.util.DesignUtil;
import com.gmonetix.codercampy.util.GlideOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class MyAccountActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.image) ImageView imageView;
    @BindView(R.id.name) TextView tvName;
    @BindView(R.id.email) TextView tvEmail;
    @BindView(R.id.phone) TextView tvPhone;
    @BindView(R.id.auth_provider) TextView authProvider;
    @BindView(R.id.changePass) AppCompatButton changePass;

    private User user;
    private SimpleTooltip tooltip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        DesignUtil.applyFontForToolbarTitle(this);

        user = Home.user;

        switch (user.provider) {

            case CoderCampy.AUTH_PROVIDER_EMAIL:
                authProvider.setText("Email & Password");
                break;

            case CoderCampy.AUTH_PROVIDER_GOOGLE:
                authProvider.setText("Google");
                changePass.setVisibility(View.GONE);
                break;

            case CoderCampy.AUTH_PROVIDER_FACEBOOK:
                authProvider.setText("Facebook");
                changePass.setVisibility(View.GONE);
                break;
        }

        tvName.setText(user.name);
        tvEmail.setText(user.email);
        tvPhone.setText(user.phone);
        Glide.with(this).load(user.image).apply(GlideOptions.getRequestOptions(R.drawable.ic_default_user,R.drawable.ic_default_user)).into(imageView);

        tooltip = new SimpleTooltip.Builder(MyAccountActivity.this)
                .anchorView(authProvider)
                .text("You are logged in using this Auth provider")
                .gravity(Gravity.BOTTOM)
                .textColor(Color.WHITE)
                .animated(true)
                .transparentOverlay(false)
                .build();

        /*tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(MyAccountActivity.this)
                        .title("Change Name")
                        .autoDismiss(false)
                        .backgroundColor(getResources().getColor(R.color.colorPrimary))
                        .content("Change your name.\nYour name appears on discussion and ratings page.")
                        .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                        .input("new name", null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                                final String newName = input.toString().trim();
                                if (!newName.isEmpty()) {

                                    dialog.dismiss();
                                    UserProfileChangeRequest nameUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(newName)
                                            .build();

                                    user.updateProfile(nameUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    d.dismiss();
                                                    if (task.isSuccessful()) {
                                                        tvName.setText(newName);
                                                        Toast.makeText(MyAccountActivity.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(MyAccountActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else dialog.dismiss();

                            }
                        }).show();

            }
        });*/

        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        authProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tooltip.show();
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.getAuth().getCurrentUser() != null) {

                    AuthCredential credential = EmailAuthProvider
                            .getCredential("user@example.com", "password1234");

                    App.getAuth().getCurrentUser().reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        App.getAuth().getCurrentUser().updatePassword("")
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                        }
                                                    }
                                                });

                                    }
                                }
                            });

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_account,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (tooltip.isShowing())
            tooltip.dismiss();
        else super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (tooltip.isShowing())
            tooltip.dismiss();
        super.onDestroy();
    }

}
