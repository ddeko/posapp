package mamabe.posappandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {
    protected List<T> Data;
    protected Context context = null;
    protected LayoutInflater inflater = null;
    protected List<T> originalList;

    public BaseAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.Data.size();
    }

    @Override
    public T getItem(int postion) {
        return Data.get(postion);
    }

    public List<T> getData() {
        return Data;
    }

    public void setData(List<T> data) {
        Data = data;
        originalList = data;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

    @Override
    public abstract long getItemId(int position);


}
