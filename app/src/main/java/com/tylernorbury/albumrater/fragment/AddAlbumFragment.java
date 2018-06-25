package com.tylernorbury.albumrater.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tylernorbury.albumrater.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAlbumFragment extends Fragment {

    public AddAlbumFragment() {
        // Required empty public constructor
    }


    public static AddAlbumFragment newInstance() {
        AddAlbumFragment fragment = new AddAlbumFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_album, container, false);
    }

}
