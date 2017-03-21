package com.marcoshoya.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Marcos Lazarin on 3/7/2017.
 */

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {

    private static final String TAG = "FlickrRecyclerViewAdapt";
    private List<Photo> photoList;
    private Context context;

    public FlickrRecyclerViewAdapter(Context context, List<Photo> photoList) {
        this.photoList = photoList;
        this.context = context;
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail = null;
        TextView title = null;

        public FlickrImageViewHolder(View itemView) {
            super(itemView);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }



}
