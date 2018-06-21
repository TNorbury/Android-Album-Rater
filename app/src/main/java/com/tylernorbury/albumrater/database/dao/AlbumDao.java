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
            "ORDER BY title DESC")
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
}
