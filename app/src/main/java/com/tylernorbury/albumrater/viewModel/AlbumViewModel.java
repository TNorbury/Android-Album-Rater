package com.tylernorbury.albumrater.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.tylernorbury.albumrater.database.entity.Album;
import com.tylernorbury.albumrater.database.repository.AlbumRepository;

import java.util.List;

/**
 * ViewModel that handles the album data
 */
public class AlbumViewModel extends AndroidViewModel {

    private AlbumRepository mRepository;
    private LiveData<List<Album>> mAllAlbums;
    private String mSearchParameter = "";
    private int mCurrentQuerySelection;


    /**
     * Creates a new AlbumViewModel
     * @param application The Application context
     */
    public AlbumViewModel(Application application) {
        super(application);
        mRepository = AlbumRepository.getRepository(application);
        mAllAlbums = mRepository.getAllAlbums();
    }

    /**
     * Get a list of all the albums in the database, using the give code to
     * dictate how all the albums will be sorted
     *
     * @param queryCode The code for the query that'll well use to retrieve the
     *                  data. These values are defined in {@link AlbumRepository}
     *
     * @return The list of albums, sorted by the given query code
     */
    public LiveData<List<Album>> getAlbumsFromQuery(int queryCode) {
        // Update the current query code that's being used
        mCurrentQuerySelection = queryCode;

        // Update the internal list of albums to be ordered by the given query
        // code
        mAllAlbums = mRepository.getAllAlbumsOrdered(queryCode, mSearchParameter);
        return mAllAlbums;
    }

    /**
     * Gets a list of all the albums from the database. Since no query code is
     * given, the currently set query code will be used
     *
     * @return A list of all albums
     */
    public LiveData<List<Album>> getAlbums() {
        return getAlbumsFromQuery(getCurrentQuerySelection());
    }

    /**
     * Insert an album into the database
     * @param album The album to be inserted into the database.
     */
    public void insert(Album album) {
        mRepository.insert(album);
    }

    /**
     * Delete all the records from the database
     */
    public void deleteAll() {
        mRepository.deleteAll();
    }

    /**
     * Deletes an album for the database with the matching primary key
     *
     * @param albumTitle The title of the album to delete
     * @param albumArtist The artist of the album to delete
     */
    public void deleteAlbum(String albumTitle, String albumArtist) {
        mRepository.deleteAlbum(albumTitle, albumArtist);
    }

    /**
     * Update the album with the given "old" primary key, with the values of the
     * "new" album
     *
     * @param oldAlbumTitle The title of the album to update
     * @param oldAlbumArtist The artist of the album to update
     * @param newAlbum The "updated" album
     */
    public void updateAlbum(String oldAlbumTitle, String oldAlbumArtist, Album newAlbum) {
        mRepository.updateAlbum(oldAlbumTitle, oldAlbumArtist, newAlbum);
    }

    /**
     * Updates the search parameter that'll be used when getting albums from
     * the database
     *
     * @param searchQuery The search parameter that will be used when getting
     *                    albums from the database. The parameter will be used
     *                    to find album titles or artist that have the given
     *                    string in their name.
     */
    public void updateSearchParameter(String searchQuery) {
        mSearchParameter = searchQuery;
    }

    /**
     * @return the current query selection
     */
    public int getCurrentQuerySelection() {
        return mCurrentQuerySelection;
    }
}
