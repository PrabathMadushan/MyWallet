package com.prabath.mywallet.Others;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class Init {


    private static final String KEY_FIRST_TIME = "FIST_TIME_KEY";
    private static final String FIRST_TIME = "FIST_TIME";

    private static Init init;

    private File imageFolderRecord;
    private File imageFolderProfile;

    private Activity activity;

    private Init(Activity activity) {
        this.activity = activity;
    }


    public static Init getInstance(Activity activity) {
        if (init == null) init = new Init(activity);
        return init;
    }

    /**
     * Don't call fist time
     */
    public static Init getInstance() {
        if (init != null) return init;
        return null;
    }

    public void setup() {

        SharedPreferences sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
        if (sharedPreferences.getAll().isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_FIRST_TIME, FIRST_TIME);
            editor.apply();
        }
        init();
    }

    private void init() {
        //Create profile image folder
        File filesDir = activity.getBaseContext().getFilesDir();
        imageFolderProfile = new File(filesDir, "profile");
        if(!imageFolderProfile.exists())imageFolderProfile.mkdir();
        //Create record image folder
        imageFolderRecord = new File(filesDir, "records");
        if(!imageFolderRecord.exists())imageFolderRecord.mkdir();
    }

    public File getImageFolderRecord() {
        return imageFolderRecord;
    }

    public File getImageFolderProfile() {
        return imageFolderProfile;
    }
}
