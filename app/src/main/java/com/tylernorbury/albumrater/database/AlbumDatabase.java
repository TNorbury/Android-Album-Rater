/**
 * Author: Tyler Norbury (tylernorbury96@gmail.com)
 * Version 1.1 (June 20th, 2018)
 */

package com.tylernorbury.albumrater.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

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
                        .addCallback(sAlbumDatabaseCallback)
                        .build();
                }
            }
        }

        return INSTANCE;
    }

    /**
     * Add a callback that gets fired when the database is opened. As it stands
     * this will only be used for testing purposes.
     */
    private static AlbumDatabase.Callback sAlbumDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * This class gets used during the callback function above.
     * It's function is to insert some dummy/test data into the database.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AlbumDao mDao;

        PopulateDbAsync(AlbumDatabase db) {
            mDao = db.albumDao();
        }


        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();

            // Create a new album
            Album album = new Album("Larva", "James Gameboy",
                    1);

            // Insert the album into the database
            mDao.insert(album);

            // Insert some other albums
            album = new Album("Evil Friends", "Portugal. The Man", 1);
            mDao.insert(album);

            album = new Album("Pure Heroin", "Lorde", 1);
            mDao.insert(album);

            album = new Album("When I Was Young", "MØ", 1);
            mDao.insert(album);

            return null;
        }
    }
}
