package mamabe.posappandroid.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import mamabe.posappandroid.Models.Setting;
import mamabe.posappandroid.Models.SettingResponse;
import mamabe.posappandroid.Preferences.SessionManager;
import mamabe.posappandroid.R;
import mamabe.posappandroid.Views.RoundedImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private ImageView  profilePicture;
    private RoundedImage cropCircle;

    private EditText EdUsername;
    private LinearLayout BtnLogin;

    private ArrayList<Setting> listSetting;

    SessionManager sessions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessions = new SessionManager(this);
        listSetting = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        if(v==BtnLogin)
        {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            fetchData();

        }
    }

    @Override
    public void initView() {
        EdUsername = (EditText) findViewById(R.id.et_user);
        BtnLogin = (LinearLayout)findViewById(R.id.btn_login);

        BtnLogin.setOnClickListener(this);
        showLoading(false);
    }

    @Override
    public void setUICallbacks() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void updateUI() {

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

                    sessions.setSetting(listSetting.get(0));

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
}
