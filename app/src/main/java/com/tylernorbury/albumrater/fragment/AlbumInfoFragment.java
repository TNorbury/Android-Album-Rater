package com.tylernorbury.albumrater.fragment;


import android.app.Application;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tylernorbury.albumrater.AlbumRaterApp;
import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.database.entity.Album;
import com.tylernorbury.albumrater.database.repository.AlbumRepository;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumInfoFragment extends Fragment {


    public AlbumInfoFragment() {
        // Required empty public constructor
    }

    public static AlbumInfoFragment newInstance(String albumTitle, String albumArtist) {
        AlbumInfoFragment fragment = new AlbumInfoFragment();

        // Set the arguments for the new fragment
        Bundle args = new Bundle();
        args.putString("albumTitle", albumTitle);
        args.putString("albumArtist", albumArtist);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_info, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Now that the view has been created, fill out the various fields
        // We'll start by getting our album from the database by using the
        // primary key which was supplied as arguments to the fragment
        AlbumRepository.getRepository((Application) AlbumRaterApp.getContext())
                .getAlbum(getArguments().getString(getString(R.string.album_title_key)),
                        getArguments().getString(getString(R.string.album_artist_key)))
                .observe(this, new Observer<Album>() {
                    @Override
                    public void onChanged(@Nullable Album album) {
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
                });
    }
}
