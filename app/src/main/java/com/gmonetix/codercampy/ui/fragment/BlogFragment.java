package com.gmonetix.codercampy.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gmonetix.codercampy.R;
import com.gmonetix.codercampy.adapter.BlogAdapter;
import com.gmonetix.codercampy.model.Blog;
import com.gmonetix.codercampy.model.Category;
import com.gmonetix.codercampy.model.Language;
import com.gmonetix.codercampy.networking.APIClient;
import com.gmonetix.codercampy.networking.APIInterface;
import com.gmonetix.codercampy.util.DesignUtil;

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
public class BlogFragment extends Fragment implements SearchView.OnQueryTextListener{

    private View rootView;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private APIInterface apiInterface;

    private List<Blog> blogList;
    private List<Blog> list;
    private BlogAdapter adapter;

    private MenuItem searchItem;
    private SearchView searchView;

    private List<Language> languageList;
    private List<Category> categoryList;
    private Integer[] selectedIndices;
    private int categoryIndex = 0;

    public BlogFragment() { }

    public static BlogFragment newInstance() {
        return new BlogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_blog, container, false);
            ButterKnife.bind(this,rootView);
            setHasOptionsMenu(true);

            apiInterface = APIClient.getClient().create(APIInterface.class);

            blogList = new ArrayList<>();
            list = new ArrayList<>();
            languageList = new ArrayList<>();
            categoryList = new ArrayList<>();
            categoryList.add(new Category(null,"All",null));

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setHasFixedSize(true);
            adapter = new BlogAdapter(getActivity());
            recyclerView.setAdapter(adapter);

            apiInterface.getAllCategories().enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                    categoryList.addAll(response.body());
                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {
                    Log.e("Error",t.getMessage());
                }
            });

            apiInterface.getAllLanguages().enqueue(new Callback<List<Language>>() {
                @Override
                public void onResponse(Call<List<Language>> call, Response<List<Language>> response) {
                    languageList.addAll(response.body());

                    selectedIndices = new Integer[languageList.size()];
                    for (int i=0; i<languageList.size(); i++) {
                        selectedIndices[i] = i;
                    }

                }

                @Override
                public void onFailure(Call<List<Language>> call, Throwable t) {
                    Log.e("Error",t.getMessage());
                }
            });

            apiInterface.getAllBlogs().enqueue(new Callback<List<Blog>>() {
                @Override
                public void onResponse(Call<List<Blog>> call, Response<List<Blog>> response) {
                    blogList.addAll(response.body());
                    list.addAll(response.body());
                    adapter.setList(list);
                }

                @Override
                public void onFailure(Call<List<Blog>> call, Throwable t) {
                    Toast.makeText(getActivity(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.all_courses_blogs_menu, menu);

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
        v2.setHintTextColor(Color.parseColor("#9e9e9e"));
        v2.setTextColor(Color.BLACK);
        v2.setHint("search...");

        searchView.setOnQueryTextListener(this);

        searchItem.setActionView(searchView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_category:

                new MaterialDialog.Builder(getActivity())
                        .title("Select Category")
                        .items(categoryList)
                        .typeface(DesignUtil.getTypeFace(getActivity()),DesignUtil.getTypeFace(getActivity()))
                        .alwaysCallSingleChoiceCallback()
                        .itemsCallbackSingleChoice(categoryIndex, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                categoryIndex = which;

                                list.clear();
                                adapter.notifyDataSetChanged();
                                list = new ArrayList<>();

                                if (categoryIndex == 0) {
                                    list.addAll(blogList);
                                } else {
                                    for (Blog b : blogList) {
                                        if (b.category.equals(categoryList.get(categoryIndex).id))
                                            list.add(b);
                                    }
                                }

                                adapter.setList(list);

                                return true;
                            }
                        })
                        .show();

                break;

            case R.id.menu_language:

                new MaterialDialog.Builder(getActivity())
                        .title("Select Languages")
                        .items(languageList)
                        .typeface(DesignUtil.getTypeFace(getActivity()),DesignUtil.getTypeFace(getActivity()))
                        .itemsCallbackMultiChoice(selectedIndices, new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                selectedIndices = which;

                                List<String> sortedList = new ArrayList<>();

                                for (Integer selectedIndice : selectedIndices) {
                                    sortedList.add(languageList.get(selectedIndice).id);
                                }

                                list.clear();
                                adapter.notifyDataSetChanged();
                                list = new ArrayList<>();

                                for (Blog b : blogList) {

                                    for (String l : sortedList) {
                                        if (b.languages.contains(l)) {
                                            list.add(b);
                                            break;
                                        }
                                    }
                                }

                                adapter.setList(list);

                                return true;
                            }
                        })
                        .positiveText("CHOOSE")
                        .show();

                break;

        }

        return super.onOptionsItemSelected(item);
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
}
