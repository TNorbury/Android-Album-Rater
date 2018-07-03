package com.tylernorbury.albumrater.fragment;


import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.tylernorbury.albumrater.AlbumRaterApp;
import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.adapter.AlbumListAdapter;
import com.tylernorbury.albumrater.database.repository.AlbumRepository;
import com.tylernorbury.albumrater.viewModel.AlbumViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumListFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private static AlbumListAdapter mAdapter;
    private OnSortParametersChangedListener mOnSortParametersChangedListener;
    private OnSearchQuerySubmittedListener mOnSearchQuerySubmittedListener;

    /**
     * This interface will handle events when a new sorting parameter is
     * selected from the spinner
     */
    public interface OnSortParametersChangedListener {
        void onSortParametersChanged(int queryCode);
    }


    public interface OnSearchQuerySubmittedListener {
        void onSearchQuerySubmittedListener(String searchQuery);
    }

    /**
     * Creates a new AlbumListFragment
     */
    public AlbumListFragment() { }

    /**
     * Create a new instance of this AlbumListFragment
     * @param adapter The RecyclerView adapter that this fragment will use.
     * @return a new AlbumListFragment
     */
    public static AlbumListFragment newInstance(AlbumListAdapter adapter) {

        // Create a new fragment
        AlbumListFragment fragment = new AlbumListFragment();

        // Set the adapter to the one supplied via argument
        mAdapter = adapter;

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get a reference to the recycler view
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        // Set the adapter for the recycler view and set to the layout manager.
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 1));

        // Set up the spinner selection listener
        Spinner spinner = view.findViewById(R.id.sorting_spinner);
        spinner.setOnItemSelectedListener(this);

        // Create an onClick listener for the search button
        ImageButton searchButton = (ImageButton) view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the helper method to process the search
                handleSearch();
            }
        });

        // Create and set a listener for whenever the return/enter key is pressed
        EditText searchField = (EditText) view.findViewById(R.id.search_text);
        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                // If the IME Action "done" happened, then call the method to
                // handle searches
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handleSearch();
                }

                // Returning false because we want the system handle the event
                // as well and do whatever it needs to do with it.
                return false;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Try to cast the given context into an OnSortParametersChangedListener and an OnSearchQuerySubmittedListener
        try {
            mOnSortParametersChangedListener = (OnSortParametersChangedListener) context;
            mOnSearchQuerySubmittedListener = (OnSearchQuerySubmittedListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSortParametersChangedListener AND OnSearchQuerySubmittedListener");
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        // Set the spinner's selection to the whatever it was the last time the
        // user was on this fragment. This is done so that there is some
        // persistence when the user comes back to the persistence
        Spinner s = getView().findViewById(R.id.sorting_spinner);
        s.setSelection(ViewModelProviders.of(getActivity()).get(AlbumViewModel.class).getCurrentQuerySelection());

        // However, we do want to clear the search parameters so that all albums
        // will be displayed (albeit in the previously selected order) when they
        // navigate back to this fragment.
        EditText searchField = (EditText) getView().findViewById(R.id.search_text);
        searchField.setText("");

        // We also want to treat this as if the user has cleared the search box
        // themselves so that the activity will update the search parameters.
        handleSearch();
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

        mOnSortParametersChangedListener.onSortParametersChanged(queryCode);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // If nothing is selected, then we'll do nothing
    }

    /**
     * Helper method used to handle search events, which are caused by either
     * pressing the search button or hitting the return key while in the search
     * text box. This method generate an OnSearchSubmitted event
     */
    private void handleSearch() {

        // Under most cases when this method is called the search box will be
        // selected and the software keyboard will be up. Assuming that's the
        // case then we want to move the keyboard off screen.
        InputMethodManager imm = (InputMethodManager) AlbumRaterApp.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        // We also want to clear focus from the search box
        getView().clearFocus();

        String searchText = ((EditText)(getView().findViewById(R.id.search_text))).getText().toString();
        mOnSearchQuerySubmittedListener.onSearchQuerySubmittedListener(searchText);
    }

}
