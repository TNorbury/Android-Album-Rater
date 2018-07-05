package com.tylernorbury.albumrater.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tylernorbury.albumrater.R;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Now that the view has been created, fill out the various fields
        // We'll start by getting our album from the database

    }
}
