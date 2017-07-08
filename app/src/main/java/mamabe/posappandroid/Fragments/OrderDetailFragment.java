package mamabe.posappandroid.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mamabe.posappandroid.Activities.AddOrderActivity;
import mamabe.posappandroid.Activities.OrderActivity;
import mamabe.posappandroid.Application.Config;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Models.OrderBody;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 6/19/2017.
 */

public class OrderDetailFragment extends BaseFragment implements View.OnClickListener {

    private OrderActivity activity;

    private RecyclerView listOrder;
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
    private String subTotal;

    String datetime;

    OrderBody orderBody;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (OrderActivity) getActivity();

        c = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat(Config.DATE_FORMAT_LONG, Locale.US);

        getDate();

        bundle = getArguments();

        orderBody = (OrderBody) bundle.getSerializable("orderBody");

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
        listOrder = (RecyclerView) view.findViewById(R.id.order_detail_list);
        tvGuestName = (TextView)view.findViewById(R.id.order_detail_customer_name);
        tvTableNumber = (TextView)view.findViewById(R.id.order_detail_table_number);
        tvTotalGuest = (TextView)view.findViewById(R.id.order_detail_total_customer);
        tvOrderDate = (TextView)view.findViewById(R.id.order_detail_date);
        tvTotalItem = (TextView)view.findViewById(R.id.order_detail_total_item);
        tvSubTotal = (TextView)view.findViewById(R.id.order_detail_sub_total);
        btnAddOrder = (Button)view.findViewById(R.id.order_detail_btn_add);
        btnCheckOut = (Button)view.findViewById(R.id.order_detail_btn_checkout);

        btnAddOrder.setOnClickListener(this);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,###.00", symbols);
        subTotal = decimalFormat.format(00);

        tvSubTotal.setText(subTotal);
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
            tvOrderDate.setText(datetime);


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
            orderBody.setCustomer_name(tvGuestName.getText().toString());
            orderBody.setOrder_date(datetime);
            orderBody.setOrder_status("");
            Intent i = new Intent(activity.getApplicationContext(), AddOrderActivity.class);

            i.putExtra("orderBody", orderBody);
            startActivity(i);
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

        datetime = String.valueOf(mYear)+"-"+ String.valueOf(mMonth) +"-"+String.valueOf(mDay) + " "
                + String.valueOf(hour) + ":" +String.valueOf(minute) + ":" +String.valueOf(ss);
    }
}
