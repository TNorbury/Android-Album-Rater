package com.tylernorbury.albumrater.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.database.entity.Album;
import com.tylernorbury.albumrater.fragment.AlbumInfoFragment;
import com.tylernorbury.albumrater.fragment.EditAlbumFragment;

public class AlbumInfoActivity extends AppCompatActivity implements AlbumInfoFragment.OnAlbumDeletedListener, AlbumInfoFragment.OnAlbumEditEventListener {

    /**
     * Indicates that the calling activity should delete the album associated
     * with this activity
     */
    public static final int RESULT_DELETE = 1;

    /**
     * Indicates that the calling activity should update the album associated
     * with this activity
     */
    public static final int RESULT_EDIT = 2;

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

    @Override
    public void onAlbumEditEvent(Album album, int editCode) {
        // Swap out the fragments so that we're displaying the fragment for
        // editing albums
        Toast.makeText(this, "Edit Album Button Clicked", Toast.LENGTH_SHORT).show();

        // If we're starting an edit, then we want to swap fragments so that
        // we're displaying the edit album fragment
        if (editCode == EDIT_ALBUM_START_CODE) {
            // Create a new edit album fragment
            EditAlbumFragment fragment = new EditAlbumFragment();

            // Pass the values of the original album to fragment
            Bundle args = new Bundle();
            args.putString(getString(R.string.original_album_title), album.getTitle());
            args.putString(getString(R.string.original_album_artist), album.getArtist());
            args.putInt(getString(R.string.original_album_rating), album.getRating());

            // As it stands I don't think I'll be needing this two values as they
            // won't be changed by the user, but I'm leaving the code here just in case
            /*args.putString(getString(R.string.original_album_review), album.getReview());
            args.putSerializable(getString(R.string.original_album_date), album.getReviewDate());*/

            // Attach the arguments to the fragment
            fragment.setArguments(args);

            // Perform a fragment transaction to display the edit album fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.album_info_fragment_holder, fragment)
                    .addToBackStack("")
                    .commit();
        }

        // Otherwise, if we're canceling the edit, then we just want to swap
        // fragments so that we're displaying the album info fragment
        else if (editCode == EDIT_ALBUM_CANCEL_CODE) {
            // Since one can only get to the album edit fragment via the album
            // info fragment, and in the process of that the album info fragment
            // is added to the back stack, all we need to do is pop the back stack
            getSupportFragmentManager().popBackStack();
        }

        // Otherwise, if we're submitting the edit then we want to propagate
        // that back to the main activity so that it can handle updating the
        // database
        else if (editCode == EDIT_ALBUM_SUBMIT_CODE) {
            // Create a reply intent that we'll use to get back to the main activity
            Intent reply = new Intent();

            // We want to pass the arguments that this activity was supplied
            // (which are the values of the pre-edit album)
            reply.putExtras(getIntent().getExtras());

            // We also want to pass the values of the updated album
            reply.putExtra(getString(R.string.new_album_title), album.getTitle());
            reply.putExtra(getString(R.string.new_album_artist), album.getArtist());
            reply.putExtra(getString(R.string.new_album_rating), album.getRating());
            reply.putExtra(getString(R.string.new_album_review), album.getReview());
            reply.putExtra(getString(R.string.new_album_date), album.getReviewDate());

            // Indicate to the calling activity that this result is for
            // submitting an edit
            setResult(RESULT_EDIT);
        }
    }
}
