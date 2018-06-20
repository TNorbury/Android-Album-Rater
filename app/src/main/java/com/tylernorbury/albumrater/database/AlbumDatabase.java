/**
 * Author: Tyler Norbury (tylernorbury96@gmail.com)
 * Version 1.1 (June 20th, 2018)
 */

package com.tylernorbury.albumrater.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.tylernorbury.albumrater.database.entity.Album;

/**
 * A database
 */
@Database(entities = {Album.class}, version = 1)
public abstract class AlbumDatabase extends RoomDatabase {

}
