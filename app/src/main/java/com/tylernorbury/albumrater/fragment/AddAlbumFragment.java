package com.tylernorbury.albumrater.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tylernorbury.albumrater.AlbumRaterApp;
import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.database.entity.Album;
import com.tylernorbury.albumrater.viewModel.AlbumViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddAlbumFragment extends Fragment {

    // When the button is clicked we'll retrieve all the data from the various
    // forms, create an album object from that, and insert it into the database.
    Button.OnClickListener mButtonClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            int albumRating = -1;
            boolean validForm = true;
            View form = getView();

            // Get the name of the album
            String albumName = ((TextView)form
                    .findViewById(R.id.add_album_title)).getText().toString();

            // If the album name is blank, tell the user to fill it out
            if (albumName.isEmpty()) {
                validForm = false;
            }

            // Get the artist of the album
            String albumArtist = ((TextView)form
                    .findViewById(R.id.add_album_artist)).getText().toString();

            // If the album artist is blank, tell the user to fill it out
            if (albumArtist.isEmpty()) {
                validForm &= false;
            }

            // Determine the rating of the album
            int selectedRating = ((RadioGroup)form
                    .findViewById(R.id.add_album_rating)).getCheckedRadioButtonId();

            // If either the "good" or "bad" rating is selected, set the
            // corresponding rating
            if (selectedRating == R.id.add_album_rating_good) {
                albumRating = Album.GOOD_ALBUM;
            }
            else if (selectedRating == R.id.add_album_rating_bad) {
                albumRating = Album.BAD_ALBUM;
            }

            // If a -1 was returned, that means that neither option was
            // selected. In that case tell the user to selected a rating
            else if (selectedRating == -1) {
                validForm &= false;
            }

            // Finally, we want to get the review that was entered for this album
            String albumReview = ((TextView)form.findViewById(R.id.add_album_review))
                    .getText().toString();


            // If the form is considered "valid", then we want to insert the
            // data into the database
            if (validForm) {
                Album album = new Album(albumName, albumArtist, albumRating, albumReview);

                // Get the album view model from the parent activity and then
                // insert the new album
                AlbumViewModel model = ViewModelProviders.of(getActivity())
                        .get(AlbumViewModel.class);
                model.insert(album);

                // Now "pop" the back stack, returning the user to the last
                // screen they were on
                getActivity().getSupportFragmentManager().popBackStack();
            }

            // Otherwise, if the form isn't valid, tell the user.
            else {
                Toast.makeText(AlbumRaterApp.getContext(),
                        getString(R.string.invalid_album_form_message),
                        Toast.LENGTH_LONG).show();
            }
        }
    };

    public AddAlbumFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_album, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get the button from the view and add an event listener to it
        Button button = view.findViewById(R.id.btn_add_album);
        button.setOnClickListener(mButtonClickListener);
    }
}
