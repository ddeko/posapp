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
public class NumberOfGuestDialog extends DialogFragment {
    private TextView cancelBtn;
    private TextView titleCaption;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    private Button button10;
    private Button button11;
    private Button button12;


    private Dialog dialog;

    private String alertTitle = "Enter number of guest";

    private boolean cancelOnTouchOutside;

    public interface NumberOfGuestDialogListener{
        void onNumberClick(String number, int position);
    }

    private NumberOfGuestDialogListener listener;
    private int tablePosition;

    public NumberOfGuestDialog(NumberOfGuestDialogListener listener) {
        super();
        this.listener = listener;
    }

    public NumberOfGuestDialog(boolean cancelOnTouchOutside, NumberOfGuestDialogListener listener) {
        this.cancelOnTouchOutside = cancelOnTouchOutside;
        this.listener = listener;
    }

    public NumberOfGuestDialog(boolean cancelOnTouchOutside, NumberOfGuestDialogListener listener, int tablePosition) {
        this.cancelOnTouchOutside = cancelOnTouchOutside;
        this.listener = listener;
        this.tablePosition = tablePosition;
    }

    public void setTitle(String title) {
        this.alertTitle = title;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_number_of_guest);
        dialog.show();

        initView();

        titleCaption.setText(alertTitle);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNumberClick(button1.getText().toString(), tablePosition);
                dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNumberClick(button2.getText().toString(), tablePosition);
                dismiss();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNumberClick(button3.getText().toString(), tablePosition);
                dismiss();
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNumberClick(button4.getText().toString(), tablePosition);
                dismiss();
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNumberClick(button5.getText().toString(), tablePosition);
                dismiss();
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNumberClick(button6.getText().toString(), tablePosition);
                dismiss();
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNumberClick(button7.getText().toString(), tablePosition);
                dismiss();
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNumberClick(button8.getText().toString(), tablePosition);
                dismiss();
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNumberClick(button9.getText().toString(), tablePosition);
                dismiss();
            }
        });
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNumberClick(button10.getText().toString(), tablePosition);
                dismiss();
            }
        });
        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNumberClick(button11.getText().toString(), tablePosition);
                dismiss();
            }
        });
        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onNumberClick(button12.getText().toString(), tablePosition);
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
        cancelBtn = (TextView)dialog.findViewById(R.id.number_of_quest_close_btn);
        titleCaption = (TextView)dialog.findViewById(R.id.number_of_quest_title);

        button1 = (Button)dialog.findViewById(R.id.number_button_1);
        button2 = (Button)dialog.findViewById(R.id.number_button_2);
        button3 = (Button)dialog.findViewById(R.id.number_button_3);
        button4 = (Button)dialog.findViewById(R.id.number_button_4);
        button5 = (Button)dialog.findViewById(R.id.number_button_5);
        button6 = (Button)dialog.findViewById(R.id.number_button_6);
        button7 = (Button)dialog.findViewById(R.id.number_button_7);
        button8 = (Button)dialog.findViewById(R.id.number_button_8);
        button9 = (Button)dialog.findViewById(R.id.number_button_9);
        button10 = (Button)dialog.findViewById(R.id.number_button_10);
        button11 = (Button)dialog.findViewById(R.id.number_button_11);
        button12 = (Button)dialog.findViewById(R.id.number_button_12);



    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
