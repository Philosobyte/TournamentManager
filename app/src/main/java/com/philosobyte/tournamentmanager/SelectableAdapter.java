package com.philosobyte.tournamentmanager;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ray on 1/14/2018.
 */
public class SelectableAdapter extends BaseAdapter implements AdapterView.OnItemClickListener, Filterable {

    protected ListView lv;
    protected int resId;
    protected int tvId;
    protected int[] btnIds;
    protected Activity context;
    protected int selectedItem = -1;
    protected List<String> items;
    protected List<String> filteredItems;
    protected Filter filter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredItems = items.stream().filter(s -> s.toLowerCase().contains(constraint.toString().toLowerCase())).collect(Collectors.toList());
            FilterResults results = new FilterResults();
            results.count = filteredItems.size();
            results.values = filteredItems;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    };

    public SelectableAdapter(Activity activity, ListView lv, int resId, int tvId, int... btnIds) {
        this.context = activity;
        this.resId = resId;
        this.tvId = tvId;
        this.btnIds = btnIds;
        this.lv = lv;
        lv.setOnItemClickListener(this);
        items = new ArrayList<>();
        filteredItems = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<String> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View finalConvertView;
        if (convertView == null) {
            finalConvertView = context.getLayoutInflater().inflate(resId, null);
        } else finalConvertView = convertView;
        TextView tvName = finalConvertView.findViewById(tvId);
        tvName.setText(filteredItems.get(position));
        List<ImageButton> btns = Arrays.stream(btnIds).mapToObj(
                btn -> (ImageButton)finalConvertView.findViewById(btn)).collect(Collectors.toList());
        if (selectedItem == position && selectedItem != -1) {
            btns.forEach(btn -> btn.setVisibility(View.VISIBLE));
        } else {
            btns.forEach(btn -> btn.setVisibility(View.GONE));
        }
        return finalConvertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedItem = position;
        notifyDataSetChanged();
    }

    public String getLastSelected() {
        return filteredItems.get(selectedItem);
    }

    public void add(String item) {
        items.add(item);
        filteredItems.add(item);
        notifyDataSetChanged();
    }

    public void addAll(List<String> items) {
        this.items.addAll(items);
        filteredItems.addAll(items);
        notifyDataSetChanged();
    }

    public void removeAll() {
        items = new ArrayList<>();
        filteredItems = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void remove(String item) {
        items.remove(item);
        filteredItems.remove(item );
        notifyDataSetChanged();
    }

    public boolean contains(String item) {
        return items.contains(item);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
}