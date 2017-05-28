package mamabe.posappandroid.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

public class MenuCategoryAdapter extends RecyclerView.Adapter {

    ArrayList<MenuCategory> menuCategorieList;

    private Context context;
    private MenuSettingActivity activity;
    private MenuFragment fragment;
    private MenuCategoryAdapterListener listener;

//    private RolePicker rolePicker;
//    private ChangePasswordDialog changePasswordDialog;

    public int lastSelected = 0;

    public interface MenuCategoryAdapterListener {
        void onClicked(int position);
    }

    public MenuCategoryAdapter(ArrayList<MenuCategory> menuCategorieList, MenuCategoryAdapterListener listener,
                               Context context, MenuSettingActivity activity, MenuFragment fragment) {
        this.menuCategorieList = menuCategorieList;
        this.listener = listener;
        this.context = context;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_category_item, parent, false);
        TextView tvMenuCategory = (TextView)v.findViewById(R.id.tv_category);
        ConstraintLayout itemWrapper = (ConstraintLayout)v.findViewById(R.id.item_wraper);

//        double d = getScreenWidth()*0.5;
//        itemWrapper.setMinWidth((int)d);
//        itemWrapper.setMaxWidth((int)d);

        return new ViewHolder(v, tvMenuCategory, itemWrapper);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ViewHolder v = (ViewHolder) holder;
        final MenuCategory category = menuCategorieList.get(position);
        v.itemWrapper.setBackgroundResource(text_border);
        v.tvMenuCategory.setText(category.getMenuCategoryName());

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
                    listener.onClicked(v.getAdapterPosition());
                notifyItemChanged(lastSelected);
                updateHighlightPosition(v.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuCategorieList.size();
    }

    public void updateHighlightPosition(int position){
        lastSelected = position;
        notifyItemChanged(position);
    }

    public void resetLastSelected()
    {
        lastSelected = 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvMenuCategory;
        public ConstraintLayout itemWrapper;


        public ViewHolder(View itemView, TextView tvMenuCategory, ConstraintLayout itemWrapper) {
            super(itemView);
            this.tvMenuCategory = tvMenuCategory;
            this.itemWrapper = itemWrapper;
        }

    }
}
