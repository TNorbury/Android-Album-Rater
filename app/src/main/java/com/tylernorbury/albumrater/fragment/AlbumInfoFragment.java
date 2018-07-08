package com.tylernorbury.albumrater.fragment;


import android.app.AlertDialog;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tylernorbury.albumrater.AlbumRaterApp;
import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.database.entity.Album;
import com.tylernorbury.albumrater.database.repository.AlbumRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumInfoFragment extends Fragment {
    private Album mAlbum;
    private OnAlbumDeletedListener mOnAlbumDeletedListener;
    private OnAlbumEditEventListener mOnAlbumEditEventListener;

    /**
     * Listener for when the user selects to delete an album
     */
    public interface OnAlbumDeletedListener {
        void onAlbumDeleted(Album album);
    }

    /**
     * Listener for when the user selects to edit an album, and also handles all
     * events related to editing an album, such as submitting an edit, or
     * backing out of an edit
     */
    public interface OnAlbumEditEventListener {

        /**
         * This tells the listener that it should start the process of editing
         * an album
         */
        int EDIT_ALBUM_START_CODE = 1;

        /**
         * This tells the listener that it should submit the changes made by the
         * edit
         */
        int EDIT_ALBUM_SUBMIT_CODE = 2;

        /**
         * This tells the listener that it should cancel the edit and revert
         * back to some other UI state
         */
        int EDIT_ALBUM_CANCEL_CODE = 3;

        void onAlbumEditEvent(Album album, int editCode);
    }

    /**
     * Create a new AlbumInfoFragment
     */
    public AlbumInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_info, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Create a reference to the attaching context and treat it as a
        // listener for album deletion events
        mOnAlbumDeletedListener = (OnAlbumDeletedListener) context;

        // Create a listener from the attaching context to handle album editing
        // events
        mOnAlbumEditEventListener = (OnAlbumEditEventListener) context;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Now that the view has been created, fill out the various fields
        // We'll start by getting our album from the database by using the
        // primary key which was supplied as arguments to the fragment
        LiveData<Album> album = AlbumRepository.getRepository((Application) AlbumRaterApp.getContext())
                .getAlbum(getArguments().getString(getString(R.string.original_album_title)),
                        getArguments().getString(getString(R.string.original_album_artist)));

        // When there are changes to the album, we want to update this view
        album.observe(this, new Observer<Album>() {
            @Override
            public void onChanged(@Nullable Album album) {

                // Since this can get called after the album has been deleted,
                // we want to make sure that it isn't null before we try to
                // update the view
                if (album != null){
                    mAlbum = album;

                    // Set the title of the album
                    ((TextView) view.findViewById(R.id.album_title))
                            .setText(album.getTitle());

                    // Set the artist of the album
                    ((TextView) view.findViewById(R.id.album_artist))
                            .setText(album.getArtist());

                    // Set the date fo the review of the album
                    ((TextView) view.findViewById(R.id.album_date))
                            .setText(album.getReviewDateString());

                    // Display the correct rating image based upon the album's rating
                    // If the rating is good, then display a thumbs up
                    if (album.getRating() == Album.GOOD_ALBUM) {
                        ((ImageView) view.findViewById(R.id.album_rating))
                                .setImageDrawable(getResources()
                                        .getDrawable(R.drawable.ic_thumb_up_unselected_big,
                                                null));
                    }

                    // Otherwise, if the rating is bad, then display a thumbs down
                    else if (album.getRating() == Album.BAD_ALBUM) {
                        ((ImageView) view.findViewById(R.id.album_rating))
                                .setImageDrawable(getResources()
                                        .getDrawable(R.drawable.ic_thumb_down_unselected_big,
                                                null));
                    }
                }
            }
        });

        // Add an on click listeners to the edit album button
        ((Button) view.findViewById(R.id.btn_edit_album)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // We want to signal to our parent activity that we want to edit
                // this album
                mOnAlbumEditEventListener.onAlbumEditEvent(mAlbum,
                        OnAlbumEditEventListener.EDIT_ALBUM_START_CODE);

            }
        });

        // Add an on click listener to the delete album button
        ((Button) view.findViewById(R.id.btn_delete_album)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // We want to create a pop-up to dialog to confirm that the user
                // actually wants to delete this album
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.dialog_delete_album_title)
                        .setMessage(R.string.dialog_delete_album_message)
                        .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "Deleting Album", Toast.LENGTH_SHORT).show();

                                // If the user chooses to delete this album,
                                // then we want to inform our parent activity
                                mOnAlbumDeletedListener.onAlbumDeleted(mAlbum);
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // If cancel is selected, then we won't do
                                // anything, just close the dialog
                            }
                        })
                        .create();

                // We now want to "pop up" the dialog
                dialog.show();
            }
        });
    }
}
