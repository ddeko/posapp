package mamabe.posappandroid.Fragments.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import mamabe.posappandroid.R;



@SuppressLint("ValidFragment")
public class ConfirmDialog extends DialogFragment {
    private TextView cancelBtn;
    private TextView confirmText;
    private TextView titleCaption;
    private Button yesBtn;
    private Button noBtn;
    private Dialog dialog;

    private String alertTitle;
    private String alertComment;
    private String leftButtonCaption;
    private String rightButtonCaption;
    private boolean cancelOnTouchOutside;

    public interface ConfirmDialogListener {
        void onYesClick();
        void onNoClick();
    }

    ConfirmDialogListener listener;

    public ConfirmDialog() {
        super();
    }

    public ConfirmDialog(boolean cancelOnTouchOutside, ConfirmDialogListener listener) {
        this.leftButtonCaption = "No";
        this.rightButtonCaption = "Yes";
        this.cancelOnTouchOutside = cancelOnTouchOutside;
        this.listener = listener;
    }

    public void setTitleAndComment(String title, String comment) {
        this.alertTitle = title;
        this.alertComment = comment;
    }

    public void setButtonsCaption(String leftButton, String rightButton) {
        this.leftButtonCaption = leftButton;
        this.rightButtonCaption = rightButton;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_layout);
        dialog.show();

        initView();

        titleCaption.setText(alertTitle);
        confirmText.setText(alertComment);
        yesBtn.setText(rightButtonCaption);
        noBtn.setText(leftButtonCaption);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onYesClick();
                dismiss();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNoClick();
                dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        cancelBtn = (TextView)dialog.findViewById(R.id.confirm_close_btn);
        confirmText = (TextView)dialog.findViewById(R.id.confirm_message_text);
        titleCaption = (TextView)dialog.findViewById(R.id.confirm_title);

        yesBtn = (Button)dialog.findViewById(R.id.confirm_yes_btn);
        noBtn = (Button)dialog.findViewById(R.id.confirm_no_btn);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }


}
