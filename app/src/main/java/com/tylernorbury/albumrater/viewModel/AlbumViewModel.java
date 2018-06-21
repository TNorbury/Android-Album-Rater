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
     * @return All of the albums
     */
    public LiveData<List<Album>> getAllAlbums() {
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
