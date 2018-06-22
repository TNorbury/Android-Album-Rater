/**
 * Author: Tyler Norbury (tylernorbury96@gmail.com)
 * Version 1.1 (June 20th, 2018)
 */

package com.tylernorbury.albumrater.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Represents a database entity for an album and its corresponding review.
 */
@Entity(tableName = "album", primaryKeys = {"title", "artist"})
public class Album {

    // The title of the album
    @NonNull
    @ColumnInfo(name = "title")
    private String mTitle;

    // The artist who made the album
    @NonNull
    @ColumnInfo(name = "artist")
    private String mArtist;

    // The user's numerical rating of the album (this will probably just be a
    // thumbs up/down sorta thing
    @ColumnInfo(name = "rating")
    private int mRating;

    // The user's review (in words) of the album
    @ColumnInfo(name = "review")
    private String mReview;

    // The date the review was made
    @ColumnInfo(name = "reviewDate")
    private GregorianCalendar mReviewDate;

    /**
     * Creates a new album that only has a rating, and no review

     * @param title The title of the album
     * @param artist The artist who made the album
     * @param rating The user's rating of the album
     */
    @Ignore
    public Album(@NonNull String title, @NonNull String artist, int rating) {
        this(title, artist, rating, "");
    }

    /**
     * Create a new album entity
     * @param title The title of the album
     * @param artist The artist who made the album
     * @param rating The user's numerical rating of the album
     * @param review The user's review (in words) of the album
     */
    public Album(@NonNull String title, @NonNull String artist, int rating,
                 String review) {
        mTitle = title;
        mArtist = artist;
        mRating = rating;
        mReview = review;

        // Set the review's date as the current time
        mReviewDate = new GregorianCalendar();
        mReviewDate.setTimeInMillis(System.currentTimeMillis());
    }

    /**
     * @return The title of the album
     */
    @NonNull
    public String getTitle() {
        return mTitle;
    }

    /**
     * @return The artist who made the album
     */
    @NonNull
    public String getArtist() {
        return mArtist;
    }

    /**
     * @return The numerical rating of the album
     */
    public int getRating() {
        return mRating;
    }

    /**
     * @return The worded review of the album
     */
    public String getReview() {
        return mReview;
    }

    /**
     * @return The date when the review was posted
     */
    public GregorianCalendar getReviewDate() {
        return mReviewDate;
    }

    /**
     * Set the date that the review was created.
     * @param date The date of the review
     */
    public void setReviewDate(GregorianCalendar date) {
        mReviewDate = date;
    }
}
