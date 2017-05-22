package mamabe.posappandroid.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import mamabe.posappandroid.R;
import mamabe.posappandroid.Views.RoundedImage;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private ImageView  profilePicture;
    private RoundedImage cropCircle;

    private EditText EdUsername;
    private LinearLayout BtnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        if(v==BtnLogin)
        {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
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
}
