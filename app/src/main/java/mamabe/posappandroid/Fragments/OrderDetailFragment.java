package mamabe.posappandroid.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.google.gson.Gson;
import com.google.gson.internal.Streams;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mamabe.posappandroid.Activities.AddOrderActivity;
import mamabe.posappandroid.Activities.KitchenActivity;
import mamabe.posappandroid.Activities.OrderActivity;
import mamabe.posappandroid.Adapter.OrderDetailAdapter;
import mamabe.posappandroid.Application.Config;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.Dialogs.ConfirmDialog;
import mamabe.posappandroid.Models.OrderBody;
import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.Models.OrderTableResponse;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 6/19/2017.
 */

public class OrderDetailFragment extends BaseFragment implements View.OnClickListener, OrderDetailAdapter.OrderDetailAdapterListener,ConfirmDialog.ConfirmDialogListener {

    private static final int ADDORDER_REQUEST_CODE = 2;

    public static final String MENUSTATUS_NOTCONFIRM_CODE = "101";
    private static final String DELETE_ORDER_CODE = "401";

    private OrderActivity activity;

    private CheckoutDetailFragment checkoutDetailFragment;

    private OrderDetailAdapter adapter;

    private RecyclerView listOrderItem;
    private TextView tvGuestName;
    private TextView tvTableNumber;
    private TextView tvTotalGuest;
    private TextView tvOrderDate;
    private TextView tvTotalItem;
    private TextView tvSubTotal;
    private Button btnAddOrder;
    private Button btnCheckOut;

    private SimpleDateFormat dateFormatter;
    private Calendar c;

    Bundle bundle;
    private String subTotals;

    String datetime;

    OrderBody orderBody;

    public ArrayList<OrderDetail> orderItems;

    DecimalFormatSymbols symbols;
    DecimalFormat decimalFormat;

    ConfirmDialog confirmDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (OrderActivity) getActivity();

        c = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat(Config.DATE_FORMAT_LONG, Locale.US);

        getDate();

        bundle = getArguments();

        orderBody = (OrderBody) bundle.getSerializable("orderBody");

        orderItems =  new ArrayList<>();
        adapter = new OrderDetailAdapter(orderItems, this, activity.getApplicationContext());



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
        listOrderItem = (RecyclerView) view.findViewById(R.id.order_detail_list);
        tvGuestName = (TextView)view.findViewById(R.id.order_detail_customer_name);
        tvTableNumber = (TextView)view.findViewById(R.id.order_detail_table_number);
        tvTotalGuest = (TextView)view.findViewById(R.id.order_detail_total_customer);
        tvOrderDate = (TextView)view.findViewById(R.id.order_detail_date);
        tvTotalItem = (TextView)view.findViewById(R.id.order_detail_total_item);
        tvSubTotal = (TextView)view.findViewById(R.id.order_detail_sub_total);
        btnAddOrder = (Button)view.findViewById(R.id.order_detail_btn_add);
        btnCheckOut = (Button)view.findViewById(R.id.order_detail_btn_checkout);

        btnAddOrder.setOnClickListener(this);
        btnCheckOut.setOnClickListener(this);

        listOrderItem.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listOrderItem.setLayoutManager(llm);
        listOrderItem.setAdapter(adapter);

        symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        decimalFormat = new DecimalFormat("#,###.00", symbols);

        subTotals = decimalFormat.format(0);

        tvSubTotal.setText(subTotals);
        tvTotalItem.setText("0 Items");
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

        if(orderBody.getOrder_id()==null)
        {
            String formattedDate = dateFormatter.format(c.getTime());
            getDate();
            tvOrderDate.setText(datetime+ ":00");


        }
        else{
            tvGuestName.setText(orderBody.getCustomer_name());
            tvOrderDate.setText(orderBody.getOrder_date());
//            String formattedDates = "";
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
//
//            try {
//                Toast.makeText(activity, orderBody.getOrder_id(), Toast.LENGTH_SHORT).show();
//                formattedDates = dateFormatter.format(dateFormat.parse(orderBody.getOrder_date()));
//                tvOrderDate.setText(formattedDates);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
////            log.d(activity, formattedDates +" "+ convertedDate + " " +orderBody.getOrder_date(), Toast.LENGTH_SHORT).show();
//            Log.d("OrderResponsdasdadae", formattedDates +" "  + " " +orderBody.getOrder_date() );
//            tvOrderDate.setText(formattedDates);
            datetime = orderBody.getOrder_date();
            fetchData(orderBody.getOrder_id());
        }


