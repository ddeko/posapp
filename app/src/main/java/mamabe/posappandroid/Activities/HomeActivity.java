package mamabe.posappandroid.Activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 3/7/2017.
 */

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void setUICallbacks() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_home;
    }

    @Override
    public void updateUI() {

    }
}
