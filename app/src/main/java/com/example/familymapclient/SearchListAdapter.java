package com.example.familymapclient;

import java.util.List;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

public class SearchListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<String> searchList;
    private LayoutInflater layoutInflater;

    public SearchListAdapter(Context context, List<String> searchList) {
        this.context = context;
        this.searchList = searchList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = layoutInflater.inflate(R.layout.search_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        String item = searchList.get(position);
        MyViewHolder myViewHolder = ((MyViewHolder)viewHolder);

        String[] parts = item.split("\\|");

        String line1;
        String line2;
        Icon image = null;
        int color;

        if (parts[0].equals("person")) {
            if (parts[1].equals("m")) {
                image = FontAwesomeIcons.fa_male;
                color = R.color.male_icon;
            } else {
                image = FontAwesomeIcons.fa_female;
                color = R.color.female_icon;
            }
            myViewHolder.searchListItemLine1.setText(parts[2]);
            myViewHolder.searchListItemLine2.setText("");
        } else {
            myViewHolder.searchListItemLine1.setText(parts[1]);
            myViewHolder.searchListItemLine2.setText(parts[2]);
            image = FontAwesomeIcons.fa_map_marker;
            color = R.color.colorMarkerGrey;
        }

        Drawable genderIcon = new IconDrawable(context, image).colorRes(color).sizeDp(40);
        myViewHolder.image.setImageDrawable(genderIcon);
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView searchListItemLine1;
        TextView searchListItemLine2;
        ImageView image;

        MyViewHolder(View itemView) {
            super(itemView);
            searchListItemLine1 = itemView.findViewById(R.id.searchListItemLine1);
            searchListItemLine2 = itemView.findViewById(R.id.searchListItemLine2);
            image = itemView.findViewById(R.id.searchListImage);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}