package com.example.homiegram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("hC8bYQU58ddWiwiGBfj7TCbtojbIbETmLdSevJrF")
                .clientKey("OBQNxPQ8uyH4E2Mp8OMCiNaz5Hz3HCnQ4V6CTjOz")
                .server("https://parseapi.back4app.com")
                .build());
    }
}
