package com.tylernorbury.albumrater.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tylernorbury.albumrater.AlbumRaterApp;
import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.database.entity.Album;

import java.util.List;

/**
 * A class that creates an adapter between albums and a recycler view
 */
public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder> {

    private int mSelectedAlbumIndex = 0;
    private final LayoutInflater mInflater;
    private List<Album> mAlbums;
    private OnAlbumSelectedListener mOnAlbumSelectedListener;

    /**
     * A view holder that holds and represents a single album. Also handles
     * events when the album is selected.
     */
    class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final GridLayout mAlbumView;

        AlbumViewHolder(View albumView) {
            super(albumView);
            mAlbumView = albumView.findViewById(R.id.albumView);
            mAlbumView.setOnClickListener(this);
        }

        public GridLayout getAlbumView() {
            return mAlbumView;
        }

        @Override
        public void onClick(View v) {

            // Since a new album has been selected, update the index to that of
            // the currently selected album
            notifyItemChanged(mSelectedAlbumIndex);
            mSelectedAlbumIndex = getAdapterPosition();
            notifyItemChanged(mSelectedAlbumIndex);

            // We now want to notify the main activity that an album has been
            // selected and send it the album that was selected so that it can
            // handle displaying it.
            mOnAlbumSelectedListener.onAlbumSelectedListener(mAlbums.get(mSelectedAlbumIndex));
        }
    }

    /**
     * This interface handles events for when an album is selected from the
     * recycler view
     */
    public interface OnAlbumSelectedListener {
        void onAlbumSelectedListener(Album album);
    }

    /**
     * Create a new AlbumListAdapter
     * @param context The application context
     */
    public AlbumListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);

        // From the given context, create a new listener.
        mOnAlbumSelectedListener = (OnAlbumSelectedListener) context;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_album, parent, false);
        return new AlbumViewHolder(itemView);
    }

    /**
     * This method binds a ViewHolder to a particular album from the list of
     * albums
     *
     * @param holder The ViewHolder that will be bound to
     * @param position The position, in the list of albums, that will be bound
     */
    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {

        // If we've received a list of albums, then set up the current album's
        // view with the relevant UI information
        if (mAlbums != null) {
            Album current = mAlbums.get(position);

            // Display the title of the mAlbumView
            TextView title = holder.getAlbumView().findViewById(R.id.AlbumTitle);
            title.setText(current.getTitle());

            // Display the artist of the mAlbumView
            TextView artist = holder.getAlbumView().findViewById(R.id.AlbumArtist);
            artist.setText(current.getArtist());

            // Display the date the review was posted
            TextView date = holder.getAlbumView().findViewById(R.id.AlbumReviewDate);
            date.setText(current.getReviewDateString());

            // Display the rating of the mAlbumView as either a thumbs up or thumbs
            // down
            // Get the view that contains the rating image
            ImageView ratingImage = holder.getAlbumView().findViewById(R.id.AlbumRating);

            // If the mAlbumView's rating is positive (i.e. 1), then we'll display a
            // thumbs up
            if (current.getRating() == Album.GOOD_ALBUM) {
                ratingImage.setImageDrawable(AlbumRaterApp.getContext()
                        .getResources().getDrawable(
                                R.drawable.ic_thumb_up_black_24dp, null));
            }

            // Otherwise, if it's negative (i.e. 0), then we'll display a
            // thumbs down
            else if (current.getRating() == Album.BAD_ALBUM) {
                ratingImage.setImageDrawable(AlbumRaterApp.getContext()
                        .getResources().getDrawable(
                                R.drawable.ic_thumb_down_black_24dp, null));
            }
        }
    }

    /**
     * Update the local copy of albums, and let any observers know that the
     * internal data has been changed
     *
     * @param albums The new list of albums
     */
    public void setAlbums(List<Album> albums) {
        mAlbums = albums;
        notifyDataSetChanged();
    }


    /**
     * @return The total number of items in this adapter
     */
    @Override
    public int getItemCount() {
        int itemCount;

        if (mAlbums != null) {
            itemCount = mAlbums.size();
        }
        else {
            itemCount = 0;
        }

        return itemCount;
    }
}
