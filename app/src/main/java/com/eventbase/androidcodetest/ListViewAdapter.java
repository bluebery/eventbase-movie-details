package com.eventbase.androidcodetest;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bluebery on 7/30/2015.
 * Modified from http://www.androidbegin.com/tutorial/android-parse-com-listview-images-and-texts-tutorial/
 */
public class ListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ImageLoader imageLoader;
    private List<MovieInformation> movieList = null;
    private ArrayList<Integer> selectedItems = new ArrayList<>();

    public ListViewAdapter(Context context, List<MovieInformation> movieList) {
        this.context = context;
        this.movieList = movieList;
        inflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(context);
    }

    public class ListItemViewHolder {
        TextView name;
        TextView shortdesc;
        ImageView image;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ListItemViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ListItemViewHolder();

            view = inflater.inflate(R.layout.listview_item, null);

            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.shortdesc = (TextView) view.findViewById(R.id.shortdesc);
            viewHolder.image = (ImageView) view.findViewById(R.id.image);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ListItemViewHolder) view.getTag();
        }

        viewHolder.name.setText(movieList.get(position).Name);
        viewHolder.shortdesc.setText(movieList.get(position).ShortDesc);

        // Use ImageLoader class to fetch a bitmap for the url given and display it in the ImageView given.
        // Also passing a 'progress' view to hide when loading is complete.
        imageLoader.DisplayImage(movieList.get(position).ThumbnailURL, viewHolder.image, (ProgressBar) view.findViewById(R.id.progress));

        // single click; this is for launching into the movie detail view
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                // a single click on a selected (highlighted) movie will unselect it
                if(selectedItems.contains(position)) {
                    selectedItems.remove((Integer) position);
                    UnSelectView(view);
                    return;
                }

                // start intent for the movie detail view, passing in the id of the movie we are interested in
                Intent intent = new Intent(context, SingleItemView.class);
                intent.putExtra("uid", (movieList.get(position).uid));
                context.startActivity(intent);
            }
        });

        // long click; this is for selecting (highlighting) items
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectedItems.add((Integer)position);
                SelectView(view);

                return true;
            }
        });

        // if the position is the selected one, mark the view as selected
        if(IsItemSelected(position)) { SelectView(view);}
        else { UnSelectView(view); }

        return view;
    }

    // gets whether the item position is already selected
    private boolean IsItemSelected(Integer position) {
        return selectedItems.contains(position);
    }

    // helps to select a view
    private void SelectView(View view) {
        view.setAlpha(0.5f);
        view.setScaleX(0.75f);
        view.setScaleY(0.75f);
    }

    // helps to unselect a row
    private void UnSelectView(View view) {
        view.setAlpha(1.0f);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
    }
}
