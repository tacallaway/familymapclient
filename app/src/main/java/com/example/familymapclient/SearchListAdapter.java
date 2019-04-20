package com.example.familymapclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import java.util.List;

public class SearchListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<String> searchList;
    private LayoutInflater layoutInflater;
    private FamilyModel familyModel;
    SearchActivity parentActivity;

    public SearchListAdapter(Context context, List<String> searchList) {
        this.context = context;
        this.searchList = searchList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setFamilyModel(FamilyModel familyModel) {

        this.familyModel = familyModel;
    }

    public void setParentActivity(SearchActivity parentActivity) {

        this.parentActivity = parentActivity;
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

        Icon image = null;
        int color;

        if (parts[0].equals("person")) {

            String personID = parts[1];

            if (parts[2].equals("m")) {
                image = FontAwesomeIcons.fa_male;
                color = R.color.male_icon;
            } else {
                image = FontAwesomeIcons.fa_female;
                color = R.color.female_icon;
            }
            myViewHolder.searchListItemLine1.setText(parts[3]);
            myViewHolder.searchListItemLine2.setText("");
            myViewHolder.image.setTag("person|" + personID);
        } else {

            String eventID = parts[1];

            myViewHolder.searchListItemLine1.setText(parts[2]);
            myViewHolder.searchListItemLine2.setText(parts[3]);
            image = FontAwesomeIcons.fa_map_marker;
            color = R.color.colorMarkerGrey;
            myViewHolder.image.setTag("event|" + eventID);
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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String tag = (String)(view.findViewById(R.id.searchListImage).getTag());
            String[] parts = tag.split("\\|");

            if(parts[0].equals("person")) {

                String personID = parts[1];

                Intent intent = new Intent(context, PersonActivity.class);
                intent.putExtra("FAMILY_MODEL", familyModel);
                intent.putExtra("PERSON_ID", personID);
                context.startActivity(intent);
                parentActivity.finish();
            } else {

                String eventID = parts[1];
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("FAMILY_MODEL", familyModel);
                intent.putExtra("EVENT_ID", eventID);
                context.startActivity(intent);
                parentActivity.finish();
            }
        }
    }
}