package mamabe.posappandroid.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mamabe.posappandroid.Activities.MenuSettingActivity;
import mamabe.posappandroid.Fragments.MenuFragment;
import mamabe.posappandroid.Models.MenuCategory;
import mamabe.posappandroid.R;

import static mamabe.posappandroid.R.drawable.text_border;
import static mamabe.posappandroid.R.drawable.text_border_selected;

/**
 * Created by DedeEko on 5/28/2017.
 */

public class MenuCategoryTypeAdapter extends RecyclerView.Adapter {

    ArrayList<MenuCategory> menuTypeList;

    private Context context;
    private MenuTypeAdapterListener listener;

    public int lastSelected = 0;

    public interface MenuTypeAdapterListener {
        void onClickedType(int position);
    }

    public MenuCategoryTypeAdapter(ArrayList<MenuCategory> menuTypeList, MenuTypeAdapterListener listener,
                               Context context) {
        this.menuTypeList = menuTypeList;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_category_type_item, parent, false);
        TextView tvMenuType = (TextView)v.findViewById(R.id.tv_category_type);
        ConstraintLayout itemWrapper = (ConstraintLayout)v.findViewById(R.id.item_wraperr);


        return new ViewHolder(v, tvMenuType, itemWrapper);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ViewHolder v = (ViewHolder) holder;
        final MenuCategory category = menuTypeList.get(position);
        v.itemWrapper.setBackgroundResource(text_border);
        v.tvMenuType.setText(category.getMenuType());

        if(position == lastSelected ){
            // You can change the background here
            v.itemWrapper.setBackgroundResource(text_border_selected);
        }
        if(position != lastSelected ){
            // Set default background
            v.itemWrapper.setBackgroundResource(text_border);

        }

        v.itemWrapper.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onClickedType(v.getAdapterPosition());
                notifyItemChanged(lastSelected);
                updateHighlightPosition(v.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuTypeList.size();
    }

    public void updateHighlightPosition(int position){
        lastSelected = position;
        notifyItemChanged(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvMenuType;
        public ConstraintLayout itemWrapper;


        public ViewHolder(View itemView, TextView tvMenuType, ConstraintLayout itemWrapper) {
            super(itemView);
            this.tvMenuType = tvMenuType;
            this.itemWrapper = itemWrapper;
        }

    }
}
