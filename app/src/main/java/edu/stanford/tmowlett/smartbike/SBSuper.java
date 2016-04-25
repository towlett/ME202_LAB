package edu.stanford.tmowlett.smartbike;

import android.app.Application;

import com.firebase.client.Firebase;

public class SBSuper extends Application {
    String UID;
    String UIDinSQLite;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

    public void setUID(String uidIn) {
        UID = uidIn;
    }

    public String getUID() {
        return UID;
    }

    public void setUIDinSQLite(String uidIn){
        UIDinSQLite = uidIn;
    }

    public String getUIDinSQLite(){
        return UIDinSQLite;
    }
}
