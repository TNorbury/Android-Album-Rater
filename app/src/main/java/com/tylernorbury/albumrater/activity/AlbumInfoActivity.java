package com.tylernorbury.albumrater.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.fragment.AlbumInfoFragment;

public class AlbumInfoActivity extends AppCompatActivity {

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
}
