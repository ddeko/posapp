package mamabe.posappandroid.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 3/7/2017.
 */

public class HomeActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout btnOrder, btnExpend, btnReport, btnSetting;
    private TextView tvName, tvRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLeftIcon(R.drawable.ic_exit_to_app_white_24dp);
        setActionBarTitleCenter("mamabe");
    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        btnExpend = (RelativeLayout) findViewById(R.id.btn_expend);
        btnOrder = (RelativeLayout) findViewById(R.id.btn_order_admin);
        btnReport = (RelativeLayout) findViewById(R.id.btn_report);
        btnSetting = (RelativeLayout) findViewById(R.id.btn_setting);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvRole = (TextView) findViewById(R.id.tv_role);

        btnExpend.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
        btnReport.setOnClickListener(this);
        btnSetting.setOnClickListener(this);

        showLoading(false);

    }

    @Override
    public void setUICallbacks() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_home_admin;
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void onClick(View v) {
        if(v==btnSetting)
        {
            Intent i = new Intent(HomeActivity.this, SettingActivity.class);
            startActivity(i);
        }
        else if(v ==btnExpend)
        {

        }
        else if(v ==btnOrder)
        {

        }
        else if(v ==btnReport)
        {

        }
    }
}
