package mamabe.posappandroid.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.Models.Setting;
import mamabe.posappandroid.Models.SettingPostResponse;
import mamabe.posappandroid.Models.SettingResponse;
import mamabe.posappandroid.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentSettingActivity extends BaseActivity implements View.OnClickListener{

    private EditText etTax;
    private EditText etService;
    private EditText etDiscount;
    private RelativeLayout btnSave;

    private ArrayList<Setting> listSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRightIcon(R.drawable.paymentsetting);
        setLeftIcon(R.drawable.back);
        setActionBarTitle("Payment Setting");

        listSetting = new ArrayList<>();

    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        showLoading(false);

        etTax = (EditText) findViewById(R.id.et_tax);
        etDiscount = (EditText)findViewById(R.id.et_discount);
        etService = (EditText) findViewById(R.id.et_service);
        btnSave = (RelativeLayout) findViewById(R.id.btn_save_setting);

        btnSave.setOnClickListener(this);
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
        return R.layout.activity_payment_setting;
    }

    @Override
    public void updateUI() {
        fetchData();

    }

    @Override
    public void onClick(View view) {

        if(view == btnSave){
            updateData();
        }
    }

    public void fetchData(){
        showLoading(true);
        listSetting.clear();
        Call<SettingResponse> call = null;
        call = api.getSetting();

        call.enqueue(new Callback<SettingResponse>() {
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {

                if (2 == response.code() / 100) {

                    final SettingResponse settingResponse = response.body();
                    Log.d("UserFragment", "response = " + new Gson().toJson(settingResponse));

                    listSetting = settingResponse.getSetting();

                    etService.setText(listSetting.get(0).getService());
                    etDiscount.setText(listSetting.get(0).getTables());
                    etTax.setText(listSetting.get(0).getTax());

                    showLoading(false);


                } else {
                    Toast.makeText(getApplicationContext(), "Cannot fetching data.", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail to fetching data.", Toast.LENGTH_SHORT).show();
                Log.d("UserFragment", t.getMessage()+t.getLocalizedMessage());
                showLoading(false);
            }
        });
    }

    public void updateData(){
        showLoading(true);

        Setting setting = new Setting();

        setting.setTables(etDiscount.getText().toString());
        setting.setService(etService.getText().toString());
        setting.setTax(etTax.getText().toString());

        Call<SettingPostResponse> call = null;

        call = api.updateSetting(setting);
        call.enqueue(new Callback<SettingPostResponse>() {
            @Override
            public void onResponse(Call<SettingPostResponse> call, Response<SettingPostResponse> response) {

                if (201 == response.code()) {

                    final SettingPostResponse postResponse = response.body();

                    Log.d("UserFragment", "response = " + new Gson().toJson(postResponse));

                    fetchData();
                    showLoading(false);

                } else {
                    Toast.makeText(getApplicationContext(), "Cannot update data", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<SettingPostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed update data", Toast.LENGTH_SHORT).show();
                showLoading(false);
            }

        });
    }
}
