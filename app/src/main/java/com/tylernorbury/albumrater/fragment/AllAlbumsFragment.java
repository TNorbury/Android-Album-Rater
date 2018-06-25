package com.tylernorbury.albumrater.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.adapter.AlbumListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllAlbumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllAlbumsFragment extends Fragment {

    private static RecyclerView mRecyclerView;


    public AllAlbumsFragment() { }

    /**
     * Create a new instance of this AllAlbumsFragment
     * @param adapter The RecyclerView adapter that this fragment will use.
     * @return a new AllAlbumsFragment
     */
    // TODO: Rename and change types and number of parameters
    public static AllAlbumsFragment newInstance(AlbumListAdapter adapter) {
        AllAlbumsFragment fragment = new AllAlbumsFragment();

        // Take the adapter passed as an argument and connect it to this
        // fragment's recycler view
        mRecyclerView.setAdapter(adapter);

        // Set the grid layout manager for the recycler view
        mRecyclerView.setLayoutManager(new GridLayoutManager(fragment.getContext(), 1));


        // not sure if I need all this bundle stuff, so I'll leave it commented
        // out for now.
        // Bundle args = new Bundle();
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        // fragment.setArguments(args);
        return fragment;
    }

/*    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
*//*        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*//*
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_albums, container, false);

        // Get a reference to the recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        return view;
    }

}
