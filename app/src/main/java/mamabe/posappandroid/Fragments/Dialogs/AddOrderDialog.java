package mamabe.posappandroid.Fragments.Dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import mamabe.posappandroid.Models.Menu;
import mamabe.posappandroid.Models.OrderDetailBody;
import mamabe.posappandroid.R;


@SuppressLint("ValidFragment")
public class AddOrderDialog extends DialogFragment {
    private TextView tvFoodName;
    private EditText etQty;
    private EditText etNote;
    private ImageView btnPlus;
    private ImageView btnMin;
    private ImageView btnDelete;
    private Button btnCancel;
    private Button btnAddItem;

    private Dialog dialog;

    private boolean cancelOnTouchOutside;

    public interface AddOrderDialogListener{
        void onAddItem(Menu item, String qty, String note);
    }

    private AddOrderDialogListener listener;
    private Menu item;

    public AddOrderDialog(boolean cancelOnTouchOutside, AddOrderDialogListener listener, Menu item) {
        this.cancelOnTouchOutside = cancelOnTouchOutside;
        this.listener = listener;
        this.item = item;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_order_window);
        dialog.show();

        initView();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddItem(item,etQty.getText().toString(),etNote.getText().toString());
                dismiss();
            }
        });
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(etQty.getText().toString()) + 1;

                etQty.setText(String.valueOf(qty));
            }
        });
        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty;
                if(Integer.parseInt(etQty.getText().toString())>0) {
                    qty = Integer.parseInt(etQty.getText().toString()) - 1;

                    etQty.setText(String.valueOf(qty));
                }
            }
        });

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        tvFoodName = (TextView)dialog.findViewById(R.id.add_order_dialog_title);
        etQty = (EditText)dialog.findViewById(R.id.et_quantity);
        etNote = (EditText)dialog.findViewById(R.id.et_note);
        btnPlus = (ImageView)dialog.findViewById(R.id.btn_add);
        btnMin = (ImageView)dialog.findViewById(R.id.btn_min);
        btnDelete = (ImageView)dialog.findViewById(R.id.add_order_dialog_delete_btn);
        btnCancel = (Button)dialog.findViewById(R.id.dialog_order_cancel_btn);
        btnAddItem = (Button)dialog.findViewById(R.id.dialog_order_add_btn);

        etQty.setText("1");
        tvFoodName.setText(item.getMenuName());
        btnDelete.setVisibility(View.GONE);

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
