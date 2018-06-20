package com.tylernorbury.albumrater.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import com.tylernorbury.albumrater.database.entity.Album;

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
    @Insert
    void insert(Album album);
}
