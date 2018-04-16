package com.pratik.twofactorauth;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pratik on 4/10/2018.
 */

public class ListAdapter extends ArrayAdapter<Artist> {

    private Activity context;
    private List<Artist> artistList;

    public ListAdapter(Activity context,List<Artist> artistList) {
        super(context,R.layout.list_layout,artistList);
        this.context = context;
        this.artistList = artistList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        TextView textviewName = (TextView)listViewItem.findViewById(R.id.textViewName);
        Artist artist = artistList.get(position);

        textviewName.setText(artist.getArtistName());
        return listViewItem;

    }
}