        if(orderBody.getTakeaway().equalsIgnoreCase("1"))
        {
            tvTableNumber.setText("Take Away");
        }
        else
        {
            tvTableNumber.setText("Table " + orderBody.getTable_no());
        }
        tvTotalGuest.setText(orderBody.getNumber_of_customer());


    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public String getPageTitle() {
        return "Order Station";
    }


    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_order_detail;
    }

    @Override
    public void onClick(View view) {

        if(view==btnAddOrder)
        {
            if(tvGuestName.getText().toString().equalsIgnoreCase(""))
            {
                Toast.makeText(activity, "Please input guest name", Toast.LENGTH_SHORT).show();
            }
            else {
                orderBody.setCustomer_name(tvGuestName.getText().toString());
                orderBody.setOrder_date(tvOrderDate.getText().toString());
                orderBody.setOrder_status("");
                Intent i = new Intent(activity.getApplicationContext(), AddOrderActivity.class);

                i.putExtra("orderBody", orderBody);
                startActivityForResult(i, ADDORDER_REQUEST_CODE);

            }
        }
        else if(view==btnCheckOut)
        {
            if(orderBody.getOrder_id()==null)
            {
                Toast.makeText(activity, "Please input order first.", Toast.LENGTH_SHORT).show();
            }else {
                checkoutDetailFragment = new CheckoutDetailFragment();

                Bundle b = new Bundle();

                if (orderBody != null) {
                    b.putSerializable("orderBody", orderBody);
                    Log.e("orderBody", "is valid");
                } else {
                    Log.e("orderBody", "is null");
                }

                checkoutDetailFragment.setArguments(b);

                replaceFragment(R.id.fragment_container, checkoutDetailFragment, true);
            }
        }

    }

    public void run() {
        setupActionBar();

    }
    public void getDate(){
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.MILLISECOND);

        String ms = String.valueOf(ss);
        String msSbtr = ms.substring(0,2);

        datetime = String.valueOf(mYear)+"-"+ String.valueOf(mMonth) +"-"+String.valueOf(mDay) + " "
                + String.valueOf(hour) + ":" +String.valueOf(minute);


    }

    public void fetchData(String orderId){
        activity.showLoading(true);

        Call<OrderTableResponse> call = null;
        call = activity.api.getOrderDetailbyId(orderId);

        call.enqueue(new Callback<OrderTableResponse>() {
            @Override
            public void onResponse(Call<OrderTableResponse> call, Response<OrderTableResponse> response) {

                if (2 == response.code() / 100) {

                    final OrderTableResponse orderTableResponse = response.body();
                    Log.d("OrderResponse", "response = " + new Gson().toJson(orderTableResponse));

                    float total= 0 ;
                    List<OrderDetail> listemp = orderTableResponse.getOrderDetails();
                    orderItems.clear();
                    for (OrderDetail orderResponseItem : listemp) {
                        orderItems.add(orderResponseItem);
                        if(orderResponseItem.getMenu().getDiscount().equalsIgnoreCase("0"))
                        {
                            float totalPrice = Integer.parseInt(orderResponseItem.getQty())*Integer.parseInt(orderResponseItem.getMenu().getPrice());
                            total = total + totalPrice;
                        }
                        else{
                            float discount = (100 - Float.parseFloat(orderResponseItem.getMenu().getDiscount()));
                            float priceDisc = (Integer.parseInt(orderResponseItem.getMenu().getPrice())*discount)/100;
                            float totalPrice = (Integer.parseInt(orderResponseItem.getQty())*priceDisc);
                            total = total + totalPrice;
                        }
                    }
                    activity.showLoading(false);

                    adapter.notifyDataSetChanged();
                    subTotals = decimalFormat.format(total);
                    tvSubTotal.setText(subTotals);
                    tvTotalItem.setText(String.valueOf(adapter.getItemCount())+" Items");
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
        if(item.getMenuStatus().equalsIgnoreCase(MENUSTATUS_NOTCONFIRM_CODE)) {
            confirmDialog = new ConfirmDialog(true, this, item.getOrderDetailId());
            confirmDialog.setButtonsCaption("No", "Yes");
            confirmDialog.setTitleAndComment("Delete Order", "Do you want to delete this order ?");
            confirmDialog.show(activity.getSupportFragmentManager(), null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADDORDER_REQUEST_CODE){
            if(resultCode!= Activity.RESULT_OK){

                return;
            }
            else{
                if(data != null) {
                    Bundle extras = data.getExtras();
                    String order_id = extras.get("order_id").toString();
                    orderBody.setOrder_id(order_id);

                    fetchData(order_id);

                }
            }
        }
    }

    @Override
    public void onYesClick() {
        OrderDetail orderDetail = new OrderDetail();

        orderDetail.setOrderDetailId(confirmDialog.getArgs());
        orderDetail.setMenuStatus(DELETE_ORDER_CODE);

        activity.showLoading(true);
        Call<OrderTableResponse> call = null;

        call = activity.api.updateStatusMenu(orderDetail);
        call.enqueue(new Callback<OrderTableResponse>() {
            @Override
            public void onResponse(Call<OrderTableResponse> call, Response<OrderTableResponse> response) {

                if (201 == response.code()) {

                    final OrderTableResponse postResponse = response.body();

                    Log.d("KitchenStation", "response = " + new Gson().toJson(postResponse));

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

    @Override
    public void onNoClick() {
        confirmDialog.dismiss();
    }
}
