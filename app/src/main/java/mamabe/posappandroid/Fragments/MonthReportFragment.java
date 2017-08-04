package mamabe.posappandroid.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mamabe.posappandroid.Activities.ReportActivity;
import mamabe.posappandroid.Activities.ReportDetailActivity;
import mamabe.posappandroid.Adapter.ReportTransactionAdapter;
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

public class MonthReportFragment extends BaseFragment implements View.OnClickListener, ReportTransactionAdapter.ReportTransactionAdapterListener{

    ReportActivity activity;

    RecyclerView monthlyReportList;
    SearchableSpinner mspin, yspin;

    ReportTransactionAdapter adapter;

    ArrayList<Transaction> transItems;

    ArrayAdapter<String> adapterm;
    ArrayAdapter<String> adaptery;

    int mYear=0;
    int mMonth=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (ReportActivity) getActivity();

        transItems =  new ArrayList<>();
        adapter =  new ReportTransactionAdapter(transItems, this, activity.getApplicationContext());

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);

        adapterm = new ArrayAdapter<String>(activity.getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, MONTHS);



        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = thisYear; i >= 2000; i--)
        {
            years.add(String.valueOf(i));
        }

        adaptery = new ArrayAdapter<String>(activity.getApplicationContext(),
                android.R.layout.simple_spinner_item, years);





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
        monthlyReportList = (RecyclerView) view.findViewById(R.id.report_month_list);
        mspin = (SearchableSpinner) view.findViewById(R.id.report_month_spinner);
        yspin = (SearchableSpinner) view.findViewById(R.id.report_year_spinner);

        monthlyReportList.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        monthlyReportList.setLayoutManager(llm);
        monthlyReportList.setAdapter(adapter);

        mspin.setAdapter(adapterm);

        mspin.setTitle("Choose Month");
        mspin.setPositiveButton("OK");

        mspin.setSelection(mMonth);

        yspin.setAdapter(adaptery);

        yspin.setTitle("Choose Year");
        yspin.setPositiveButton("OK");

        yspin.setSelection(adaptery.getPosition(String.valueOf(mYear)));

        yspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                fetchData();
            } // to close the onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                fetchData();
            } // to close the onItemSelected

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        return R.layout.fragment_report_month;
    }

    @Override
    public void onClick(View v) {

    }

    public void run() {
        setupActionBar();
    }

    private static final String[] MONTHS = new String[] {
            "Januari", "Febuari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };

    private String monthToNumber(String mon) {
        if(mon.equalsIgnoreCase("Januari"))
            return "1";
        else if(mon.equalsIgnoreCase("Febuari"))
            return "2";
        else if(mon.equalsIgnoreCase("Maret"))
            return "3";
        else if(mon.equalsIgnoreCase("April"))
            return "4";
        else if(mon.equalsIgnoreCase("Mei"))
            return "5";
        else if(mon.equalsIgnoreCase("Juni"))
            return "6";

        else if(mon.equalsIgnoreCase("Juli"))
            return "7";
        else if(mon.equalsIgnoreCase("Agustus"))
            return "8";
        else if(mon.equalsIgnoreCase("September"))
            return "9";
        else if(mon.equalsIgnoreCase("Oktober"))
            return "10";
        else if(mon.equalsIgnoreCase("November"))
            return "11";
        else if(mon.equalsIgnoreCase("Desember"))
            return "12";

        return "0";
    }

    @Override
    public void onItemClick(Transaction item, int position) {
        Intent intent = new Intent(getActivity(), ReportDetailActivity.class);
        intent.putExtra("message", item.getTransId());
        startActivity(intent);
    }

    private void fetchData(){
        activity.showLoading(true);

        Call<TransactionResponse> call = null;

        call = activity.api.getReport(monthToNumber(mspin.getSelectedItem().toString()), yspin.getSelectedItem().toString());

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
