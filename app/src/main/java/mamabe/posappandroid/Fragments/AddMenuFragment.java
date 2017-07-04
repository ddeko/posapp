package mamabe.posappandroid.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rm.rmswitch.RMSwitch;

import mamabe.posappandroid.Activities.MenuSettingActivity;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.Pickerview.MenuCategoryPicker;
import mamabe.posappandroid.Models.Menu;
import mamabe.posappandroid.Models.MenuBody;
import mamabe.posappandroid.Models.MenuPostResponse;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 5/2/2017.
 */

public class AddMenuFragment extends BaseFragment implements View.OnClickListener{

    public static final int ADDMENUFRAGMENT_ADD_CODE = 1;
    public static final int ADDMENUFRAGMENT_UPDATE_CODE = 2;

    MenuSettingActivity activity;

    EditText menuName;
    EditText menuPrice;
    EditText menuCategory;
    EditText menuDisc;
    RMSwitch menuAvailable;
    RelativeLayout btnSave;
    TextView btnClear;
    TextView btnSaveText;

    MenuCategoryPicker menuCategoryPicker;

    String selectedCategory;
    String idCategory;

    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MenuSettingActivity) getActivity();

        menuCategoryPicker = new MenuCategoryPicker(getActivity()) {
            @Override
            protected void onPickDone(String category, String id  ) {
                selectedCategory = category;
                idCategory = id;

                menuCategory.setText(selectedCategory);
            }

            @Override
            protected String lastSelectedProvince() {
                selectedCategory = menuCategory.getText().toString();

                return selectedCategory;
            }
        };


    }

    private void setupActionBar() {
        MenuSettingActivity mainActivity = (MenuSettingActivity) getActivity();
        mainActivity.setRightIcon(R.drawable.menu);
        mainActivity.setLeftIcon(R.drawable.back);
        mainActivity.setActionBarTitle(getPageTitle());

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



    @Override
    public void initView(View view) {
        menuName = (EditText)view.findViewById(R.id.fragment_add_menu_name);
        menuPrice = (EditText)view.findViewById(R.id.fragment_add_menu_price);
        menuCategory = (EditText)view.findViewById(R.id.fragment_add_menu_category);
        menuDisc = (EditText)view.findViewById(R.id.fragment_add_menu_disc);
        menuAvailable = (RMSwitch)view.findViewById(R.id.fragment_add_menu_available);
        btnSave = (RelativeLayout)view.findViewById(R.id.btn_save_menu);
        btnClear = (TextView)view.findViewById(R.id.textView34);
        btnSaveText = (TextView)view.findViewById(R.id.textView11);

        btnSave.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        menuCategory.setOnClickListener(this);

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
    public void updateUI() {
        setupActionBar();

        bundle = getArguments();

        if(bundle.getInt("request_code")==ADDMENUFRAGMENT_ADD_CODE)
        {
            btnSaveText.setText("Add Menu");
        }
        else if (bundle.getInt("request_code")==ADDMENUFRAGMENT_UPDATE_CODE){
            btnSaveText.setText("Save Menu");

            Menu menu = (Menu) bundle.getSerializable("menu");

            if (menu != null) {
                menuName.setText(menu.getMenuName());
                menuPrice.setText(menu.getPrice());
                menuCategory.setText(menu.getMenuCategoryName());
                menuDisc.setText(menu.getDiscount());
                if(menu.getAvailability().equalsIgnoreCase("1")){
                    menuAvailable.setChecked(true);
                }
                else{
                    menuAvailable.setChecked(false);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public String getPageTitle() {
        return "Menu Setting";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_add_menu;
    }

    @Override
    public void onClick(View v) {
        if(v == btnClear)
        {
            menuDisc.setText("0");
        }
        else if(v == btnSave)
        {
            bundle = getArguments();
            MenuBody menuBody = new MenuBody();

            menuBody.setMenu_name(menuName.getText().toString());
            menuBody.setPrice(menuPrice.getText().toString());
            menuBody.setMenuCategory_name(menuCategory.getText().toString());
            menuBody.setDiscount(menuDisc.getText().toString());

            if(menuAvailable.isChecked())
            {
                menuBody.setAvailbility("1");
            }
            else{
                menuBody.setAvailbility("0");
            }
            if(bundle.getInt("request_code")==ADDMENUFRAGMENT_ADD_CODE)
            {
                insertData(menuBody);
            }
            else if (bundle.getInt("request_code")==ADDMENUFRAGMENT_UPDATE_CODE){
                Menu menu = (Menu) bundle.getSerializable("menu");

                menuBody.setMenu_id(menu.getMenuId());
                updateData(menuBody);
            }

            getActivity().onBackPressed();

        }
        else if(v == menuCategory)
        {
            menuCategoryPicker.show(v);
        }
    }

    public void run() {
//        setupActionBar();




    }

    public void insertData(final MenuBody menuBody){
        activity.showLoading(true);
        Call<MenuPostResponse> call = null;

        call = activity.api.postMenu(menuBody);
        call.enqueue(new Callback<MenuPostResponse>() {
            @Override
            public void onResponse(Call<MenuPostResponse> call, Response<MenuPostResponse> response) {



                if (201 == response.code()) {

                    final MenuPostResponse postResponse = response.body();

                    Log.d("UserFragment", "response = " + new Gson().toJson(postResponse));

                    activity.showLoading(false);

                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot insert data", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<MenuPostResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Failed insert data", Toast.LENGTH_SHORT).show();
                activity.showLoading(false);
            }

        });

    }

    public void updateData(final MenuBody menuBody){
        activity.showLoading(true);
        Call<MenuPostResponse> call = null;

        call = activity.api.updateMenu(menuBody);
        call.enqueue(new Callback<MenuPostResponse>() {
            @Override
            public void onResponse(Call<MenuPostResponse> call, Response<MenuPostResponse> response) {



                if (201 == response.code()) {

                    final MenuPostResponse postResponse = response.body();

                    Log.d("UserFragment", "response = " + new Gson().toJson(postResponse));

                    activity.showLoading(false);

                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot update data", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<MenuPostResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Failed update data", Toast.LENGTH_SHORT).show();
                activity.showLoading(false);
            }

        });

    }
}
