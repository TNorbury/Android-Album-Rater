/**
 * Author: Tyler Norbury (tylernorbury96@gmail.com)
 * Version 1.1 (June 20th, 2018)
 */

package com.tylernorbury.albumrater;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tylernorbury.albumrater.adapter.AlbumListAdapter;
import com.tylernorbury.albumrater.database.entity.Album;
import com.tylernorbury.albumrater.fragment.AddAlbumFragment;
import com.tylernorbury.albumrater.fragment.AllAlbumsFragment;
import com.tylernorbury.albumrater.viewModel.AlbumViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private AlbumViewModel mAlbumViewModel;
    private AlbumListAdapter mAdapter;

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

            // First, determine which menu item was selected
            switch (item.getItemId()) {

                // The home screen will display all the albums the user has
                // reviewed
                case R.id.navigation_home:
                    frag = AllAlbumsFragment.newInstance(mAdapter);
                    mTextMessage.setText(R.string.title_home);
                    ret = true;
                    break;

                // This will the screen where the user can add a new album
                // review
                case R.id.navigation_add:
                    frag = new AddAlbumFragment();
                    mTextMessage.setText(R.string.title_add);
                    ret = true;
                    break;
            }

            // Now perform a transaction to display the new fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_view, frag);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.addToBackStack(null);
            transaction.commit();

            return ret;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Create an adapter for display the list of albums
        mAdapter = new AlbumListAdapter(this);

        // Create a new fragment for displaying all of the albums.
        AllAlbumsFragment fragment = AllAlbumsFragment.newInstance(mAdapter);

        // Perform a transaction to display the fragment.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_view, fragment);
        transaction.commit();

        // Get a view model for the AlbumViewModel
        mAlbumViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);

        // Add an observer for any changes to the list of albums. When that
        // change does happen, then update the copy of words that the adapter has saved
        mAlbumViewModel.getAllAlbums().observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(@Nullable List<Album> albums) {
                mAdapter.setAlbums(albums);
            }
        });
    }
}
