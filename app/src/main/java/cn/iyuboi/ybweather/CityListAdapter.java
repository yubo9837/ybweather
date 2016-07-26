package cn.iyuboi.ybweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yubo on 2016/7/26.
 */
public class CityListAdapter extends BaseAdapter {

    private List<String> list;
    private LayoutInflater inflater;
    public CityListAdapter(Context context,List<String> list){

        this.list=list;
        inflater=LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView=null;
        if(view==null){
            rowView=inflater.inflate(R.layout.item_city_list,null);
        }else{
            rowView=view;
        }

        TextView tv_city=(TextView) rowView.findViewById(R.id.tv_city);
        tv_city.setText(getItem(i));

        return rowView;
    }
}
