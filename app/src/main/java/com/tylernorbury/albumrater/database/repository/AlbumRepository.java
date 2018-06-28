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

    // We'll use these codes to determine which query we'll call. The different
    // codes represent a different combination of column name and ordering
    public static final int QUERY_TITLE_ASC = 1;
    public static final int QUERY_TITLE_DESC = 2;
    public static final int QUERY_ARTIST_ASC = 3;
    public static final int QUERY_ARTIST_DESC = 4;
    public static final int QUERY_RATING_ASC = 5;
    public static final int QUERY_RATING_DESC = 6;
    public static final int QUERY_DATE_ASC = 7;
    public static final int QUERY_DATE_DESC = 8;

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
     * Get all the albums in the database, ordering the results by the given
     * parameters
     *
     * @param queryCode The code for the query that'll well use to retrieve the
     *                  data.
     *
     * @return an ordered list of the albums in the database.
     */
    public LiveData<List<Album>> getAllAlbumsOrdered(int queryCode) {

        // Depending on the given query code, calling the corresponding query
        // from the DAO
        switch (queryCode) {
            case QUERY_TITLE_ASC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedTitleASC();
                break;

            case QUERY_TITLE_DESC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedTitleDESC();
                break;

            case QUERY_ARTIST_ASC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedArtistASC();
                break;

            case QUERY_ARTIST_DESC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedArtistDESC();
                break;

            case QUERY_RATING_ASC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedRatingASC();
                break;

            case QUERY_RATING_DESC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedRatingDESC();
                break;

            case QUERY_DATE_ASC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedDateASC();
                break;

            case QUERY_DATE_DESC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedDateDESC();
                break;
        }

        return  mAllAlbums;
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
