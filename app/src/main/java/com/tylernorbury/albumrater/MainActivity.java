/**
 * Author: Tyler Norbury (tylernorbury96@gmail.com)
 * Version 1.1 (June 20th, 2018)
 */

package com.tylernorbury.albumrater;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.tylernorbury.albumrater.adapter.AlbumListAdapter;
import com.tylernorbury.albumrater.database.entity.Album;
import com.tylernorbury.albumrater.fragment.AllAlbumsFragment;
import com.tylernorbury.albumrater.viewModel.AlbumViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private AlbumViewModel mAlbumViewModel;
    private AllAlbumsFragment mAllAlbumsFragment;

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

            // First, determine which menu item was selected

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_add:
                    mTextMessage.setText(R.string.title_add);
                    return true;
/*                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;*/
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Create the recycler and adapter
        final AlbumListAdapter adapter = new AlbumListAdapter(this);

        mAllAlbumsFragment = AllAlbumsFragment.newInstance(adapter);

        // Get a view model for the AlbumViewModel
        mAlbumViewModel = ViewModelProviders.of(this).get(AlbumViewModel.class);

        // Add an observer for any changes to the list of albums. When that
        // change does happen, then update the copy of words that the adapter has saved
        mAlbumViewModel.getAllAlbums().observe(this, new Observer<List<Album>>() {
            @Override
            public void onChanged(@Nullable List<Album> albums) {
                adapter.setAlbums(albums);
            }
        });
    }

}
