/**
 * Author: Tyler Norbury (tylernorbury96@gmail.com)
 * Version 1.1
 */

package com.tylernorbury.albumrater.database.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.tylernorbury.albumrater.database.AlbumDatabase;
import com.tylernorbury.albumrater.database.dao.AlbumDao;
import com.tylernorbury.albumrater.database.entity.Album;

import java.util.List;

/**
 * Repository for accessing album data
 */
public class AlbumRepository {

    // We want the repository to be a singleton
    private static AlbumRepository INSTANCE;

    private AlbumDao mAlbumDao;
    private LiveData<List<Album>> mAllAlbums;

    /**
     * Create an album repository
     * @param application The application context
     */
    private AlbumRepository(Application application) {

        // Get the DAO and all the albums
        mAlbumDao = AlbumDatabase.getDatabase(application).albumDao();
        mAllAlbums = mAlbumDao.getAllAlbums();
    }

    /**
     * Gets the repository
     * @param application The application context
     * @return The Album repository
     */
    public static AlbumRepository getRepository(Application application) {
        // If the repository hasn't been created, then create it
        if (INSTANCE == null) {
            synchronized (AlbumRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AlbumRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * @return Returns all the albums
     */
    public LiveData<List<Album>> getAllAlbums() {
        return mAllAlbums;
    }

    /**
     * Insert an album into the repository.
     * @param album The album to be inserted
     */
    public void insert(Album album) {
        new insertAsyncTask(mAlbumDao).execute(album);
    }

    /**
     * Helper class that handles creating threads to handle the insertion of
     * albums into the repository.
     */
    private static class insertAsyncTask extends AsyncTask<Album, Void, Void> {

        private AlbumDao mAsyncDao;

        /**
         * Create a new async task
         * @param dao The DAO that the task will use.
         */
        public insertAsyncTask(AlbumDao dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final Album... params) {
            mAsyncDao.insert(params[0]);
            return null;
        }
    }
}
