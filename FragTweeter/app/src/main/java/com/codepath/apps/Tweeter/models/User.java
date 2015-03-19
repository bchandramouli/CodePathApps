package com.codepath.apps.Tweeter.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by moulib on 2/21/15.
 */

@Table(name = "Users")
public class User extends Model implements Parcelable {

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "name")
    private String name;

    @Column(name = "profile_url")
    private String profile_url;

    @Column(name = "handle")
    private String handle;

    @Column(name = "description")
    private String tagline;

    @Column(name = "followers")
    private int followers;

    @Column(name = "following")
    private int following;

    // Default constructor
    public User() {
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(uid);
        out.writeString(name);
        out.writeString(profile_url);
        out.writeString(handle);
        out.writeString(tagline);
        out.writeInt(followers);
        out.writeInt(following);
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

    public User(Parcel in) {
        uid = in.readLong();
        name = in.readString();
        profile_url = in.readString();
        handle = in.readString();
        tagline = in.readString();
        followers = in.readInt();
        following = in.readInt();
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

    public String getTagline() {
        return tagline;
    }

    public int getFollowers() {
        return followers;
    }

    public int getFollowing() {
        return following;
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
            this.tagline = userJson.getString("description");
            this.followers = userJson.getInt("followers_count");
            this.following = userJson.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User(User another) {
        this.name = another.name;
        this.uid = another.uid;
        this.profile_url = another.profile_url;
        this.handle = another.handle;
        this.tagline = another.tagline;
        this.followers = another.followers;
        this.following = another.following;
    }
}
