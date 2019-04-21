package com.example.familymapclient;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class FilterListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<String> filterList;
    private LayoutInflater layoutInflater;
    private FiltersData filtersData;

    public FilterListAdapter(Context context, List<String> filterList, FiltersData filters) {
        this.context = context;
        this.filterList = filterList;
        this.layoutInflater = LayoutInflater.from(context);
        this.filtersData = filters;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = layoutInflater.inflate(R.layout.filter_list_item, parent, false);
        return new FilterListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        String item = filterList.get(position);
        MyViewHolder myViewHolder = ((MyViewHolder)viewHolder);

        String[] parts = item.split("\\|");

        myViewHolder.filterListItemLine1.setText(parts[1]);
        myViewHolder.filterListItemLine2.setText(parts[2]);
        myViewHolder.filterSwitch.setTag(parts[0]);

        if (filtersData != null) {
            Boolean checkValue = filtersData.filters.get(parts[0]);
            myViewHolder.filterSwitch.setChecked(checkValue == null || checkValue);
        }
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView filterListItemLine1;
        TextView filterListItemLine2;
        Switch filterSwitch;

        MyViewHolder(View itemView) {
            super(itemView);
            filterListItemLine1 = itemView.findViewById(R.id.filterListItemLine1);
            filterListItemLine2 = itemView.findViewById(R.id.filterListItemLine2);
            filterSwitch = itemView.findViewById(R.id.filterSwitch);

            filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String event = (String)buttonView.getTag();
                    filtersData.filters.put(event, isChecked);
                }
            });
        }
    }
}
