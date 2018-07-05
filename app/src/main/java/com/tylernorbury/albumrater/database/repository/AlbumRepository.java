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
    public static final int QUERY_TITLE_ASC = 0;
    public static final int QUERY_TITLE_DESC = 1;
    public static final int QUERY_ARTIST_ASC = 2;
    public static final int QUERY_ARTIST_DESC = 3;
    public static final int QUERY_RATING_ASC = 4;
    public static final int QUERY_RATING_DESC = 5;
    public static final int QUERY_DATE_ASC = 6;
    public static final int QUERY_DATE_DESC = 7;

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
    public LiveData<List<Album>> getAllAlbumsOrdered(int queryCode, String searchParameter) {

        // Depending on the given query code, calling the corresponding query
        // from the DAO
        switch (queryCode) {
            case QUERY_TITLE_ASC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedTitleASC(formatSearchParameter(searchParameter));
                break;

            case QUERY_TITLE_DESC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedTitleDESC(formatSearchParameter(searchParameter));
                break;

            case QUERY_ARTIST_ASC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedArtistASC(formatSearchParameter(searchParameter));
                break;

            case QUERY_ARTIST_DESC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedArtistDESC(formatSearchParameter(searchParameter));
                break;

            case QUERY_RATING_ASC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedRatingASC(formatSearchParameter(searchParameter));
                break;

            case QUERY_RATING_DESC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedRatingDESC(formatSearchParameter(searchParameter));
                break;

            case QUERY_DATE_ASC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedDateASC(formatSearchParameter(searchParameter));
                break;

            case QUERY_DATE_DESC:
                mAllAlbums = mAlbumDao.getAllAlbumsOrderedDateDESC(formatSearchParameter(searchParameter));
                break;
        }

        return  mAllAlbums;
    }

    /**
     * Insert an album into the repository.
     * @param album The album to be inserted
     */
    public void insert(Album album) {
        new InsertAsyncTask(mAlbumDao).execute(album);
    }

    /**
     * Deletes all the albums from the database
     */
    public void deleteAll() {
        // Create a new asynchronous task to clear the database
        new DeleteAllAsyncTask(mAlbumDao).execute();
    }

    public LiveData<Album> getAlbum(String albumTitle, String albumArtist) {
        return mAlbumDao.getAlbum(albumTitle, albumArtist);
    }

    /**
     * Formats the given search parameter to make it better for searching
     *
     * @param searchParameter The parameter to format
     * @return a formatted search parameter
     */
    private String formatSearchParameter(String searchParameter) {
        StringBuilder sb = new StringBuilder();

        // Firstly we want to put an SQL wild card character at the start of the
        // parameter
        sb.append("%");

        // Next we want to append the search parameter itself
        sb.append(searchParameter);

        // Finally, we want to append an SQL wild card character to the end of
        // the parameter
        sb.append("%");

        return sb.toString();
    }


    /**
     * Helper class that creates thread(s) to clear the database
     */
    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private AlbumDao mAsyncDao;

        public DeleteAllAsyncTask(AlbumDao dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncDao.deleteAll();
            return null;
        }
    }

    /**
     * Helper class that handles creating threads to handle the insertion of
     * albums into the repository.
     */
    private static class InsertAsyncTask extends AsyncTask<Album, Void, Void> {

        private AlbumDao mAsyncDao;

        /**
         * Create a new async task
         * @param dao The DAO that the task will use.
         */
        public InsertAsyncTask(AlbumDao dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final Album... params) {
            mAsyncDao.insert(params[0]);
            return null;
        }
    }
}
