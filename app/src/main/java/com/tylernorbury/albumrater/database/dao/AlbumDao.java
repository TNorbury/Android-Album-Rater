/**
 * Author: Tyler Norbury (tylernorbury96@gmail.com)
 * Version 1.1 (June 20th, 2018)
 */

package com.tylernorbury.albumrater.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.tylernorbury.albumrater.database.entity.Album;

import java.util.List;

/**
 * A DAO for accessing the album table
 */
@Dao
public interface AlbumDao {

    /**
     * Insert a new album (and review) into the database
     *
     * @param album The album that has been reviewed
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Album album);

    /**
     * Gets all the albums that have been reviewed, ordered by the name of the
     * album
     * @return All the albums that've been reviewed.
     */
    @Query("SELECT title, artist, rating, review, reviewDate FROM album " +
            "ORDER BY title ASC")
    LiveData<List<Album>> getAllAlbums();


    /**
     * @return a handful of the most recently reviewed albums
     */
    @Query("SELECT title, artist, rating, review, reviewDate FROM album " +
            "ORDER BY datetime(reviewDate) " +
            "LIMIT 5")
    LiveData<List<Album>> getRecentAlbums();

    @Query("DELETE FROM album")
    void deleteAll();

    // TODO add some queries for searching for albums by title/artist name

    /**
     * @return All albums, ordered by title, ascending
     */
    @Query("SELECT * FROM ALBUM " +
            "WHERE title LIKE :searchParameter OR artist like :searchParameter " +
            "ORDER BY title ASC")
    LiveData<List<Album>> getAllAlbumsOrderedTitleASC(String searchParameter);

    /**
     * @return all albums, ordered by title, descending
     */
    @Query("SELECT * FROM ALBUM " +
            "WHERE title LIKE :searchParameter OR artist like :searchParameter " +
            "ORDER BY title DESC")
    LiveData<List<Album>> getAllAlbumsOrderedTitleDESC(String searchParameter);

    /**
     * @return all albums, ordered by artist's name, ascending
     */
    @Query("SELECT * FROM ALBUM " +
            "WHERE title LIKE :searchParameter OR artist like :searchParameter " +
            "ORDER BY artist ASC")
    LiveData<List<Album>> getAllAlbumsOrderedArtistASC(String searchParameter);

    /**
     * @return all albums, ordered by artist's name, descending
     */
    @Query("SELECT * FROM ALBUM " +
            "WHERE title LIKE :searchParameter OR artist like :searchParameter " +
            "ORDER BY artist DESC")
    LiveData<List<Album>> getAllAlbumsOrderedArtistDESC(String searchParameter);

    /**
     * @return all albums, ordered by the album's rating, ascending
     */
    @Query("SELECT * FROM ALBUM " +
            "WHERE title LIKE :searchParameter OR artist like :searchParameter " +
            "ORDER BY rating ASC")
    LiveData<List<Album>> getAllAlbumsOrderedRatingASC(String searchParameter);

    /**
     * @return all albums, ordered by the album's rating, descending
     */
    @Query("SELECT * FROM ALBUM " +
            "WHERE title LIKE :searchParameter OR artist like :searchParameter " +
            "ORDER BY rating DESC")
    LiveData<List<Album>> getAllAlbumsOrderedRatingDESC(String searchParameter);

    /**
     * @return all albums, ordered by the date of the review, ascending
     */
    @Query("SELECT * FROM ALBUM " +
            "WHERE title LIKE :searchParameter OR artist like :searchParameter " +
            "ORDER BY reviewDate ASC")
    LiveData<List<Album>> getAllAlbumsOrderedDateASC(String searchParameter);

    /**
     * @return all albums, ordered by the date of the review, descending
     */
    @Query("SELECT * FROM ALBUM " +
            "WHERE title LIKE :searchParameter OR artist like :searchParameter " +
            "ORDER BY reviewDate DESC")
    LiveData<List<Album>> getAllAlbumsOrderedDateDESC(String searchParameter);
}
