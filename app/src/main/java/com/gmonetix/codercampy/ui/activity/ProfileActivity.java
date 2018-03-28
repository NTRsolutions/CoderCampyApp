package com.gmonetix.codercampy.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.dialog.MyProgressDialog;
import com.gmonetix.codercampy.util.GlideOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.image) ImageView imageView;
    @BindView(R.id.name) TextView tvName;
    @BindView(R.id.email) TextView tvEmail;
    @BindView(R.id.phone) TextView tvPhone;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user.getDisplayName() != null) {
            tvName.setText(user.getDisplayName());
        }

        if (user.getEmail() != null) {
            tvEmail.setText(user.getEmail());
        }

        if (user.getPhoneNumber() != null) {
            tvPhone.setText(user.getPhoneNumber());
        }

        Glide.with(this).load(user.getPhotoUrl()).apply(GlideOptions.getRequestOptions(R.drawable.ic_default_user,R.drawable.ic_default_user)).into(imageView);

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialDialog.Builder(ProfileActivity.this)
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
                                    final MyProgressDialog d = new MyProgressDialog(ProfileActivity.this,"Updating..");
                                    d.show();

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
                                                        Toast.makeText(ProfileActivity.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(ProfileActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                } else dialog.dismiss();

                            }
                        }).show();

            }
        });

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

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void changePass(View view) {

    }

}
