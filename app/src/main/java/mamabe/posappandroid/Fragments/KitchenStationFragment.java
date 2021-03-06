package mamabe.posappandroid.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mamabe.posappandroid.Activities.KitchenActivity;
import mamabe.posappandroid.Adapter.KitchenStatusAdapter;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.Dialogs.OptionDialog;
import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.Models.OrderTableResponse;
import mamabe.posappandroid.Preferences.SessionManager;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 7/10/2017.
 */

public class KitchenStationFragment extends BaseFragment implements KitchenStatusAdapter.KitchenStatusAdapterListener,OptionDialog.OptionDialogListener{

    public static final String MENUSTATUS_NOTCONFIRM_CODE = "101";
    public static final String MENUSTATUS_CONFIRMATION_CODE = "1";
    public static final String MENUSTATUS_COOKING_CODE = "2";
    public static final String MENUSTATUS_COOKED_CODE = "3";
    public static final String MENUSTATUS_DELIVERING_CODE = "4";
    public static final String MENUSTATUS_DELIVERED_CODE = "5";

    KitchenActivity activity;

    OptionDialog optionDialog;

    private KitchenStatusAdapter confirmAdapter, cookingAdapter;

    private RecyclerView listStatusConfirm, listStatusCooking;

    SessionManager sessions;

    private HashMap<String,String> userData;


    public ArrayList<OrderDetail> confirmItems, cookingItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (KitchenActivity) getActivity();

        confirmItems =  new ArrayList<>();
        cookingItems =  new ArrayList<>();

        confirmAdapter = new KitchenStatusAdapter(confirmItems, this, activity.getApplicationContext());
        cookingAdapter = new KitchenStatusAdapter(cookingItems, this, activity.getApplicationContext());

        sessions = new SessionManager(getBaseActivity());
        userData = sessions.getUserDetails();
    }

    private void setupActionBar() {
        KitchenActivity mainActivity = (KitchenActivity) getActivity();
        mainActivity.setRightIcon(R.drawable.orderstatus);
        mainActivity.setLeftIcon(R.drawable.back);
        mainActivity.setActionBarTitle(getPageTitle());

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void initView(View view) {
        listStatusConfirm = (RecyclerView) view.findViewById(R.id.kitchen_station_confirm_list);
        listStatusCooking = (RecyclerView) view.findViewById(R.id.kitchen_station_cooking_list);

        listStatusConfirm.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listStatusConfirm.setLayoutManager(llm);
        listStatusConfirm.setAdapter(confirmAdapter);

        listStatusCooking.setHasFixedSize(false);
        LinearLayoutManager llm2 = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listStatusCooking.setLayoutManager(llm2);
        listStatusCooking.setAdapter(cookingAdapter);

    }

    @Override
    public void setUICallbacks() {
        getBaseActivity().setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                if(userData.get(SessionManager.KEY_ROLENAME).equalsIgnoreCase("admin"))
                {
                    getActivity().onBackPressed();
                }
                else{
                    activity.finish();
                }
            }

            @Override
            public void onRightIconClick() {

            }
        });
    }

    @Override
    public void updateUI() {
        setupActionBar();
        fetchData();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public String getPageTitle() {
        return "Kitchen Station";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_kitchen_station;
    }



    public void fetchData(){
        fetchConfirmation(MENUSTATUS_CONFIRMATION_CODE);
        fetchConfirmation(MENUSTATUS_COOKING_CODE);
    }

    public void fetchConfirmation(final String menu_status){
        activity.showLoading(true);

        Call<OrderTableResponse> call = null;
        call = activity.api.getOrderDetailbyStatus(menu_status);

        call.enqueue(new Callback<OrderTableResponse>() {
            @Override
            public void onResponse(Call<OrderTableResponse> call, Response<OrderTableResponse> response) {

                if (2 == response.code() / 100) {

                    final OrderTableResponse orderTableResponse = response.body();
                    Log.d("OrderResponse", "response = " + new Gson().toJson(orderTableResponse));


                    List<OrderDetail> listemp = orderTableResponse.getOrderDetails();
                    if(menu_status.equals(MENUSTATUS_CONFIRMATION_CODE)){
                        confirmItems.clear();
                        for (OrderDetail orderResponseItem : listemp) {
                            confirmItems.add(orderResponseItem);
                        }
                        confirmAdapter.notifyDataSetChanged();
                    }
                    else if(menu_status.equals(MENUSTATUS_COOKING_CODE)){
                        cookingItems.clear();
                        for (OrderDetail orderResponseItem : listemp) {
                            cookingItems.add(orderResponseItem);
                        }
                        cookingAdapter.notifyDataSetChanged();
                    }

                    activity.showLoading(false);
                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot fetching data.", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<OrderTableResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Fail to fetching data.", Toast.LENGTH_SHORT).show();
                Log.d("OrderResponse", t.getMessage()+t.getLocalizedMessage());
                activity.showLoading(false);
            }
        });

    }

    @Override
    public void onItemClick(OrderDetail item, int position) {
        optionDialog = new OptionDialog(true, this, item);
        optionDialog.setTitleAndComment("Change Status");
        optionDialog.show(activity.getSupportFragmentManager(), null);

    }

    @Override
    public void onOptionClick(String id, OrderDetail item) {
        OrderDetail orderDetail = new OrderDetail();

        orderDetail.setOrderDetailId(item.getOrderDetailId());
        orderDetail.setMenuStatus(id);

        activity.showLoading(true);
        Call<OrderTableResponse> call = null;

        call = activity.api.updateStatusMenu(orderDetail);
        call.enqueue(new Callback<OrderTableResponse>() {
            @Override
            public void onResponse(Call<OrderTableResponse> call, Response<OrderTableResponse> response) {

                if (201 == response.code()) {

                    final OrderTableResponse postResponse = response.body();

                    Log.d("KitchenStation", "response = " + new Gson().toJson(postResponse));

                    fetchData();
                    activity.waiterStationFragment.notifyWaiter();
                    activity.showLoading(false);


                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot update status", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<OrderTableResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Failed update status", Toast.LENGTH_SHORT).show();
                activity.showLoading(false);
            }

        });
    }

    public void notifyKitchen(){
        fetchData();
    }
}
