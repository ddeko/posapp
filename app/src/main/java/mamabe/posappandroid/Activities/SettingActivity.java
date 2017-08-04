package mamabe.posappandroid.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mamabe.posappandroid.Callbacks.OnActionbarListener;
import mamabe.posappandroid.R;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    RelativeLayout btnMenu, btnPayment, btnUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRightIcon(R.drawable.setting);
        setLeftIcon(R.drawable.back);
        setActionBarTitle("Setting");
    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        btnMenu = (RelativeLayout) findViewById(R.id.btn_menu);
        btnPayment = (RelativeLayout) findViewById(R.id.btn_payment);
        btnUser = (RelativeLayout) findViewById(R.id.btn_user);

        btnMenu.setOnClickListener(this);
        btnPayment.setOnClickListener(this);
        btnUser.setOnClickListener(this);
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
        return R.layout.activity_setting;
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void onClick(View v) {
        if(v==btnUser)
        {
            Intent i = new Intent(SettingActivity.this, UserSettingActivity.class);
            startActivity(i);
        }
        else if(v ==btnPayment)
        {
            Intent i = new Intent(SettingActivity.this, PaymentSettingActivity.class);
            startActivity(i);
        }
        else if(v ==btnMenu)
        {
            Intent i = new Intent(SettingActivity.this, MenuSettingActivity.class);
            startActivity(i);
        }
    }
}
