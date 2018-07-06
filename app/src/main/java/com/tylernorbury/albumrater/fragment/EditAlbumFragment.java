package com.tylernorbury.albumrater.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tylernorbury.albumrater.R;

/**
 * A fragment for editing a specific album entry
 */
public class EditAlbumFragment extends Fragment {


    public EditAlbumFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_album, container, false);
    }

}
