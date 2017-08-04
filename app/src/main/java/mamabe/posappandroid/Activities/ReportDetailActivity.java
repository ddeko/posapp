package mamabe.posappandroid.Activities;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import mamabe.posappandroid.Adapter.CheckoutDetailAdapter;
import mamabe.posappandroid.Application.Config;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Fragments.Dialogs.ConfirmationDialog;
import mamabe.posappandroid.Models.OrderBody;
import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.Models.OrderTableResponse;
import mamabe.posappandroid.Models.Transaction;
import mamabe.posappandroid.Models.TransactionResponse;
import mamabe.posappandroid.Preferences.SessionManager;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDetailActivity extends BaseActivity implements CheckoutDetailAdapter.CheckoutDetailAdapterListener{


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

    private SimpleDateFormat dateFormatter;
    private Calendar c;

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

    public ArrayList<OrderDetail> orderItems;

    DecimalFormatSymbols symbols;
    DecimalFormat decimalFormat;

    Transaction transaction;

    Intent intent;

    SessionManager sessions;
    private HashMap<String,String> settingData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRightIcon(R.drawable.order);
        setLeftIcon(R.drawable.back);
        setActionBarTitle("Report Detail");


        sessions = new SessionManager(this);

        transaction = new Transaction();

//        settingData = sessions.getSettings();


        intent = getIntent();



        c = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat(Config.DATE_FORMAT_LONG, Locale.US);

        getDate();

        orderItems =  new ArrayList<>();
        adapter = new CheckoutDetailAdapter(orderItems, this, getApplicationContext());

    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        listOrderItem = (RecyclerView) findViewById(R.id.checkout_list);
        tvGuestName = (TextView)findViewById(R.id.checkout_customer_name);
        tvTableNumber = (TextView)findViewById(R.id.checkout_table_number);
        tvTotalGuest = (TextView)findViewById(R.id.checkout_total_customer);
        tvOrderDate = (TextView)findViewById(R.id.checkout_date);
        tvTotalItem = (TextView)findViewById(R.id.checkout_total_item);
        tvSubTotal = (TextView)findViewById(R.id.checkout_sub_total);
        tvServiceInfo = (TextView)findViewById(R.id.textView38);
//        tvDiscountInfo = (TextView)view.findViewById(R.id.textView8);
        tvTaxInfo = (TextView)findViewById(R.id.textView39);
        tvService = (TextView)findViewById(R.id.checkout_service);
//        tvDiscount = (TextView)view.findViewById(R.id.checkout_discount);
        tvTax = (TextView)findViewById(R.id.checkout_tax);
        tvTotal = (TextView)findViewById(R.id.checkout_total);
        tvCash = (EditText)findViewById(R.id.checkout_cash);
        tvChange = (TextView)findViewById(R.id.checkout_change);


        listOrderItem.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listOrderItem.setLayoutManager(llm);

        orderItems =  new ArrayList<>();
        adapter = new CheckoutDetailAdapter(orderItems, this, getApplicationContext());

        listOrderItem.setAdapter(adapter);

        tvServiceInfo.setText("Service Charge (5"+ "%) :");
        tvTaxInfo.setText("TAX (10%) :");

        symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        decimalFormat = new DecimalFormat("#,###.00", symbols);

        tvCash.setText("");
        tvChange.setText("0.00");

        tvTotalItem.setText("Total Items : 0 Items");

    }

    @Override
    public void setUICallbacks() {
        setActionbarListener(new OnActionbarListener() {
            @Override
            public void onLeftIconClick() {
                onBackPressed();
            }

            @Override
            public void onRightIconClick() {

            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.activity_report_detail;
    }

    @Override
    public void updateUI() {

        String id = intent.getStringExtra("message");

        fetchDataTransaction(id);


    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    public void getDate(){
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);



        datetime = String.valueOf(mYear)+"-"+ String.valueOf(mMonth+1) +"-"+String.valueOf(mDay) + " "
                + String.valueOf(hour) + ":" +String.valueOf(minute);


    }

    public void fetchData(String orderId){
        showLoading(true);

        Call<OrderTableResponse> call = null;
        call = api.getOrderDetailbyId(orderId);

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
                    showLoading(false);



                    subtotals = total;

                    tvTotalItem.setText("Total Item : "+String.valueOf(adapter.getItemCount())+" Items");

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot fetching data.", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<OrderTableResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail to fetching data.", Toast.LENGTH_SHORT).show();
                Log.d("OrderResponse", t.getMessage()+t.getLocalizedMessage());
                showLoading(false);
            }
        });
    }

    @Override
    public void onItemClick(OrderDetail item, int position) {

    }

    private void fetchDataTransaction(String trans_id){
        showLoading(true);

        Call<TransactionResponse> call = null;

        call = api.getTransDetail(trans_id);

        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {

                if (2 == response.code() / 100) {

                    final TransactionResponse transactionResponse = response.body();
                    Log.d("MenuFragment", "response = " + new Gson().toJson(transactionResponse));

                    transaction = transactionResponse.getTransaction().get(0);

                    adapter.notifyDataSetChanged();
                    showLoading(false);
                    setData();

                } else {
                    Toast.makeText(getApplicationContext(), "Cannot fetching data.", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail to fetching data.", Toast.LENGTH_SHORT).show();
                Log.d("MenuFragment", t.getMessage()+t.getLocalizedMessage());
                showLoading(false);
            }
        });
    }

    public void setData(){
        if(transaction.getOrderId()==null)
        {
            String formattedDate = dateFormatter.format(c.getTime());
            getDate();
            tvOrderDate.setText(datetime+ ":00");


        }
        else{
            tvGuestName.setText(transaction.getCustomerName());
            tvOrderDate.setText(transaction.getTransDate());
            tvSubTotal.setText(transaction.getSubtotal());
            tvService.setText(transaction.getService());
            tvTax.setText(transaction.getTax());
            tvTotal.setText(transaction.getTotal());
            tvCash.setText(transaction.getCash());
            tvCash.setEnabled(false);
            tvChange.setText(transaction.getChange());
            fetchData(transaction.getOrderId());

        }

        if(transaction.getTakeaway().equalsIgnoreCase("1"))
        {
            tvTableNumber.setText("Take Away");
        }
        else
        {
            tvTableNumber.setText("Table " + transaction.getTableNo());
        }
        tvTotalGuest.setText(transaction.getNumberOfCustomer());

    }
}
