package com.tylernorbury.albumrater.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    private static AlbumListAdapter mAdapter;


    public AllAlbumsFragment() { }

    /**
     * Create a new instance of this AllAlbumsFragment
     * @param adapter The RecyclerView adapter that this fragment will use.
     * @return a new AllAlbumsFragment
     */
    public static AllAlbumsFragment newInstance(AlbumListAdapter adapter) {

        // Create a new fragment
        AllAlbumsFragment fragment = new AllAlbumsFragment();

        // Set the adapter to the one supplied via argument
        mAdapter = adapter;

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_albums, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get a reference to the recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // Set the adapter for the recycler view and set to the layout manager.
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));
    }
}
