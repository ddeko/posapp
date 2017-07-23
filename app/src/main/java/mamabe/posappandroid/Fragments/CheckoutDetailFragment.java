package mamabe.posappandroid.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import mamabe.posappandroid.Activities.AddOrderActivity;
import mamabe.posappandroid.Activities.KitchenActivity;
import mamabe.posappandroid.Activities.OrderActivity;
import mamabe.posappandroid.Adapter.CheckoutDetailAdapter;
import mamabe.posappandroid.Adapter.OrderDetailAdapter;
import mamabe.posappandroid.Application.Config;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.Dialogs.ConfirmDialog;
import mamabe.posappandroid.Fragments.Dialogs.ConfirmationDialog;
import mamabe.posappandroid.Models.OrderBody;
import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.Models.OrderTableResponse;
import mamabe.posappandroid.Models.TransBody;
import mamabe.posappandroid.Models.TransPostResponse;
import mamabe.posappandroid.Preferences.SessionManager;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 6/19/2017.
 */

public class CheckoutDetailFragment extends BaseFragment implements View.OnClickListener, CheckoutDetailAdapter.CheckoutDetailAdapterListener,ConfirmationDialog.ConfirmationDialogListener {

    public static final String MENUSTATUS_NOTCONFIRM_CODE = "101";
    private static final String DELETE_ORDER_CODE = "401";

    private OrderActivity activity;

    private CheckoutDetailAdapter adapter;

    private RecyclerView listOrderItem;
    private TextView tvGuestName;
    private TextView tvTableNumber;
    private TextView tvTotalGuest;
    private TextView tvOrderDate;
    private TextView tvTotalItem;
    private TextView tvSubTotal;
    private TextView tvServiceInfo;
    private TextView tvTaxInfo;
    private TextView tvService;
    private TextView tvTax;
    private TextView tvTotal;
    private EditText tvCash;
    private TextView tvChange;
    private Button btnCloseOrder;
    private Button btnBill;

    private SimpleDateFormat dateFormatter;
    private Calendar c;

    Bundle bundle;
    private String subTotalString;
    private String serviceString;
    private String discountString;
    private String taxString;
    private String totalString;
    private String cashString;
    private String changeString;

    private double cash = 0;
    private double change = 0;
    private double totals = 0;
    private double tax = 0;
    private double serviceCharge = 0;
    private double subtotals = 0;

    String datetime;

    OrderBody orderBody;

    public ArrayList<OrderDetail> orderItems;

    DecimalFormatSymbols symbols;
    DecimalFormat decimalFormat;

    ConfirmationDialog confirmDialog;

    SessionManager sessions;
    private HashMap<String,String> settingData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (OrderActivity) getActivity();

        sessions = new SessionManager(getBaseActivity());

        settingData = sessions.getSettings();

        c = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat(Config.DATE_FORMAT_LONG, Locale.US);

        getDate();

        bundle = getArguments();

        orderBody = (OrderBody) bundle.getSerializable("orderBody");

        orderItems =  new ArrayList<>();
        adapter = new CheckoutDetailAdapter(orderItems, this, activity.getApplicationContext());

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
        listOrderItem = (RecyclerView) view.findViewById(R.id.checkout_list);
        tvGuestName = (TextView)view.findViewById(R.id.checkout_customer_name);
        tvTableNumber = (TextView)view.findViewById(R.id.checkout_table_number);
        tvTotalGuest = (TextView)view.findViewById(R.id.checkout_total_customer);
        tvOrderDate = (TextView)view.findViewById(R.id.checkout_date);
        tvTotalItem = (TextView)view.findViewById(R.id.checkout_total_item);
        tvSubTotal = (TextView)view.findViewById(R.id.checkout_sub_total);
        tvServiceInfo = (TextView)view.findViewById(R.id.textView38);
//        tvDiscountInfo = (TextView)view.findViewById(R.id.textView8);
        tvTaxInfo = (TextView)view.findViewById(R.id.textView39);
        tvService = (TextView)view.findViewById(R.id.checkout_service);
//        tvDiscount = (TextView)view.findViewById(R.id.checkout_discount);
        tvTax = (TextView)view.findViewById(R.id.checkout_tax);
        tvTotal = (TextView)view.findViewById(R.id.checkout_total);
        tvCash = (EditText)view.findViewById(R.id.checkout_cash);
        tvChange = (TextView)view.findViewById(R.id.checkout_change);
        btnCloseOrder = (Button)view.findViewById(R.id.checkout_btn_close_order);
        btnBill = (Button)view.findViewById(R.id.checkout_btn_bill);

