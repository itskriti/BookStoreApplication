package com.example.bookstoreapplication.utils;

import android.app.Activity;
import android.view.View;

public class UserInterfaceUtils {

    public static void statusBarIcons(Boolean isDark, Activity activity){
        if(isDark)
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        else
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

    }
}
