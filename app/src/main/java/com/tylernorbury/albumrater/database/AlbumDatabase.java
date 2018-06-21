/**
 * Author: Tyler Norbury (tylernorbury96@gmail.com)
 * Version 1.1 (June 20th, 2018)
 */

package com.tylernorbury.albumrater.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.tylernorbury.albumrater.database.converter.DateTimeConverter;
import com.tylernorbury.albumrater.database.dao.AlbumDao;
import com.tylernorbury.albumrater.database.entity.Album;

/**
 * A database that contains all the album reviews
 */
@Database(entities = {Album.class}, version = 1)
@TypeConverters(DateTimeConverter.class)
public abstract class AlbumDatabase extends RoomDatabase {

    // Gets an instance of the Album DAO
    public abstract AlbumDao albumDao();

    // The database will be singleton
    private static AlbumDatabase INSTANCE;

    public static AlbumDatabase getDatabase(final Context context) {

        // If the database hasn't been instantiated, then create it
        if (INSTANCE == null) {

            // Synchronize the creation to prevent race conditions
            synchronized (AlbumDatabase.class) {

                // Double check to make sure that the database wasn't created
                // whilst waiting
                if (INSTANCE == null) {

                    // Create the database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AlbumDatabase.class, "albumDatabase")
                        .build();
                }
            }
        }

        return INSTANCE;
    }
}
