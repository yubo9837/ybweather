package cn.iyuboi.ybweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yubo on 2016/7/26.
 */
public class CityListAdapter extends BaseAdapter implements Filterable {

    /* Full list of cities */
    private List<String> fullList;
    /* Filtered list of cities */
    private List<String> filteredList;
    private LayoutInflater inflater;
    public CityListAdapter(Context context,List<String> list){
        this.fullList=list;
        inflater=LayoutInflater.from(context);
        filteredList = new LinkedList<>();
        filteredList.addAll(list); // Show all by default
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public String getItem(int i) {
        return filteredList.get(i);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Not null-safe!
                if (constraint == null || constraint.length() == 0) {
                    // Original
                    results.count = fullList.size();
                    results.values = fullList;
                } else {
                    LinkedList<String> filtered = new LinkedList<>();
                    for (String city : fullList) {
                        if (city.startsWith(constraint.toString()))
                            filtered.add(city);
                    }
                    results.count = filtered.size();
                    results.values = filtered;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List<String>)results.values);
                notifyDataSetChanged();
            }
        };
    }
}
