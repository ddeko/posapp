package mamabe.posappandroid.Adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mamabe.posappandroid.Models.OrderDetail;
import mamabe.posappandroid.Models.Transaction;
import mamabe.posappandroid.R;

/**
 * Created by DedeEko on 7/11/2017.
 */

public class ReportTransactionAdapter extends RecyclerView.Adapter<ReportTransactionAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Transaction> items;
    private ReportTransactionAdapterListener listener;

    public interface ReportTransactionAdapterListener {
        void onItemClick(Transaction item, int position);
    }

    public ReportTransactionAdapter(ArrayList<Transaction> items, ReportTransactionAdapterListener listener, Context context) {
        this.items = items;
        this.listener = listener;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvGuestName;
        public TextView tvTotalGuest;
        public TextView tvTotalItem;
        public TextView tvTransDate;
        public TextView tvTotalPaid;
        public TextView tvTakeaway;
        public View item;


        public ViewHolder(View itemView, TextView tvGuestName, TextView tvTotalGuest, TextView tvTotalItem,
                          TextView tvTransDate, TextView tvTotalPaid, TextView tvTakeaway, View item) {
            super(itemView);
            this.tvGuestName = tvGuestName;
            this.tvTotalGuest = tvTotalGuest;
            this.tvTotalItem = tvTotalItem;
            this.tvTransDate = tvTransDate;
            this.tvTotalPaid = tvTotalPaid;
            this.tvTakeaway = tvTakeaway;
            this.item = item;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_order_item, parent, false);
        TextView tvGuestName = (TextView)v.findViewById(R.id.report_detail_guest_name);
        TextView tvTotalGuest = (TextView)v.findViewById(R.id.report_detail_guest_count);
        TextView tvTotalItem = (TextView)v.findViewById(R.id.report_detail_total_item_ordered);
        TextView tvTransDate = (TextView)v.findViewById(R.id.report_detail_trans_date);
        TextView tvTotalPaid = (TextView)v.findViewById(R.id.report_detail_total_payed);
        TextView tvTakeaway = (TextView)v.findViewById(R.id.report_detail_item_takeaway);
        View item = v.findViewById(R.id.list_report_detail_item);

        tvGuestName.setSelected(true);
        tvGuestName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tvGuestName.setSingleLine(true);


        return new ViewHolder(v, tvGuestName, tvTotalGuest, tvTotalItem, tvTransDate, tvTotalPaid, tvTakeaway, item);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Transaction transaction = items.get(position);

        holder.tvGuestName.setText(transaction.getCustomerName());
        holder.tvTotalGuest.setText(transaction.getNumberOfCustomer());
        holder.tvTotalItem.setText(String.valueOf(transaction.getOrderDetail().size()));
        holder.tvTransDate.setText(transaction.getTransDate());
        holder.tvTotalPaid.setText(transaction.getTotal());

        if(transaction.getTakeaway().equalsIgnoreCase("0")){
            holder.tvTakeaway.setVisibility(View.GONE);
        }
        else{
            holder.tvTakeaway.setVisibility(View.VISIBLE);
        }


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null)
                    listener.onItemClick(transaction, holder.getAdapterPosition());
            }
        });

//
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

