package com.android.freelance.javaretrofit.data.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.freelance.javaretrofit.data.network.responses.model.User;

public class SharedPrefManager {

    private static String LOG_TAG = SharedPreferences.class.getName();
    private static final String SHARED_PREF_NAME = "mySharedPref";
    private static SharedPrefManager mInstance;
    private Context mContext;

    public SharedPrefManager(Context mCtx) {
        Log.i(LOG_TAG, "TEST : SharePrefManager Constructor() is called...");

        this.mContext = mCtx;
    }

    //Singleton Class
    public static synchronized SharedPrefManager getInstance(Context mContext) {
        Log.i(LOG_TAG, "TEST : SharePrefManager getInstnace() is called...");

        if (mInstance == null) {
            mInstance = new SharedPrefManager(mContext);
        }
        return mInstance;
    }

    public void saveUser(User user) {
        Log.i(LOG_TAG, "TEST : saveUser() is called...");

        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        spEditor.putInt("id", user.getId());
        spEditor.putString("email", user.getEmail());
        spEditor.putString("name", user.getName());
        spEditor.putString("school", user.getSchool());

        spEditor.apply();
    }

    public boolean isLoggedIn() {
        Log.i(LOG_TAG, "TEST : isLoggedIn() is called...");

        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sp.getInt("id", -1) != -1; // -1 is meant there's no data.
    }

    public User getUser() {
        Log.i(LOG_TAG, "TEST : getUser() is called...");

        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sp.getInt("id", -1),
                sp.getString("email", null),
                sp.getString("name", null),
                sp.getString("school", null));
    }

    public void clear() {
        Log.i(LOG_TAG, "TEST : clear() is called...");

        SharedPreferences sp = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();
        spEditor.clear();
        spEditor.apply();
    }

}
