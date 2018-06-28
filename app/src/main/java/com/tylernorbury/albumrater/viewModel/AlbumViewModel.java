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
        // Update the internal list of albums to be ordered by the given query
        // code
        mAllAlbums = mRepository.getAllAlbumsOrdered(queryCode);
        return mAllAlbums;
    }

    /**
     * Insert an album into the database
     * @param album The album to be inserted into the database.
     */
    public void insert(Album album) {
        mRepository.insert(album);
    }
}