        btnCloseOrder.setOnClickListener(this);
        btnBill.setOnClickListener(this);

        listOrderItem.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listOrderItem.setLayoutManager(llm);
        listOrderItem.setAdapter(adapter);

        tvServiceInfo.setText("Service Charge (" + settingData.get(SessionManager.KEY_SERVICE) + "%) :");
        tvTaxInfo.setText("TAX (" + settingData.get(SessionManager.KEY_TAX) + "%) :");

        symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        decimalFormat = new DecimalFormat("#,###.00", symbols);

        tvCash.setText("");
        tvChange.setText("0.00");

        tvTotalItem.setText("Total Items : 0 Items");

        tvCash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(tvCash.getText().toString().equalsIgnoreCase(""))
                {
                    cash=0;
                }else{
                    cash = Double.parseDouble(tvCash.getText().toString());
                }
                change = cash-totals;
                changeString = decimalFormat.format(change);
                tvChange.setText(changeString);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

        if(orderBody.getOrder_id()==null)
        {
            String formattedDate = dateFormatter.format(c.getTime());
            getDate();
            tvOrderDate.setText(datetime+ ":00");


        }
        else{
            tvGuestName.setText(orderBody.getCustomer_name());
            getDate();
            tvOrderDate.setText(datetime+ ":00");
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


        if(orderBody.getOrder_status().equalsIgnoreCase("paid"))
        {
            btnBill.setEnabled(false);
        }
        else{
            btnBill.setEnabled(true);
        }
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
        return R.layout.fragment_checkout;
    }

