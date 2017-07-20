package mamabe.posappandroid.Fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mamabe.posappandroid.Activities.KitchenActivity;
import mamabe.posappandroid.Activities.MenuSettingActivity;
import mamabe.posappandroid.Activities.OrderActivity;
import mamabe.posappandroid.Adapter.ListTableAdapter;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.Dialogs.NumberOfGuestDialog;
import mamabe.posappandroid.Models.Order;
import mamabe.posappandroid.Models.OrderBody;
import mamabe.posappandroid.Models.OrderResponse;
import mamabe.posappandroid.Models.Table;
import mamabe.posappandroid.Preferences.SessionManager;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 5/2/2017.
 */

public class ListTableFragment extends BaseFragment implements View.OnClickListener, ListTableAdapter.ListTableAdapterListener, NumberOfGuestDialog.NumberOfGuestDialogListener{

    private OrderActivity activity;
    private OrderDetailFragment orderDetailFragment;

    private GridLayoutManager layoutManager;
    private RecyclerView listTable;
    private TextView tvCustomer, tvTotalTable;

    private ArrayList<Order> tableItems;
    private ArrayList<Order> listOrder;
    private ListTableAdapter adapter;

    SessionManager sessions;

    private HashMap<String,String> settingData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (OrderActivity) getActivity();

        tableItems = new ArrayList<>();
        listOrder = new ArrayList<>();
        adapter = new ListTableAdapter(tableItems, this, activity.getApplicationContext());

