package com.tylernorbury.albumrater.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.adapter.AlbumListAdapter;
import com.tylernorbury.albumrater.database.repository.AlbumRepository;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllAlbumsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllAlbumsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static AlbumListAdapter mAdapter;
    private OnSortParametersChangedListener mListener;

    /**
     * This interface will handle events when a new sorting parameter is
     * selected from the spinner
     */
    public interface OnSortParametersChangedListener {
        void onSortParametersChanged(int queryCode);
    }

    /**
     * Creates a new AllAlbumsFragment
     */
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_albums, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get a reference to the recycler view
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        // Set the adapter for the recycler view and set to the layout manager.
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));

        // Set up the spinner selection listener
        Spinner spinner = view.findViewById(R.id.sorting_spinner);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Try to cast the given context into an OnSortParametersChangedListener
        try {
            mListener = (OnSortParametersChangedListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSortParametersChangedListener");
        }
    }

    /**
     * This will handle an item being selected from the spinner. The sorting of
     * the album list will be changed depending on the selection
     *
     * @param parent The AdapterView where the selection happened
     * @param view The view within the AdapterView that was clicked
     * @param position The position of the view in the adapter
     * @param id The row id of the item that is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // Get the string value of the spinner item that
        String sortSelection = parent.getItemAtPosition(position).toString();
        int queryCode = 0;

        // Based on the selected option, determine query code we'll use
        // Sort by title
        if (sortSelection.equals(getString(R.string.sort_title_asc))) {
            queryCode = AlbumRepository.QUERY_TITLE_ASC;
        }
        else if (sortSelection.equals(getString(R.string.sort_title_desc))) {
            queryCode = AlbumRepository.QUERY_TITLE_DESC;
        }

        // Sort by name of artist
        else if (sortSelection.equals(getString(R.string.sort_artist_asc))) {
            queryCode = AlbumRepository.QUERY_ARTIST_ASC;
        }
        else if (sortSelection.equals(getString(R.string.sort_artist_desc))) {
            queryCode = AlbumRepository.QUERY_ARTIST_DESC;
        }

        // Sorting by rating
        else if (sortSelection.equals(getString(R.string.sort_rating_asc))) {
            queryCode = AlbumRepository.QUERY_RATING_ASC;
        }
        else if (sortSelection.equals(getString(R.string.sort_rating_desc))) {
            queryCode = AlbumRepository.QUERY_RATING_DESC;
        }

        // Sorting by date of review
        else if (sortSelection.equals(getString(R.string.sort_date_asc))) {
            queryCode = AlbumRepository.QUERY_DATE_ASC;
        }
        else if (sortSelection.equals(getString(R.string.sort_date_desc))) {
            queryCode = AlbumRepository.QUERY_DATE_DESC;
        }

        mListener.onSortParametersChanged(queryCode);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // If nothing is selected, then we'll do nothing
    }


}
