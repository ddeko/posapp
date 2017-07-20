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
import java.util.List;

import mamabe.posappandroid.Activities.KitchenActivity;
import mamabe.posappandroid.Adapter.KitchenStatusAdapter;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.Models.OrderTableResponse;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 7/10/2017.
 */

public class KitchenStationFragment extends BaseFragment implements KitchenStatusAdapter.KitchenStatusAdapterListener{

    public static final String MENUSTATUS_CONFIRMATION_CODE = "1";
    public static final String MENUSTATUS_COOKING_CODE = "2";

    KitchenActivity activity;

    private KitchenStatusAdapter confirmAdapter, cookingAdapter;

    private RecyclerView listStatusConfirm, listStatusCooking;


    public ArrayList<OrderDetail> confirmItems, cookingItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (KitchenActivity) getActivity();

        confirmItems =  new ArrayList<>();
        cookingItems =  new ArrayList<>();

        confirmAdapter = new KitchenStatusAdapter(confirmItems, this, activity.getApplicationContext());
        cookingAdapter = new KitchenStatusAdapter(cookingItems, this, activity.getApplicationContext());

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

    @Override
    public void onItemClick(OrderDetail item, int position) {

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

}
