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

            // An array of albums to insert into the database.
            Album[] albums = {
                new Album("Larva", "James Gameboy", Album.GOOD_ALBUM),
                new Album("Evil Friends", "Portugal. The Man", Album.GOOD_ALBUM),
                new Album("Pure Heroin", "Lorde", Album.GOOD_ALBUM),
                new Album("When I Was Young", "MØ", Album.GOOD_ALBUM),
                new Album("How Did We Get So Dark?", "Royal Blood", Album.BAD_ALBUM),
                new Album("You're Gonna Miss It All", "Modern Baseball", Album.GOOD_ALBUM),
                new Album("Big Fish Theory", "Vince Staples", Album.GOOD_ALBUM),
                new Album("Cozy Tapes Vol. 2: Too Cozy", "A$AP Mob", Album.BAD_ALBUM),
                new Album("Black Sabbath", "Black Sabbath", Album.GOOD_ALBUM),
                new Album("KIDS SEE GHOSTS", "KIDS SEE GHOSTS", Album.GOOD_ALBUM),
                new Album("More Life", "Drake", Album.BAD_ALBUM),
                new Album("Views", "Drake", Album.BAD_ALBUM)
            };

            // Go through all the albums above and insert them into the database
            for (Album album: albums) {
                mDao.insert(album);
            }

            return null;
        }
    }
}
