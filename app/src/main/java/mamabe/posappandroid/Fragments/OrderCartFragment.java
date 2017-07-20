package mamabe.posappandroid.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import mamabe.posappandroid.Activities.AddOrderActivity;
import mamabe.posappandroid.Adapter.CartAdapter;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.Dialogs.ConfirmDialog;
import mamabe.posappandroid.Fragments.Dialogs.UpdateOrderDialog;
import mamabe.posappandroid.Models.MenuPostResponse;
import mamabe.posappandroid.Models.OrderBody;
import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.Models.OrderDetailBody;
import mamabe.posappandroid.Models.OrderDetailPostResponse;
import mamabe.posappandroid.Models.OrderPostResponse;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 7/3/2017.
 */

public class OrderCartFragment extends BaseFragment implements AddOrderActivity.SendCartData, CartAdapter.CartAdapterListener,
                                                               View.OnClickListener, UpdateOrderDialog.UpdateOrderDialogListener
                                                                , ConfirmDialog.ConfirmDialogListener{

    AddOrderActivity activity;

    private CartAdapter adapter;

    private ArrayList<OrderDetailBody> listCart;

    private RecyclerView recyclerCart;
    private RelativeLayout sendOrderBtn;

    OrderBody order;
    ConfirmDialog confirmDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (AddOrderActivity) getActivity();
        listCart = new ArrayList<>();

        adapter = new CartAdapter(((AddOrderActivity)getActivity()).orderItemList,this,activity);

        order = (OrderBody) activity.getIntent().getSerializableExtra("orderBody");
    }


    private void setupActionBar() {
        AddOrderActivity mainActivity = (AddOrderActivity) getActivity();
        mainActivity.setRightIcon(R.drawable.menu);
        mainActivity.setLeftIcon(R.drawable.back);
        mainActivity.setActionBarTitle(getPageTitle());
    }

    @Override
    public void initView(View view) {
        recyclerCart = (RecyclerView) view.findViewById(R.id.order_cart_list);
        sendOrderBtn = (RelativeLayout) view.findViewById(R.id.order_cart_btn_send);

        recyclerCart.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerCart.setLayoutManager(llm);

        adapter = new CartAdapter(((AddOrderActivity)getActivity()).orderItemList,this,activity);

        recyclerCart.setAdapter(adapter);


//        listCart = ((AddOrderActivity)getActivity()).orderItemList;
        adapter.notifyDataSetChanged();

        sendOrderBtn.setOnClickListener(this);
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
        adapter.notifyDataSetChanged();
        Log.d("OrderDetail", "date3 = " +order.getOrder_date());
    }

    @Override
    public String getPageTitle() {
        return "Menu Setting";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_order_cart;
    }

    @Override
    public void onAddData(ArrayList<OrderDetail> orderItemList) {

    }

    @Override
    public void onClick(View view) {
        if(view==sendOrderBtn)
        {
            if(((AddOrderActivity)getActivity()).orderItemList.size()>0) {
                confirmDialog = new ConfirmDialog(true, this);
                confirmDialog.setButtonsCaption("Cancel", "Send Order");
                confirmDialog.setTitleAndComment("Send Order", "Do you want to send this order ?");
                confirmDialog.show(activity.getSupportFragmentManager(), null);
            }
            else{
                Toast.makeText(activity, "There is no item in order cart. ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onYesClick() {
        if(order.getOrder_status().equalsIgnoreCase("")&&order.getOrder_id()==null)
        {
            order.setOrder_status("unfinished");
            Log.d("OrderDetail", "date2 = " +order.getOrder_date());

            insertOrder(order);
        }
        else
        {
            insertOrderDetail();
        }
    }

    @Override
    public void onNoClick() {
        confirmDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void notifyAdapter(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(OrderDetail item, int position) {
        OrderDetail orderItem = ((AddOrderActivity)getActivity()).orderItemList.get(position);
        UpdateOrderDialog updateOrderDialog = new UpdateOrderDialog(false, true, this, orderItem, position);
        updateOrderDialog.show(activity.getSupportFragmentManager(),null);
    }

    @Override
    public void onUpdateItem(OrderDetail orderItem, String qty, String note, int position, String takeaway) {
        ((AddOrderActivity)getActivity()).orderItemList.get(position).setQty(qty);
        ((AddOrderActivity)getActivity()).orderItemList.get(position).setNote(note);
        ((AddOrderActivity)getActivity()).orderItemList.get(position).setTakeaway(takeaway);
        notifyAdapter();
    }

    @Override
    public void onItemDelete(int position) {
        ((AddOrderActivity)getActivity()).orderItemList.remove(position);
        notifyAdapter();
    }

    public void insertData(OrderDetailBody orderDetailBodies)
    {
        activity.showLoading(true);
        Call<OrderDetailPostResponse> call = null;

        call = activity.api.postOrderDetail(orderDetailBodies);

        call.enqueue(new Callback<OrderDetailPostResponse>() {
            @Override
            public void onResponse(Call<OrderDetailPostResponse> call, Response<OrderDetailPostResponse> response) {

                if (201 == response.code()) {

                    final OrderDetailPostResponse postResponse = response.body();

                    Log.d("OrderDetail", "response = " + new Gson().toJson(postResponse));

                    activity.showLoading(false);

                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot insert data", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<OrderDetailPostResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Failed insert data", Toast.LENGTH_SHORT).show();
                activity.showLoading(false);
            }

        });
    }

    public void insertOrder(OrderBody orderBody)
    {
        activity.showLoading(true);
        Call<OrderPostResponse> call = null;

        call = activity.api.postOrder(orderBody);
        Log.d("OrderDetail", "date1 = " +orderBody.getOrder_date());
        call.enqueue(new Callback<OrderPostResponse>() {
            @Override
            public void onResponse(Call<OrderPostResponse> call, Response<OrderPostResponse> response) {

                if (201 == response.code()) {

                    final OrderPostResponse postResponse = response.body();

                    Log.d("Order", "response = " + new Gson().toJson(postResponse) + postResponse.getOrder_id());

                    order.setOrder_id(postResponse.getOrder_id());


                    activity.showLoading(false);
                    insertOrderDetail();


                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot insert data", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<OrderPostResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Failed insert data", Toast.LENGTH_SHORT).show();
                activity.showLoading(false);
            }

        });
    }

    public void insertOrderDetail(){
        for (OrderDetail Item : ((AddOrderActivity)getActivity()).orderItemList) {
            OrderDetailBody orderDetailBody = new OrderDetailBody();
            orderDetailBody.setNote(Item.getNote());
            orderDetailBody.setQty(Item.getQty());
            orderDetailBody.setAdditional(Item.getAdditional());
            orderDetailBody.setMenu_id(Item.getMenu().getMenuId());
            orderDetailBody.setMenu_status(Item.getMenuStatus());
            orderDetailBody.setOrder_id(order.getOrder_id());
            orderDetailBody.setTakeaway(Item.getTakeaway());
            insertData(orderDetailBody);
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra("order_id",order.getOrder_id());
        if (activity.getParent() == null) {
            activity.setResult(Activity.RESULT_OK, resultIntent);
        }
        else {
            activity.getParent().setResult(Activity.RESULT_OK, resultIntent);
        }
        activity.finish();
    }


}
