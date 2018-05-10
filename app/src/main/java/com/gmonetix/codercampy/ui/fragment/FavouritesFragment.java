package com.gmonetix.codercampy.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.CourseAdapter;
import com.gmonetix.codercampy.model.Course;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.ui.activity.Home;
import com.gmonetix.codercampy.util.CourseItemAnimator;
import com.gmonetix.codercampy.util.GridItemDecoration;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouritesFragment extends Fragment implements SearchView.OnQueryTextListener{

    private View rootView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.empty_layout) LinearLayout emptyLayout;

    private List<Course> courseList;
    private CourseAdapter adapter;

    private APIInterface apiInterface;

    private MenuItem searchItem;
    private SearchView searchView;

    public FavouritesFragment() { }

    public static FavouritesFragment newInstance() {
        FavouritesFragment fragment = new FavouritesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_favourites, container, false);
            ButterKnife.bind(this,rootView);
            setHasOptionsMenu(true);

            apiInterface = APIClient.getClient().create(APIInterface.class);

            courseList = new ArrayList<>();
//            adapter = new CourseAdapter(getActivity(),apiInterface);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            recyclerView.setItemAnimator(new CourseItemAnimator());
            recyclerView.addItemDecoration(new GridItemDecoration(50));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);

            StringBuilder a = new StringBuilder();
            if (Home.user.favourites != null) {
                for (String s : Home.user.favourites) {
                    if (!a.toString().isEmpty())
                        a.append(",");
                    a.append(s);
                }
            }

            if (a.toString().trim().isEmpty()) {
                emptyLayout.setVisibility(View.VISIBLE);
            } else {
                apiInterface.getCourses(a.toString()).enqueue(new Callback<List<Course>>() {
                    @Override
                    public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                        if (response.body() != null) {
                            courseList.addAll(response.body());
                            adapter.setList(courseList);
                            emptyLayout.setVisibility(View.GONE);
                        } else {
                            emptyLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Course>> call, Throwable t) {
                        Log.e("Error",t.getMessage());
                        emptyLayout.setVisibility(View.VISIBLE);
                    }
                });
            }

        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        searchItem = menu.findItem(R.id.menu_search);
        searchView = new SearchView(getActivity());
        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        int searchCloseImgId = android.support.v7.appcompat.R.id.search_close_btn;
        int mag = android.support.v7.appcompat.R.id.search_src_text;
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        ImageView v1 = (ImageView) searchView.findViewById(searchCloseImgId);
        EditText v2 = (EditText) searchView.findViewById(mag);
        v.setImageResource(R.drawable.ic_search);
        v1.setImageResource(R.drawable.ic_close);
        v2.setHintTextColor(Color.BLACK);
        v2.setTextColor(Color.BLACK);
        v2.setHint("search...");

        searchView.setOnQueryTextListener(this);

        searchItem.setActionView(searchView);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            adapter.getFilter().filter("");
        } else {
            adapter.getFilter().filter(newText.toLowerCase());
        }
        return false;
    }

    public boolean onBackPressed() {
        if (searchItem!= null) {
            if (searchItem.isVisible() && !searchView.isIconified()) {
                searchView.onActionViewCollapsed();
                return true;
            }
        }
        return false;
    }

}
