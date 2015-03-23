package com.codepath.apps.HandShake.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.annotation.Table;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;

/**
 * Created by moulib on 2/21/15.
 */

@Table(name = "Users")
public class User extends Model implements Parcelable {

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String uid;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "industry")
    private String industry;

    @Column(name = "description")
    private String description;


    // Default constructor
    public User() {
        super();
    }

    public String getUid() {
        return uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getIndustry() {
        return industry;
    }

    public String getDescription() {
        return description;
    }

    // De-serialize JSON to Java object
    public User(JSONObject userJson) {
        try {
            this.uid = userJson.getString("id");
            this.firstName = userJson.getString("firstName");
            this.lastName = userJson.getString("lastName");
            this.description = userJson.getString("headline");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            this.industry = userJson.getString("industry");
        } catch (JSONException e) {
            this.industry = "";
        }

        try {
            this.imageUrl = userJson.getString("siteStandardProfileRequest").getString("url");
        } catch (JSONException e) {
            this.imageUrl = "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(uid);
        out.writeString(firstName);
        out.writeString(lastName);
        out.writeString(imageUrl);
        out.writeString(industry);
        out.writeString(description);
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
        uid = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        imageUrl = in.readString();
        industry = in.readString();
        description = in.readString();
    }
}
