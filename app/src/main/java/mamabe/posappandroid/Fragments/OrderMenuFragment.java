package mamabe.posappandroid.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mamabe.posappandroid.Activities.AddOrderActivity;
import mamabe.posappandroid.Adapter.MenuCategoryAdapter;
import mamabe.posappandroid.Adapter.MenuCategoryTypeAdapter;
import mamabe.posappandroid.Adapter.OrderMenuAdapter;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.Dialogs.AddOrderDialog;
import mamabe.posappandroid.Fragments.Dialogs.UpdateOrderDialog;
import mamabe.posappandroid.Models.Menu;
import mamabe.posappandroid.Models.MenuCategory;
import mamabe.posappandroid.Models.MenuCategoryResponse;
import mamabe.posappandroid.Models.MenuResponse;
import mamabe.posappandroid.Models.OrderBody;
import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 5/27/2017.
 */

public class OrderMenuFragment extends BaseFragment implements View.OnClickListener, MenuCategoryAdapter.MenuCategoryAdapterListener
                                                            , MenuCategoryTypeAdapter.MenuTypeAdapterListener
                                                            , OrderMenuAdapter.MenuAdapterListener
                                                            , AddOrderDialog.AddOrderDialogListener
                                                            , UpdateOrderDialog.UpdateOrderDialogListener {

    AddOrderActivity activity;

    private OrderMenuAdapter adapterMenu;
    private MenuCategoryAdapter adapterCategory;
    private MenuCategoryTypeAdapter adapterType;

    private ArrayList<MenuCategory> menuCategoryList;
    private ArrayList<MenuCategory> menuTypeList;
    private ArrayList<Menu> menuList;

    private RecyclerView recyclerCategoryType;
    private RecyclerView recyclerCategory;
    private RecyclerView recyclerMenu;

    private boolean isAdditional;

    OrderBody orderBody;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (AddOrderActivity) getActivity();

        menuCategoryList = new ArrayList<>();
        menuTypeList = new ArrayList<>();
        menuList = new ArrayList<>();

        adapterCategory = new MenuCategoryAdapter(menuCategoryList, this, activity.getApplicationContext());
        adapterType = new MenuCategoryTypeAdapter(menuTypeList, this, activity.getApplicationContext());
        adapterMenu = new OrderMenuAdapter(menuList, this, activity);



        orderBody = (OrderBody) activity.getIntent().getSerializableExtra("orderBody");

    }

    private void setupActionBar() {
        AddOrderActivity mainActivity = (AddOrderActivity) getActivity();
        mainActivity.setRightIcon(R.drawable.menu);
        mainActivity.setLeftIcon(R.drawable.back);
        mainActivity.setActionBarTitle(getPageTitle());
    }

    @Override
    public void initView(View view) {
        recyclerCategoryType = (RecyclerView) view.findViewById(R.id.add_order_list_type);
        recyclerCategory = (RecyclerView) view.findViewById(R.id.add_order_list_category);
        recyclerMenu = (RecyclerView) view.findViewById(R.id.add_order_list_menu);

        recyclerCategoryType.setHasFixedSize(false);
        recyclerCategory.setHasFixedSize(false);
        recyclerMenu.setHasFixedSize(false);

        setScroll(false,false,recyclerCategory);
        setScroll(false,false,recyclerCategoryType);
        setScroll(true,true,recyclerMenu);

        adapterCategory = new MenuCategoryAdapter(menuCategoryList, this, activity.getApplicationContext());
        adapterType = new MenuCategoryTypeAdapter(menuTypeList, this, activity.getApplicationContext());
        adapterMenu = new OrderMenuAdapter(menuList, this, activity);

        recyclerCategory.setAdapter(adapterCategory);
        recyclerCategoryType.setAdapter(adapterType);
        recyclerMenu.setAdapter(adapterMenu);

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
        fetchData();
    }

    @Override
    public void updateUI() {
        setupActionBar();
        if(orderBody.getOrder_status().equalsIgnoreCase("")&&orderBody.getOrder_id()==null)
        {
            setAdditional(false);
        }
        else
        {
            setAdditional(true);
        }
    }

    @Override
    public String getPageTitle() {
        return "Menu Setting";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_add_order;
    }

    @Override
    public void onClick(View view) {


    }

    public void fetchData(){
        activity.showLoading(true);

        Call<MenuCategoryResponse> call = null;
        call = activity.api.getMenuType();

        call.enqueue(new Callback<MenuCategoryResponse>() {
            @Override
            public void onResponse(Call<MenuCategoryResponse> call, Response<MenuCategoryResponse> response) {

                if (2 == response.code() / 100) {

                    final MenuCategoryResponse menuCategoryResponse = response.body();
                    Log.d("MenuFragment", "response = " + new Gson().toJson(menuCategoryResponse));
                    menuTypeList.clear();
                    MenuCategory All = new MenuCategory();
                    All.setMenuType("All");
                    menuTypeList.add(All);

                    List<MenuCategory> listemp = menuCategoryResponse.getResult();
                    for (MenuCategory Item : listemp) {
                        menuTypeList.add(Item);
                        adapterType.notifyDataSetChanged();
                    }

                    fetchDataCategory(All);
                    fetchDataMenu("","");

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

    public void fetchDataMenu(String menuType, String categoryName){
        activity.showLoading(true);

        Call<MenuResponse> call = null;

        if(menuType.equalsIgnoreCase("All"))
        {
            call = activity.api.getMenuByAvailability("", categoryName);
        }
        else{
            call = activity.api.getMenuByAvailability(menuType, categoryName);
        }


        menuList.clear();
        call.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {

                if (2 == response.code() / 100) {

                    final MenuResponse menuResponse = response.body();
                    Log.d("MenuFragment", "response = " + new Gson().toJson(menuResponse));



                    List<Menu> listemp = menuResponse.getResult();
                    for (Menu Item : listemp) {
                        menuList.add(Item);

                    }
                    adapterMenu.notifyDataSetChanged();
                    activity.showLoading(false);


                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot fetching data.", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Fail to fetching data.", Toast.LENGTH_SHORT).show();
                Log.d("MenuFragment", t.getMessage()+t.getLocalizedMessage());
                activity.showLoading(false);
            }
        });
    }

    public void fetchDataCategory(final MenuCategory category){
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
                    All.setMenuType(category.getMenuType());
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
    }

    @Override
    public void onClickedType(int position) {
        MenuCategory category = menuTypeList.get(position);
        fetchDataCategory(category);
        adapterCategory.resetLastSelected();

        fetchDataMenu(category.getMenuType(), "");
    }

    @Override
    public void onClicked(int position) {
        MenuCategory category = menuCategoryList.get(position);

        if(category.getMenuCategoryName().equalsIgnoreCase("All"))
        {
            fetchDataMenu(category.getMenuType(), "");
        }
        else{
            fetchDataMenu(category.getMenuType(), category.getMenuCategoryName());
        }
    }

    @Override
    public void onItemClick(String menuId, int position) {
        int pos = 0;
        boolean isInCart = false;
        if(((AddOrderActivity)getActivity()).orderItemList.size()>0) {
            Toast.makeText(activity, String.valueOf(((AddOrderActivity)getActivity()).orderItemList.size()) , Toast.LENGTH_SHORT).show();
            for (OrderDetail item : ((AddOrderActivity) getActivity()).orderItemList) {
                if (item.getMenu().getMenuId().equals(menuId)) {
                    pos = ((AddOrderActivity) getActivity()).orderItemList.indexOf(item);
                    isInCart = true;
                }
            }
            if (isInCart) {
                OrderDetail orderItem = ((AddOrderActivity) getActivity()).orderItemList.get(pos);
                UpdateOrderDialog updateOrderDialog = new UpdateOrderDialog(true, true, this, orderItem, pos);
                updateOrderDialog.show(activity.getSupportFragmentManager(), null);
            } else {
                Menu menu = menuList.get(position);
                AddOrderDialog addOrderDialog = new AddOrderDialog(true, this, menu);
                addOrderDialog.show(activity.getSupportFragmentManager(), null);
            }
        }else {
            Menu menu = menuList.get(position);
            AddOrderDialog addOrderDialog = new AddOrderDialog(true, this, menu);
            addOrderDialog.show(activity.getSupportFragmentManager(), null);

        }

    }


    @Override
    public void onAddItem(Menu item, String qty, String note, String takeaway) {
        OrderDetail orderDetail =  new OrderDetail();
        orderDetail.setMenu(item);
        orderDetail.setMenuStatus("1");
        if(!isAdditional)
        {
            orderDetail.setAdditional("0");
        }
        else
        {
            orderDetail.setAdditional("1");
            orderDetail.setOrderId("1");
        }
        orderDetail.setNote(note);
        orderDetail.setQty(qty);
        orderDetail.setTakeaway(takeaway);

        activity.onAddItemToCart(orderDetail);
    }

    public void setAdditional(boolean additional){
        isAdditional = additional;
    }

    @Override
    public void onUpdateItem(OrderDetail orderItem, String qty, String note, int position, String takeaway) {
        ((AddOrderActivity)getActivity()).orderItemList.get(position).setQty(qty);
        ((AddOrderActivity)getActivity()).orderItemList.get(position).setNote(note);
        ((AddOrderActivity)getActivity()).orderItemList.get(position).setTakeaway(takeaway);
        activity.notifyCart();
    }

    @Override
    public void onItemDelete(int position) {

    }
}