    @Override
    public void onClick(View view) {

        if(view==btnCloseOrder)
        {
            if(orderBody.getOrder_status().equalsIgnoreCase("unfinished")){
                Toast.makeText(activity, "Please Finish The Payment first", Toast.LENGTH_SHORT).show();
            }else{
                int count=0;
                for(OrderDetail item :orderItems){
                    if(item.getMenuStatus().equalsIgnoreCase("5"))
                    {
                        count = count+1;
                    }
                }
                if(orderItems.size()-count>0){
                    Toast.makeText(activity, String.valueOf(orderItems.size()-count) +" Items haven't complete", Toast.LENGTH_SHORT).show();
                    confirmDialog = new ConfirmationDialog(true, this, "status");
                    confirmDialog.setButtonsCaption("No", "Yes");
                    confirmDialog.setTitleAndComment("Order Warning", String.valueOf(orderItems.size()-count) +" Items haven't complete, Do you want to finish this order ?");
                    confirmDialog.show(activity.getSupportFragmentManager(), null);
                }
                else {
                    confirmDialog = new ConfirmationDialog(true, this, "close");
                    confirmDialog.setButtonsCaption("No", "Yes");
                    confirmDialog.setTitleAndComment("Finish Order", "Do you want to finish this order ?");
                    confirmDialog.show(activity.getSupportFragmentManager(), null);
                }
            }



        }
        else if(view==btnBill)
        {
            if(change<0)
            {
                tvCash.requestFocus();
                tvCash.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(tvCash, InputMethodManager.SHOW_FORCED);
            }
            else{
                confirmDialog = new ConfirmationDialog(true, this, "pay");
                confirmDialog.setButtonsCaption("No", "Yes");
                confirmDialog.setTitleAndComment("Payemnt Confirmation", "Do you want to pay this order ?");
                confirmDialog.show(activity.getSupportFragmentManager(), null);
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
        int ss = c.get(Calendar.SECOND);



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

                    double total= 0 ;
                    List<OrderDetail> listemp = orderTableResponse.getOrderDetails();
                    orderItems.clear();
                    for (OrderDetail orderResponseItem : listemp) {
                        orderItems.add(orderResponseItem);
                        if(orderResponseItem.getMenu().getDiscount().equalsIgnoreCase("0"))
                        {
                            double totalPrice = Integer.parseInt(orderResponseItem.getQty())*Integer.parseInt(orderResponseItem.getMenu().getPrice());
                            total = total + totalPrice;
                        }
                        else{
                            double discount = (100 - Float.parseFloat(orderResponseItem.getMenu().getDiscount()));
                            double priceDisc = (Integer.parseInt(orderResponseItem.getMenu().getPrice())*discount)/100;
                            double totalPrice = (Integer.parseInt(orderResponseItem.getQty())*priceDisc);
                            total = total + totalPrice;
                        }
                    }
                    activity.showLoading(false);

                    adapter.notifyDataSetChanged();

                    subtotals = total;
                    initPayment(total);

                    tvTotalItem.setText("Total Item :"+String.valueOf(adapter.getItemCount())+" Items");
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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onYesClick(String args) {
        if(args.equalsIgnoreCase("close")){
            TransBody transBody = new TransBody();

            transBody.setOrder_id(orderBody.getOrder_id());
            transBody.setOrder_status("finished");

            changeStatusOrder(transBody);

            getActivity().getFragmentManager().popBackStack();
            getActivity().getFragmentManager().popBackStack();
        }
        else if(args.equalsIgnoreCase("pay")){
            TransBody transBody = new TransBody();

            transBody.setOrder_id(orderBody.getOrder_id());
            transBody.setOrder_status("paid");
            transBody.setCash(String.valueOf(cash));
            transBody.setChange(String.valueOf(change));
            transBody.setSubtotal(String.valueOf(subtotals));
            transBody.setTax(String.valueOf(tax));
            transBody.setService(String.valueOf(serviceCharge));
            transBody.setTotal(String.valueOf(totals));
            transBody.setTrans_date(tvOrderDate.getText().toString());

            insertTransaction(transBody);
            updateUI();
        }
        else if(args.equalsIgnoreCase("status")){

            for(OrderDetail item :orderItems){
                if(!item.getMenuStatus().equalsIgnoreCase("5"))
                {
                    OrderDetail orderDetail = new OrderDetail();

                    orderDetail.setOrderDetailId(item.getOrderDetailId());
                    orderDetail.setMenuStatus("5");

                    changeStatusMenu(orderDetail);
                }
            }

            TransBody transBody = new TransBody();

            transBody.setOrder_id(orderBody.getOrder_id());
            transBody.setOrder_status("finished");

            changeStatusOrder(transBody);

            getActivity().getFragmentManager().popBackStack();
            getActivity().getFragmentManager().popBackStack();

        }
    }

    @Override
    public void onNoClick(String args) {
        confirmDialog.dismiss();
    }

    public void initPayment(double subtotal){
        if(subtotal == 0){

            tvSubTotal.setText("0.00");
            tvService.setText("0.00");
            tvTax.setText("0.00");
            tvTotal.setText("0.00");

        }
        else {
            subTotalString = decimalFormat.format(subtotal);
            tvSubTotal.setText(subTotalString);

            serviceCharge = (subtotal* Double.parseDouble(settingData.get(SessionManager.KEY_SERVICE)))/100;
            serviceString = decimalFormat.format(serviceCharge);
            tvService.setText("+ "+serviceString);

            tax = (subtotal* Double.parseDouble(settingData.get(SessionManager.KEY_TAX)))/100;
            taxString = decimalFormat.format(tax);
            tvTax.setText("+ "+taxString);

            totals = subtotal+serviceCharge+tax;
            totalString = decimalFormat.format(totals);
            tvTotal.setText(totalString);

            change = cash-totals;
            changeString = decimalFormat.format(change);
            tvChange.setText(changeString);

        }

    }

    public void insertTransaction(TransBody transBody)
    {
        activity.showLoading(true);
        Call<TransPostResponse> call = null;

        call = activity.api.insertTransaction(transBody);

        call.enqueue(new Callback<TransPostResponse>() {
            @Override
            public void onResponse(Call<TransPostResponse> call, Response<TransPostResponse> response) {

                if (201 == response.code()) {

                    final TransPostResponse postResponse = response.body();

                    Log.d("Order", "response = " + new Gson().toJson(postResponse));

                    orderBody.setOrder_status("paid");
                    activity.showLoading(false);


                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot insert data", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<TransPostResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Failed insert data", Toast.LENGTH_SHORT).show();
                activity.showLoading(false);
            }

        });
    }

    public void changeStatusOrder(TransBody transBody)
    {
        activity.showLoading(true);
        Call<TransPostResponse> call = null;

        call = activity.api.updateStatusOrder(transBody);
        Log.d("OrderDetail", "date1 = " +orderBody.getOrder_date());
        call.enqueue(new Callback<TransPostResponse>() {
            @Override
            public void onResponse(Call<TransPostResponse> call, Response<TransPostResponse> response) {

                if (201 == response.code()) {

                    final TransPostResponse postResponse = response.body();

                    Log.d("Order", "response = " + new Gson().toJson(postResponse));

                    activity.showLoading(false);


                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot insert data", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<TransPostResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Failed insert data", Toast.LENGTH_SHORT).show();
                activity.showLoading(false);
            }

        });
    }

    public void changeStatusMenu(OrderDetail orderDetail){
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
}
