package com.tylernorbury.albumrater.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.database.entity.Album;
import com.tylernorbury.albumrater.fragment.AlbumInfoFragment;

public class AlbumInfoActivity extends AppCompatActivity implements AlbumInfoFragment.OnAlbumDeletedListener{

    public static final int RESULT_DELETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_info);

        // Get the arguments supplied
        // Bundle args = getIntent().getExtras();

        // Create and display the album info fragment
        AlbumInfoFragment fragment = new AlbumInfoFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.album_info_fragment_holder, fragment)
                .commit();
    }

    @Override
    public void onAlbumDeleted(Album album) {
        // Since our the user decided to delete the album, we want to return to
        // the activity that called us and tell it to delete the album
        Intent reply = new Intent();

        // Give the reply intent the primary key of the album to delete
        reply.putExtra(getString(R.string.album_title_key), album.getTitle());
        reply.putExtra(getString(R.string.album_artist_key), album.getArtist());

        // Indicate what type of result is being returned from this activity and
        // then "finish" this activity
        setResult(RESULT_DELETE, reply);
        finish();
    }
}
