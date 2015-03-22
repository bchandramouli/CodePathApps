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
public class User extends Model {

    @Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "industry")
    private String industry;

    // Default constructor
    public User() {
        super();
    }

    public long getUid() {
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

    // De-serialize JSON to Java object
    public User(JSONObject userJson) {
        try {
            this.firstName = userJson.getString("firstName");
            this.lastName = userJson.getString("lastName");
            this.industry = userJson.getString("industry");
            this.imageUrl = userJson.getString("imageUrl");
            this.uid = userJson.getLong("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
