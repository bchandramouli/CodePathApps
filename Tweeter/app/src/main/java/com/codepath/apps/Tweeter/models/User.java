package com.codepath.apps.Tweeter.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by moulib on 2/21/15.
 */


public class User implements Parcelable {
    private String name;
    private long uid;
    private String profile_url;
    private String handle;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeLong(uid);
        out.writeString(profile_url);
        out.writeString(handle);
    }

    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        name = in.readString();
        uid = in.readLong();
        profile_url = in.readString();
        handle = in.readString();
    }

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
