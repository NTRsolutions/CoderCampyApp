package com.gmonetix.codercampy.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gmonetix.codercampy.App;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.dialog.MoreDialog;
import com.gmonetix.codercampy.model.User;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.ui.fragment.AllCoursesFragment;
import com.gmonetix.codercampy.ui.fragment.BlogFragment;
import com.gmonetix.codercampy.ui.fragment.FavouritesFragment;
import com.gmonetix.codercampy.ui.fragment.HomeFragment;
import com.gmonetix.codercampy.ui.fragment.InstructorsFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.toolbar) Toolbar toolbar;

    MenuItem loginItem, favItem;

    private APIInterface apiInterface;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toggle.getDrawerArrowDrawable().setColor(getColor(R.color.colorPrimary));
        } else {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu navMenu = navigationView.getMenu();
        favItem = navMenu.getItem(2);
        loginItem = navMenu.getItem(5).getSubMenu().getItem(2);

        apiInterface = APIClient.getClient().create(APIInterface.class);

        if (App.getAuth().getCurrentUser() != null) {
            loginItem.setIcon(R.drawable.ic_logout);
            loginItem.setTitle("Sign Out");
            favItem.setVisible(true);

            apiInterface.getUser(App.getAuth().getCurrentUser().getUid()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    user = response.body();
                    Toast.makeText(Home.this, "done", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        } else {
            loginItem.setIcon(R.drawable.ic_login);
            loginItem.setTitle("Log In");
            favItem.setVisible(false);
        }

        apiInterface = APIClient.getClient().create(APIInterface.class);



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        getSupportFragmentManager().beginTransaction().replace(R.id.home_container,new HomeFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container,new HomeFragment()).commit();
                this.setTitle("Home");
                break;

            case R.id.nav_all_courses:
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, AllCoursesFragment.newInstance()).commit();
                this.setTitle("All Courses");
                break;

            case R.id.nav_my_fav:
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, FavouritesFragment.newInstance()).commit();
                this.setTitle("My Favourites");
                break;

            case R.id.nav_blog:
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container,new BlogFragment()).commit();
                this.setTitle("Blog");
                break;

            case R.id.nav_instructors:
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container, InstructorsFragment.newInstance()).commit();
                this.setTitle("Instructors");
                break;

            case R.id.nav_more:
                new MoreDialog(this).show();
                break;

            case R.id.nav_settings:
                startActivity(new Intent(this,SettingsActivity.class));
                break;

            case R.id.nav_logout:
                if (App.getAuth().getCurrentUser() != null) {
                    //logout
                    App.getAuth().signOut();
                } else {
                    //login
                    startActivity(new Intent(this,SignUpActivity.class));
                }
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
