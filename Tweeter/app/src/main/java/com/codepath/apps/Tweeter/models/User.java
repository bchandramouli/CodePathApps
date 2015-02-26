package com.codepath.apps.Tweeter.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by moulib on 2/21/15.
 */


public class User {
    private String name;
    private String profile_url;
    private long uid;
    private String handle;

    public String getName() {
        return name;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public long getUid() {
        return uid;
    }

    public String getHandle() {
        return handle;
    }

    // De-serialize JSON to Java object
    public User(JSONObject userJson) {
        /*
           "user": {
              "name": "OAuth Dancer",
              "profile_image_url":"http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
              "location": "San Francisco, CA",
              "url": "http://bit.ly/oauth-dancer",
              "screen_name": "oauth_dancer"
              "id": 119476949,
            },
        */
        try {
            this.name = userJson.getString("name");
            this.uid = userJson.getLong("id");
            this.profile_url = userJson.getString("profile_image_url");
            this.handle = userJson.getString("screen_name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
