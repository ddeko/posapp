package mamabe.posappandroid.Fragments.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 7/22/2017.
 */

@SuppressLint("ValidFragment")
public class OptionDialog extends DialogFragment{

    public static final String MENUSTATUS_NOTCONFIRM_CODE = "101";
    public static final String MENUSTATUS_CONFIRMATION_CODE = "1";
    public static final String MENUSTATUS_COOKING_CODE = "2";
    public static final String MENUSTATUS_COOKED_CODE = "3";
    public static final String MENUSTATUS_DELIVERING_CODE = "4";
    public static final String MENUSTATUS_DELIVERED_CODE = "5";

    private TextView cancelBtn;
    private TextView titleCaption;
    private TextView statusNotConfirm;
    private TextView statusConfirm;
    private TextView statusCooking;
    private TextView statusCooked;
    private TextView statusDelivering;
    private TextView statusDelivered;
    private Dialog dialog;

    private String alertTitle;
    private boolean cancelOnTouchOutside;

    public interface OptionDialogListener {
        void onOptionClick(String id, OrderDetail item);
    }

    OptionDialogListener listener;
    OrderDetail orderItem;


    public OptionDialog() {
        super();
    }

    public OptionDialog(boolean cancelOnTouchOutside, OptionDialogListener listener, OrderDetail item) {
        this.cancelOnTouchOutside = cancelOnTouchOutside;
        this.listener = listener;
        this.orderItem = item;
    }

    public void setTitleAndComment(String title) {
        this.alertTitle = title;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_option_layout);
        dialog.show();

        initView();

        titleCaption.setText(alertTitle);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        statusNotConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onOptionClick(MENUSTATUS_NOTCONFIRM_CODE, orderItem);
                dismiss();
            }
        });

        statusConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onOptionClick(MENUSTATUS_CONFIRMATION_CODE, orderItem);
                dismiss();
            }
        });

        statusCooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onOptionClick(MENUSTATUS_COOKING_CODE, orderItem);
                dismiss();
            }
        });

        statusCooked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onOptionClick(MENUSTATUS_COOKED_CODE, orderItem);
                dismiss();
            }
        });

        statusDelivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onOptionClick(MENUSTATUS_DELIVERING_CODE, orderItem);
                dismiss();
            }
        });

        statusDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onOptionClick(MENUSTATUS_DELIVERED_CODE, orderItem);
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
        titleCaption = (TextView)dialog.findViewById(R.id.confirm_title);
        statusNotConfirm = (TextView)dialog.findViewById(R.id.option_status_not_confirm);
        statusConfirm = (TextView)dialog.findViewById(R.id.option_status_confirm);
        statusCooking = (TextView)dialog.findViewById(R.id.option_status_cooking);
        statusCooked = (TextView)dialog.findViewById(R.id.option_status_cooked);
        statusDelivering = (TextView)dialog.findViewById(R.id.option_status_delivering);
        statusDelivered = (TextView)dialog.findViewById(R.id.option_status_delivered);

        if(orderItem.getMenuStatus().equalsIgnoreCase(MENUSTATUS_CONFIRMATION_CODE)){
            statusNotConfirm.setVisibility(View.VISIBLE);
            statusConfirm.setVisibility(View.GONE);
            statusCooking.setVisibility(View.VISIBLE);
            statusCooked.setVisibility(View.VISIBLE);
            statusDelivering.setVisibility(View.VISIBLE);
            statusDelivered.setVisibility(View.VISIBLE);
        }
        else if(orderItem.getMenuStatus().equalsIgnoreCase(MENUSTATUS_COOKING_CODE)){
            statusNotConfirm.setVisibility(View.GONE);
            statusConfirm.setVisibility(View.VISIBLE);
            statusCooking.setVisibility(View.GONE);
            statusCooked.setVisibility(View.VISIBLE);
            statusDelivering.setVisibility(View.VISIBLE);
            statusDelivered.setVisibility(View.VISIBLE);
        }
        else if(orderItem.getMenuStatus().equalsIgnoreCase(MENUSTATUS_COOKED_CODE)){
            statusNotConfirm.setVisibility(View.GONE);
            statusConfirm.setVisibility(View.VISIBLE);
            statusCooking.setVisibility(View.VISIBLE);
            statusCooked.setVisibility(View.GONE);
            statusDelivering.setVisibility(View.VISIBLE);
            statusDelivered.setVisibility(View.VISIBLE);
        }
        else if(orderItem.getMenuStatus().equalsIgnoreCase(MENUSTATUS_DELIVERING_CODE)){
            statusNotConfirm.setVisibility(View.GONE);
            statusConfirm.setVisibility(View.VISIBLE);
            statusCooking.setVisibility(View.VISIBLE);
            statusCooked.setVisibility(View.VISIBLE);
            statusDelivering.setVisibility(View.GONE);
            statusDelivered.setVisibility(View.VISIBLE);
        }
        else if(orderItem.getMenuStatus().equalsIgnoreCase(MENUSTATUS_DELIVERED_CODE)){
            statusNotConfirm.setVisibility(View.GONE);
            statusConfirm.setVisibility(View.VISIBLE);
            statusCooking.setVisibility(View.VISIBLE);
            statusCooked.setVisibility(View.VISIBLE);
            statusDelivering.setVisibility(View.VISIBLE);
            statusDelivered.setVisibility(View.GONE);
        }
        else if(orderItem.getMenuStatus().equalsIgnoreCase(MENUSTATUS_NOTCONFIRM_CODE)){
            statusNotConfirm.setVisibility(View.GONE);
            statusConfirm.setVisibility(View.VISIBLE);
            statusCooking.setVisibility(View.VISIBLE);
            statusCooked.setVisibility(View.VISIBLE);
            statusDelivering.setVisibility(View.VISIBLE);
            statusDelivered.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public void dismiss() {
        super.dismiss();
    }

}
