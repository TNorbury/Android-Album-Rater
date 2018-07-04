/**
 * Author: Tyler Norbury (tylernorbury96@gmail.com)
 * Version 1.1 (June 20th, 2018)
 */

package com.tylernorbury.albumrater.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.WindowManager;

import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.adapter.AlbumListAdapter;
import com.tylernorbury.albumrater.database.entity.Album;
import com.tylernorbury.albumrater.fragment.AddAlbumFragment;
import com.tylernorbury.albumrater.fragment.AlbumListFragment;
import com.tylernorbury.albumrater.fragment.SettingsMenuFragment;
import com.tylernorbury.albumrater.viewModel.AlbumViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AlbumListFragment.OnSortParametersChangedListener, AlbumListFragment.OnSearchQuerySubmittedListener {
    private AlbumViewModel mAlbumViewModel;
    private AlbumListAdapter mAdapter;

    // Define the observer that will observe the album list
    private Observer<List<Album>> mAlbumListObserver = new Observer<List<Album>>() {
        @Override
        public void onChanged(@Nullable List<Album> albums) {
            mAdapter.setAlbums(albums);
        }
    };

    // We'll use a set of flags to determine the current state of the fragment
    // backstack. This is done so that we don't unnecessarily replace fragments
    // that are getting popped off the backstack AND it ensures that the correct
    // navigation button is selected when something is popped off of the backstack
    private static final int BACKSTACK_CLEAR = 0; // The backstack event has been handled
    private static final int BACKSTACK_ADDED = 1; // A fragment was added to the backstack
    private static final int BACKSTACK_POPPED = 2; // A fragment was popped from the backstack

    // This will keep track of the current state of the backstack
    private int mBackstackState = BACKSTACK_CLEAR;

    // If the currently selected navigation item was selected, we don't want to
    // do anything
    private BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationReselectedListener
            = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem item) {
            // Don't do anything
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        /**
         * Switches the displayed fragment depending on which menu option was
         * selected
         * @param item The menu item that was selected.
         * @return
         */
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean ret = false;
            Fragment frag = null;

            // First, determine which menu item was selected, and if necessary
            // create a new instance of the corresponding fragment
            switch (item.getItemId()) {

                // The home screen will display all the albums the user has
                // reviewed
                case R.id.navigation_home:
                    if (mBackstackState != BACKSTACK_POPPED)
                        frag = AlbumListFragment.newInstance(mAdapter);
                    ret = true;
                    break;

                // This will the screen where the user can add a new album
                // review
                case R.id.navigation_add:
                    if (mBackstackState != BACKSTACK_POPPED)
                        frag = new AddAlbumFragment();
                    ret = true;
                    break;

                case R.id.navigation_settings:
                    if (mBackstackState != BACKSTACK_POPPED)
                        frag = new SettingsMenuFragment();
                    ret = true;
                    break;
            }

            // If the backstack wasn't popped, then we want to replace the
            // fragment that is displayed
            if (mBackstackState != BACKSTACK_POPPED && frag != null) {

                // Perform a transaction to display the new fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_view, frag, "visible_fragment");
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.addToBackStack(null);
                transaction.commit();

                // Update the backstack state to indicate a fragment was added.
                mBackstackState = BACKSTACK_ADDED;
            }

            return ret;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationReselectedListener);

        // Create an adapter for display the list of albums
        mAdapter = new AlbumListAdapter(this);

        // Create a new fragment for displaying all of the albums.
        AlbumListFragment fragment = AlbumListFragment.newInstance(mAdapter);

        // Perform a transaction to display the fragment.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_view, fragment, "visible_fragment");
        transaction.commit();

        // Get a view model for the AlbumViewModel
        mAlbumViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);

        // This makes it so when the keyboard shows up the screen doesn't also move up.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Listen for changes to the backstack so that we can update the navigation
        // buttons
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                // If nothing was added to the backstack we know that this event
                // was triggered by a "pop". In that case we want to tell the
                // navigation view which fragment was popped, so that the correct
                // button can be highlighted.
                if (mBackstackState != BACKSTACK_ADDED) {

                    // Indicate that something was popped from the backstack.
                    mBackstackState = BACKSTACK_POPPED;

                    // Get the fragment that is currently displayed (the one that
                    // was popped)
                    Fragment frag = getSupportFragmentManager()
                            .findFragmentByTag("visible_fragment");

                    // Get the navigation view
                    BottomNavigationView navigation = findViewById(R.id.navigation);

                    // Now determine which type of fragment was popped, and
                    // select that fragment's corresponding navigation item
                    if (frag instanceof AlbumListFragment) {
                        navigation.setSelectedItemId(R.id.navigation_home);

                    }
                    else if (frag instanceof AddAlbumFragment) {
                        navigation.setSelectedItemId(R.id.navigation_add);
                    }
                    else if (frag instanceof SettingsMenuFragment) {
                        navigation.setSelectedItemId(R.id.navigation_settings);
                    }
                }

                // Indicate that the backstack change event has been handled
                mBackstackState = BACKSTACK_CLEAR;
            }
        });
    }

    @Override
    public void onSortParametersChanged(int queryCode) {

        // We want to take the parameters given to us by the fragment and make a
        // query using them. We'll then take the newly ordered results and
        // attach an observer to them
        mAlbumViewModel.getAlbumsFromQuery(queryCode)
                .observe(this, mAlbumListObserver);
    }

    @Override
    public void onSearchQuerySubmittedListener(String searchQuery) {
        // Take the given search query and get all albums using the query as a
        // search parameter
        mAlbumViewModel.updateSearchParameter(searchQuery);

        // We now want to get all the albums with the updated query.
        updateAlbumList();
    }

    /**
     * Tell the activity that it needs to update its album list
     */
    public void updateAlbumList() {

        // Update and observe the list of albums
        mAlbumViewModel.getAlbums().observe(this, mAlbumListObserver);
    }
}
