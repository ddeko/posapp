package mamabe.posappandroid.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mamabe.posappandroid.Activities.MenuSettingActivity;
import mamabe.posappandroid.Activities.ReportActivity;
import mamabe.posappandroid.Activities.ReportDetailActivity;
import mamabe.posappandroid.Adapter.ReportTransactionAdapter;
import mamabe.posappandroid.Application.Config;
import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Models.Transaction;
import mamabe.posappandroid.Models.TransactionResponse;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DedeEko on 5/2/2017.
 */

public class DailyReportFragment extends BaseFragment implements View.OnClickListener, ReportTransactionAdapter.ReportTransactionAdapterListener{

    ReportActivity activity;

    RecyclerView dailyReportList;
    TextView reportDate;

    ReportTransactionAdapter adapter;

    ArrayList<Transaction> transItems;

    private SimpleDateFormat dateFormatter;
    private Calendar c;

    String datetime;
    String Year;
    String Month;
    String Day;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (ReportActivity) getActivity();

        c = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat(Config.DATE_FORMAT_LONG, Locale.US);

        getDate();

        transItems = new ArrayList<>();
        adapter =  new ReportTransactionAdapter(transItems, this, activity.getApplicationContext());
    }

    private void setupActionBar() {
        ReportActivity mainActivity = (ReportActivity) getActivity();
        mainActivity.setRightIcon(R.drawable.report);
        mainActivity.setLeftIcon(R.drawable.back);
        mainActivity.setActionBarTitle(getPageTitle());

        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void initView(View view) {
        dailyReportList = (RecyclerView) view.findViewById(R.id.report_daily_list);
        reportDate = (TextView)view.findViewById(R.id.report_date);

        dailyReportList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        dailyReportList.setLayoutManager(llm);
        dailyReportList.setAdapter(adapter);


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

        String formattedDate = dateFormatter.format(c.getTime());

        getDate();

        fetchReport();
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    @Override
    public String getPageTitle() {
        return "Report";
    }

    @Override
    public int getFragmentLayout() {
        return R.layout.fragment_report_daily;
    }

    @Override
    public void onClick(View v) {

    }

    public void run() {
        setupActionBar();
//        fetchData();
    }

    public void getDate(){
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int ss = c.get(Calendar.SECOND);

        Year = String.valueOf(mYear);
        Month = String.valueOf(mMonth);
        Day = String.valueOf(mDay);

        datetime = String.valueOf(mYear)+"-"+ String.valueOf(mMonth+1) +"-"+String.valueOf(mDay) + " "
                + String.valueOf(hour) + ":" +String.valueOf(minute);


    }
    @Override
    public void onItemClick(Transaction item, int position) {
        Intent intent = new Intent(getActivity(), ReportDetailActivity.class);
        intent.putExtra("message", item.getTransId());
        startActivity(intent);
    }

    public void fetchReport(){
        activity.showLoading(true);

        Call<TransactionResponse> call = null;

        call = activity.api.getReportDaily(Day, Month, Year);

        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {

                if (2 == response.code() / 100) {

                    final TransactionResponse transactionResponse = response.body();
                    Log.d("MenuFragment", "response = " + new Gson().toJson(transactionResponse));
                    transItems.clear();
                    List<Transaction> listemp = transactionResponse.getTransaction();
                    for (Transaction Item : listemp) {
                        transItems.add(Item);
                    }
                    adapter.notifyDataSetChanged();
                    activity.showLoading(false);


                } else {
                    Toast.makeText(activity.getApplicationContext(), "Cannot fetching data.", Toast.LENGTH_SHORT).show();
                    activity.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                Toast.makeText(activity.getApplicationContext(), "Fail to fetching data.", Toast.LENGTH_SHORT).show();
                Log.d("MenuFragment", t.getMessage()+t.getLocalizedMessage());
                activity.showLoading(false);
            }
        });
    }
}
