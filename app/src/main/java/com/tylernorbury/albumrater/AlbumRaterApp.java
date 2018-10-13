package com.tylernorbury.albumrater;

import android.app.Application;
import android.content.Context;

/**
 * Class which serves as an interface to the application context
 */
public class AlbumRaterApp extends Application
{

    private static Context mContext;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = getApplicationContext();
    }

    /**
     * @return the application's context
     */
    public static Context getContext()
    {
        return AlbumRaterApp.mContext;
    }

}
