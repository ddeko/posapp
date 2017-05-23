package mamabe.posappandroid.Fragments.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import mamabe.posappandroid.R;


/**
 * Created by DedeEko on 4/17/2016.
 */
@SuppressLint("ValidFragment")
public class ChangePasswordDialog extends DialogFragment implements View.OnClickListener{

    protected Dialog dialog;

    private Context context;

    private ImageView btnDone;
    private TextView tvOldPass;
    private EditText etOldPass;
    private EditText etNewPass;
    private EditText etConfirmPass;

    private ChangePasswordListener listener;

    private String oldpass, newpass, confirmpass, idUser;

    private int State=0;


    public interface ChangePasswordListener {
        void onChangeDone(String password);
    }

    @SuppressLint("ValidFragment")
    public ChangePasswordDialog(String password, String id, Context context, ChangePasswordListener listener)
    {
        this.oldpass = password;
        this.idUser = id;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.MaterialDialogSheet);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_password_window);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        btnDone = (ImageView) dialog.findViewById(R.id.btn_Done);
        tvOldPass = (TextView) dialog.findViewById(R.id.tv_oldPass);
        etOldPass = (EditText) dialog.findViewById(R.id.et_oldPass);
        etNewPass = (EditText) dialog.findViewById(R.id.et_newPass);
        etConfirmPass = (EditText) dialog.findViewById(R.id.et_confrimPass);

        dialog.show();

        btnDone.setOnClickListener(this);


        validate();

        return dialog;
    }


    @Override
    public void onResume() {
        super.onResume();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN)
                        return true;
                    else {
                        dialog.dismiss();
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {
        if(v==btnDone)
        {
            if(State == 0){
                newpass = etNewPass.getText().toString();
                confirmpass = etConfirmPass.getText().toString();
                if(etNewPass.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(context, "Passwords cannot be empty.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (confirmpass.equals(newpass)) {
                        if (listener != null)
                            listener.onChangeDone(newpass);

                        dialog.dismiss();
                    } else {
                        Toast.makeText(context, "Passwords don't match.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else {
                if(etOldPass.getText().toString().equals(oldpass)) {

                    newpass = etNewPass.getText().toString();
                    confirmpass = etConfirmPass.getText().toString();
                    if(etNewPass.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(context, "Passwords cannot be empty.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if (confirmpass.equals(newpass)) {
                            if (listener != null)
                                listener.onChangeDone(newpass);

                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "Passwords don't match.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(context, "Old Passwords don't match.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public void validate(){
        if(idUser==null && oldpass.equalsIgnoreCase("")){

            State = 0;
            tvOldPass.setVisibility(View.GONE);
            etOldPass.setVisibility(View.GONE);
        }
        else {
            State = 1;
            tvOldPass.setVisibility(View.VISIBLE);
            etOldPass.setVisibility(View.VISIBLE);
        }
    }

}
