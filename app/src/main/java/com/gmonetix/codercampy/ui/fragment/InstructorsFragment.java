package com.gmonetix.codercampy.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.InstructorAdapter;
import com.gmonetix.codercampy.model.Instructor;
import com.gmonetix.codercampy.util.GridItemDecoration;
import com.gmonetix.codercampy.util.InstructorItemAnimator;
import com.gmonetix.codercampy.viewmodel.HomeViewModel;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstructorsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private View rootView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private List<Instructor> instructorList;
    private InstructorAdapter adapter;

    private MenuItem searchItem;
    private SearchView searchView;

    private HomeViewModel homeViewModel;

    public InstructorsFragment() { }

    public static InstructorsFragment newInstance() {
        InstructorsFragment fragment = new InstructorsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_instructors, container, false);
            ButterKnife.bind(this,rootView);
            setHasOptionsMenu(true);

            instructorList = new ArrayList<>();
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
            recyclerView.setItemAnimator(new InstructorItemAnimator());
            recyclerView.addItemDecoration(new GridItemDecoration(50));
            recyclerView.setHasFixedSize(true);
            adapter = new InstructorAdapter(getActivity());
            recyclerView.setAdapter(adapter);

            homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

            homeViewModel.getAllInstructors().observe(this,allInstructors->{

                instructorList.addAll(allInstructors);
                adapter.setList(instructorList);

            });

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
