package mamabe.posappandroid.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mamabe.posappandroid.Activities.MenuSettingActivity;
import mamabe.posappandroid.Adapter.MenuCategoryAdapter;
import mamabe.posappandroid.Adapter.MenuCategoryTypeAdapter;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Models.MenuCategory;
import mamabe.posappandroid.Models.MenuCategoryResponse;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 5/27/2017.
 */

public class MenuFragment extends BaseFragment implements View.OnClickListener, MenuCategoryAdapter.MenuCategoryAdapterListener
                                                            , MenuCategoryTypeAdapter.MenuTypeAdapterListener{

    MenuSettingActivity activity;

    private MenuCategoryAdapter adapterCategory;
    private MenuCategoryTypeAdapter adapterType;

    private ArrayList<MenuCategory> menuCategoryList;
    private ArrayList<MenuCategory> menuTypeList;

    private RecyclerView recyclerCategoryType;
    private RecyclerView recyclerCategory;
    private RecyclerView recyclerMenu;
    private RelativeLayout btnAddMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MenuSettingActivity) getActivity();

        menuCategoryList = new ArrayList<>();
        menuTypeList = new ArrayList<>();

        adapterCategory = new MenuCategoryAdapter(menuCategoryList, this, activity.getApplicationContext(), activity, this);
        adapterType = new MenuCategoryTypeAdapter(menuTypeList, this, activity.getApplicationContext(), activity, this);
    }

    private void setupActionBar() {
        MenuSettingActivity mainActivity = (MenuSettingActivity) getActivity();
        mainActivity.setRightIcon(R.drawable.menu);
        mainActivity.setLeftIcon(R.drawable.back);
        mainActivity.setActionBarTitle(getPageTitle());
    }

    @Override
    public void initView(View view) {
        btnAddMenu = (RelativeLayout) view.findViewById(R.id.btn_add_menu);
        recyclerCategoryType = (RecyclerView) view.findViewById(R.id.listCategoryType);
        recyclerCategory = (RecyclerView) view.findViewById(R.id.listCategory);
        recyclerMenu = (RecyclerView) view.findViewById(R.id.listMenu);

        recyclerCategoryType.setHasFixedSize(false);
        recyclerCategory.setHasFixedSize(false);
        recyclerMenu.setHasFixedSize(false);

        setScroll(false,false,recyclerCategory);
        setScroll(false,false,recyclerCategoryType);
        setScroll(true,true,recyclerMenu);

        adapterCategory = new MenuCategoryAdapter(menuCategoryList, this, activity.getApplicationContext(), activity, this);
        adapterType = new MenuCategoryTypeAdapter(menuTypeList, this, activity.getApplicationContext(), activity, this);

        recyclerCategory.setAdapter(adapterCategory);
        recyclerCategoryType.setAdapter(adapterType);

        btnAddMenu.setOnClickListener(this);

    }

    public void setScroll(final boolean flag, boolean orientation, RecyclerView recyclerView) {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext()){
            @Override
            public boolean canScrollVertically() {
                return flag;
            }
        };
        if(orientation==false)
        {
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
        else {
            llm.setOrientation(LinearLayoutManager.VERTICAL);
        }

        recyclerView.setLayoutManager(llm);
    }

    @Override
    public void setUICallbacks() {
        getBaseActivity().setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                getActivity().onBackPressed();
            }

            @Override
            public void onRightIconClick() {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void updateUI() {
        setupActionBar();
    }

    @Override
    public String getPageTitle() {
        return "Menu Setting";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_menu_setting;
    }

    @Override
    public void onClick(View view) {

    }

    public void fetchData(){
        activity.showLoading(true);
        menuTypeList.clear();
        Call<MenuCategoryResponse> call = null;
        call = activity.api.getMenuType();

        call.enqueue(new Callback<MenuCategoryResponse>() {
            @Override
            public void onResponse(Call<MenuCategoryResponse> call, Response<MenuCategoryResponse> response) {

                if (2 == response.code() / 100) {

                    final MenuCategoryResponse menuCategoryResponse = response.body();
                    Log.d("MenuFragment", "response = " + new Gson().toJson(menuCategoryResponse));

                    MenuCategory All = new MenuCategory();
                    All.setMenuType("All");
                    menuTypeList.add(All);

                    List<MenuCategory> listemp = menuCategoryResponse.getResult();
                    for (MenuCategory Item : listemp) {
                        menuTypeList.add(Item);
                        adapterType.notifyDataSetChanged();
                    }

                    fetchDataCategory(All);

                    activity.showLoading(false);


                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot fetching data.", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<MenuCategoryResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Fail to fetching data.", Toast.LENGTH_SHORT).show();
                Log.d("MenuFragment", t.getMessage()+t.getLocalizedMessage());
                activity.showLoading(false);
            }
        });
    }

    public void fetchDataCategory(MenuCategory category){
        activity.showLoading(true);

        Call<MenuCategoryResponse> call = null;

        if(category.getMenuType().equalsIgnoreCase("All"))
        {
            call = activity.api.getMenuCategory("");
        }
        else{
            call = activity.api.getMenuCategory(category.getMenuType());
        }

        call.enqueue(new Callback<MenuCategoryResponse>() {
            @Override
            public void onResponse(Call<MenuCategoryResponse> call, Response<MenuCategoryResponse> response) {

                if (2 == response.code() / 100) {

                    final MenuCategoryResponse menuCategoryResponse = response.body();
                    Log.d("MenuFragment", "response = " + new Gson().toJson(menuCategoryResponse));

                    menuCategoryList.clear();
                    MenuCategory All = new MenuCategory();
                    All.setMenuCategoryName("All");
                    menuCategoryList.add(All);

                    List<MenuCategory> listemp = menuCategoryResponse.getResult();
                    for (MenuCategory Item : listemp) {
                        menuCategoryList.add(Item);
                        adapterCategory.notifyDataSetChanged();
                    }

                    activity.showLoading(false);


                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot fetching data.", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<MenuCategoryResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Fail to fetching data.", Toast.LENGTH_SHORT).show();
                Log.d("MenuFragment", t.getMessage()+t.getLocalizedMessage());
                activity.showLoading(false);
            }
        });
    }

    public void run() {
        setupActionBar();
        fetchData();
    }

    @Override
    public void onClickedType(int position) {
        MenuCategory category = menuTypeList.get(position);
        fetchDataCategory(category);
        adapterCategory.resetLastSelected();
    }

    @Override
    public void onClicked(int position) {

    }
}
