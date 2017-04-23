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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView  profilePicture;
    private RoundedImage cropCircle;

    private EditText EdUsername;
    private LinearLayout BtnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EdUsername = (EditText) findViewById(R.id.et_username);
        BtnLogin = (LinearLayout)findViewById(R.id.btn_login);

        BtnLogin.setOnClickListener(this);
        //        profilePicture = (ImageView) findViewById(R.id.profilepicture);
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.no_user);
//        cropCircle = new RoundedImage(bm);
//        profilePicture.setImageDrawable(cropCircle);
    }

    @Override
    public void onClick(View v) {
        if(v==BtnLogin)
        {
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
        }
    }
}
