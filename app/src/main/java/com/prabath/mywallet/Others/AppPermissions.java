package com.prabath.mywallet.Others;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

public class AppPermissions {

    private static AppPermissions appPermissions = null;

    private AppPermissions() {
    }

    public static AppPermissions newInstance() {
        if (appPermissions == null) {
            appPermissions = new AppPermissions();
        }
        return appPermissions;
    }

    public boolean check(Activity activity, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isAvailable=true;
            for (String permission : permissions) {
                boolean b = activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
                if (!b){
                    isAvailable=false;
                    break;
                }
            }
            return isAvailable;
        }
        return false;
    }

    public int request(Activity activity,String... permissions){
        final int requestCode=2836;
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
        return requestCode;
    }

    public int checkIfNotRequest(Activity activity,String... permissions){
        if (!check(activity,permissions)){
            return request(activity,permissions);
        }
        return -1;
    }


}