        sessions = new SessionManager(getBaseActivity());

    }

    private void setupActionBar() {
        OrderActivity mainActivity = (OrderActivity) getActivity();
        mainActivity.setRightIcon(R.drawable.order);
        mainActivity.setLeftIcon(R.drawable.back);
        mainActivity.setActionBarTitle(getPageTitle());

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void initView(View view) {
        listTable = (RecyclerView)view.findViewById(R.id.table_list);
        tvCustomer = (TextView)view.findViewById(R.id.tv_customer);
        tvTotalTable = (TextView)view.findViewById(R.id.tv_total_table);

        layoutManager = new GridLayoutManager(getActivity(), 2);
        listTable.setHasFixedSize(false);
        listTable.setLayoutManager(layoutManager);
        listTable.setAdapter(adapter);
        listTable.addItemDecoration(new TableItemSpacingDecoration(2, 12, false));
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
                Intent i = new Intent(activity.getApplicationContext(), KitchenActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void updateUI() {
        setupActionBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();

        updateUI();
    }

    @Override
    public String getPageTitle() {
        return "Order Station";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_list_table;
    }

    @Override
    public void onClick(View v) {

    }

    public void run() {
        setupActionBar();
        fetchData();
//        getTotalCustomer();
    }

    public void fetchData() {
        tableItems.clear();
        settingData = sessions.getSettings();
        int total = Integer.parseInt(settingData.get(SessionManager.KEY_TABLES));

        Order takeaway = new Order();
        takeaway.setTableNo("0");
        takeaway.setNumberOfCustomer("0");
        takeaway.setTakeaway("1");
        tableItems.add(takeaway);

        for(int i = 1; i<=total; i++){
            Order table = new Order();
            table.setTableNo(String.valueOf(i));
            table.setNumberOfCustomer("0");
            tableItems.add(table);
        }
        tvTotalTable.setText(String.valueOf(total +" Tabels"));


        fetchOrderData();
    }

    public void getTotalCustomer(){
        int total = 0;
        for (Order Item : tableItems) {
            total = total+Integer.parseInt(Item.getNumberOfCustomer());
            Log.d("getTotalCustomer", total + " + " + Item.getNumberOfCustomer());
        }
        tvCustomer.setText(String.valueOf(total)+" Guest");
    }

    @Override
    public void onItemClick(int position) {

        if(tableItems.get(position).getTakeaway()=="1") {
            OrderBody orderBody = new OrderBody();
            orderBody.setTable_no(tableItems.get(position).getTableNo());
            orderBody.setNumber_of_customer("1");
            orderBody.setTakeaway("1");

            orderDetailFragment = new OrderDetailFragment();

            Bundle b = new Bundle();

            if (orderBody != null) {
                b.putSerializable("orderBody", orderBody);
                Log.e("orderBody", "is valid");
            } else {
                Log.e("orderBody", "is null");
            }

            orderDetailFragment.setArguments(b);

            replaceFragment(R.id.fragment_container, orderDetailFragment, true);
        }
        else{
            if(tableItems.get(position).getOrderId()==null)
            {
                NumberOfGuestDialog numberOfGuestDialog = new NumberOfGuestDialog(true,this,position);
                numberOfGuestDialog.show(activity.getSupportFragmentManager(),null);
            }
            else{
                OrderBody orderBody = new OrderBody();
                orderBody.setTable_no(tableItems.get(position).getTableNo());
                orderBody.setNumber_of_customer(tableItems.get(position).getNumberOfCustomer());
                orderBody.setTakeaway(tableItems.get(position).getTakeaway());
                orderBody.setOrder_date(tableItems.get(position).getOrderDate());
                orderBody.setOrder_id(tableItems.get(position).getOrderId());
                orderBody.setOrder_status(tableItems.get(position).getOrderStatus());
                orderBody.setCustomer_name(tableItems.get(position).getCustomerName());

                orderDetailFragment = new OrderDetailFragment();

                Bundle b = new Bundle();

                if (orderBody != null) {
                    b.putSerializable("orderBody", orderBody);
                    Log.e("orderBody", "is valid");
                } else {
                    Log.e("orderBody", "is null");
                }

                orderDetailFragment.setArguments(b);

                replaceFragment(R.id.fragment_container, orderDetailFragment, true);
            }

        }
    }

    @Override
    public void onNumberClick(String number, int position) {

        OrderBody orderBody = new OrderBody();

        orderDetailFragment = new OrderDetailFragment();

        orderBody.setTable_no(tableItems.get(position).getTableNo());
        orderBody.setNumber_of_customer(number);
        orderBody.setTakeaway("0");

        Bundle b = new Bundle();

        if (orderBody != null) {
            b.putSerializable("orderBody", orderBody);
            Log.e("orderBody", "is valid");
        } else {
            Log.e("orderBody", "is null");
        }

        orderDetailFragment.setArguments(b);

        replaceFragment(R.id.fragment_container, orderDetailFragment, true);

    }

    public void fetchOrderData(){
        activity.showLoading(true);
        listOrder.clear();
        Call<OrderResponse> call = null;
        call = activity.api.getAllOrder();

        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {

                if (2 == response.code() / 100) {

                    final OrderResponse orderResponse = response.body();
                    Log.d("OrderResponse", "response = " + new Gson().toJson(orderResponse));

                    List<Order> listemp = orderResponse.getOrders();
                    for (Order orderResponseItem : listemp) {
                        listOrder.add(orderResponseItem);
                        for (Order table : tableItems) {
                            if(orderResponseItem.getTableNo().equals(table.getTableNo())) {
                                table.setTakeaway(orderResponseItem.getTakeaway());
                                table.setCustomerName(orderResponseItem.getCustomerName());
                                table.setNumberOfCustomer(orderResponseItem.getNumberOfCustomer());
                                table.setOrderDate(orderResponseItem.getOrderDate());
                                table.setOrderId(orderResponseItem.getOrderId());
                                table.setOrderStatus(orderResponseItem.getOrderStatus());
                                table.setOrderDetail(orderResponseItem.getOrderDetail());
                            }
                        }
                    }
                    activity.showLoading(false);
                    adapter.notifyDataSetChanged();
                    getTotalCustomer();

                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot fetching data.", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Fail to fetching data.", Toast.LENGTH_SHORT).show();
                Log.d("OrderResponse", t.getMessage()+t.getLocalizedMessage());
                activity.showLoading(false);
            }
        });
    }

    public static class TableItemSpacingDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public TableItemSpacingDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if(includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if(position < spanCount) // top edge
                    outRect.top = spacing;

                outRect.bottom = spacing; // item bottom
            }
            else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)

                if(position >= spanCount)
                    outRect.top = spacing; // item top
            }
        }
    }
}
