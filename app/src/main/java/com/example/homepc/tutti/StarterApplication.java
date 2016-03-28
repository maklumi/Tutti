package com.example.homepc.tutti;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by HomePC on 27/3/2016.
 */

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
       // Parse.initialize(this);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("AGrjHeAxmiMTPXlSos3QIXFSIAuosYJ9GnfVCGdB")
                .clientKey("oZ9pvlolvcxWWsub2BNZHXanrIXeXNCtCJuvmSdt")
                .server("https://parseapi.back4app.com")
                .build()
        );

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
