package pbs.researchmobile.com.mach;


import android.app.Application;

import com.facebook.stetho.Stetho;

public class FirebaseChatMainApp extends Application {
    private static boolean sIsChatActivityOpen = false;

    public static boolean isChatActivityOpen() {
        return sIsChatActivityOpen;
    }

    public static void setChatActivityOpen(boolean isChatActivityOpen) {
        FirebaseChatMainApp.sIsChatActivityOpen = isChatActivityOpen;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}