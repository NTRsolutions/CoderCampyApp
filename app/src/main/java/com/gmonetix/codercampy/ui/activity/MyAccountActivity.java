package com.gmonetix.codercampy.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class MyAccountActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 198;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.image) CircleImageView imageView;
    @BindView(R.id.name) TextView tvName;
    @BindView(R.id.email) TextView tvEmail;
    @BindView(R.id.phone) TextView tvPhone;
    @BindView(R.id.auth_provider) TextView authProvider;
    @BindView(R.id.changePass) AppCompatButton changePass;

    private User user;

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

        imageView.setOnClickListener(v -> {

            Log.e("TAG","Clciked");

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

        });

        tvName.setOnClickListener(v ->{

            new MaterialDialog.Builder(MyAccountActivity.this)
                    .title("Change Name")
                    .autoDismiss(false)
                    .typeface(DesignUtil.getTypeFace(MyAccountActivity.this),DesignUtil.getTypeFace(MyAccountActivity.this))
                    .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                    .input("New Name", null, (dialog, input) -> {

                        final String newName = input.toString().trim();
                        if (!newName.isEmpty()) {

                            dialog.dismiss();




                        } else {
                            Toasty.error(MyAccountActivity.this,"Name cannot be empty",Toast.LENGTH_SHORT,true).show();
                        }

                    })
                    .negativeText("CANCEL")
                    .onNegative((dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();

        });

        tvEmail.setOnClickListener(v -> {

            new MaterialDialog.Builder(MyAccountActivity.this)
                    .title("Change Email")
                    .autoDismiss(false)
                    .typeface(DesignUtil.getTypeFace(MyAccountActivity.this),DesignUtil.getTypeFace(MyAccountActivity.this))
                    .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                    .input("New Email", null, (dialog, input) -> {

                        final String newEmail = input.toString().trim();
                        if (!newEmail.isEmpty()) {

                            dialog.dismiss();




                        } else {
                            Toasty.error(MyAccountActivity.this,"Email cannot be empty",Toast.LENGTH_SHORT,true).show();
                        }

                    })
                    .negativeText("CANCEL")
                    .onNegative((dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();

        });

        tvPhone.setOnClickListener(v -> {

            new MaterialDialog.Builder(MyAccountActivity.this)
                    .title("Change Phone")
                    .autoDismiss(false)
                    .typeface(DesignUtil.getTypeFace(MyAccountActivity.this),DesignUtil.getTypeFace(MyAccountActivity.this))
                    .inputType(InputType.TYPE_CLASS_PHONE)
                    .input("New Phone", null, (dialog, input) -> {

                        final String newPhone = input.toString().trim();
                        if (!newPhone.isEmpty()) {

                            dialog.dismiss();




                        } else {
                            Toasty.error(MyAccountActivity.this,"Phone cannot be empty",Toast.LENGTH_SHORT,true).show();
                        }

                    })
                    .negativeText("CANCEL")
                    .onNegative((dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();

        });

        authProvider.setOnClickListener(v -> Toasty.success(MyAccountActivity.this,"You are logged in using this Auth provider",Toast.LENGTH_SHORT,false).show());

        changePass.setOnClickListener(v -> {
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
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
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

}
