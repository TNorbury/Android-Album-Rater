package com.tylernorbury.albumrater.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.database.entity.Album;

/**
 * A fragment for editing a specific album entry
 */
public class EditAlbumFragment extends Fragment
{

    private AlbumInfoFragment.OnAlbumEditEventListener mOnAlbumEditEventListener;

    Button.OnClickListener mSubmitEditButtonListener = new Button.OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            View form = getView();
            // We want to create a new album object to pass to the listener
            // that represents that "updated" album.
            // We'll start by collecting information from the various fields
            int albumRating = -1;
            boolean validForm = true;

            // Get the name of the album
            String albumName = ((TextView) form.findViewById(R.id.edit_album_title)).getText()
                    .toString();

            // If the album name is blank, tell the user to fill it out
            if (albumName.isEmpty())
            {
                validForm = false;
            }

            // Get the artist of the album
            String albumArtist = ((TextView) form.findViewById(R.id.edit_album_artist)).getText()
                    .toString();

            // If the album artist is blank, tell the user to fill it out
            if (albumArtist.isEmpty())
            {
                validForm &= false;
            }

            // Determine the rating of the album
            int selectedRating = ((RadioGroup) form.findViewById(R.id.edit_album_rating))
                    .getCheckedRadioButtonId();

            // If either the "good" or "bad" rating is selected, set the
            // corresponding rating
            if (selectedRating == R.id.edit_album_rating_good)
            {
                albumRating = Album.GOOD_ALBUM;
            }
            else if (selectedRating == R.id.edit_album_rating_bad)
            {
                albumRating = Album.BAD_ALBUM;
            }

            // If a -1 was returned, that means that neither option was
            // selected. In that case tell the user to selected a rating
            else if (selectedRating == -1)
            {
                validForm &= false;
            }

            // Get the text review
            String albumReview = ((TextView) form.findViewById(R.id.edit_album_review)).getText()
                    .toString();

            // If all the fields are filled out, then we can tell the listener to submit the edit
            if (validForm)
            {

                // This Album object represents the "updated" album, and
                // will be passed to the listener
                Album album = new Album(albumName, albumArtist, albumRating, albumReview);

                mOnAlbumEditEventListener.onAlbumEditEvent(album,
                        AlbumInfoFragment.OnAlbumEditEventListener.EDIT_ALBUM_SUBMIT_CODE);
            }

            // Otherwise, tell the user that the form isn't valid
            else
            {
                Toast.makeText(getContext(), getString(R.string.invalid_album_form_message),
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * Create a new fragment for editing fragments
     */
    public EditAlbumFragment()
    {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_album, container, false);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        // Create a listener for album edit-related events from the attaching
        // context
        mOnAlbumEditEventListener = (AlbumInfoFragment.OnAlbumEditEventListener) context;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // Fill out the text fields with the album's current values

        // Set the album title field
        ((EditText) view.findViewById(R.id.edit_album_title))
                .setText(getArguments().getString(getString(R.string.original_album_title)));

        // Set the album artist field
        ((EditText) view.findViewById(R.id.edit_album_artist))
                .setText(getArguments().getString(getString(R.string.original_album_artist)));

        // Based upon the rating of the album, set one of the rating buttons as
        // selected
        // If it's a good album, then set the thumbs up button
        if (getArguments().getInt(getString(R.string.original_album_rating)) == Album.GOOD_ALBUM)
        {

            // Toggle the thumbs up button
            ((RadioButton) view.findViewById(R.id.edit_album_rating_good)).toggle();
        }

        // Otherwise, if it's a bad album, then set the thumbs down button
        else if (getArguments()
                .getInt(getString(R.string.original_album_rating)) == Album.BAD_ALBUM)
        {

            // Toggle the thumbs down button
            ((RadioButton) view.findViewById(R.id.edit_album_rating_bad)).toggle();
        }

        // Enter the existing review into the text field
        ((EditText) view.findViewById(R.id.edit_album_review))
                .setText(getArguments().getString(getString(R.string.original_album_review)));

        // Now that we have the fields all filled out, we have to add event
        // listeners to the two buttons
        // Set the listener for the cancel button
        ((Button) view.findViewById(R.id.btn_cancel_edit))
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // Since the user clicked the cancel button, we'll just return
                        // to the edit album fragment.
                        // Since the listener doesn't need an album object when the edit is being canceled, we'll just pass null
                        mOnAlbumEditEventListener.onAlbumEditEvent(null,
                                AlbumInfoFragment.OnAlbumEditEventListener.EDIT_ALBUM_CANCEL_CODE);
                    }
                });

        // Set the album listener for the submit edit button
        ((Button) view.findViewById(R.id.btn_submit_edit))
                .setOnClickListener(mSubmitEditButtonListener);
    }
}
