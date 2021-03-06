package com.codepath.apps.Tweeter.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by moulib on 2/21/15.
 */
@Table(name = "Tweets")
public class Tweet extends Model implements Parcelable {
    // display_tweets attributes
    @Column(name = "id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uId;

    @Column(name = "body")
    private String body;

    @Column(name = "user")
    private User user;

    @Column(name = "timestamp")
    private String timestamp;

    @Column(name = "favorites")
    private int favorites;

    @Column(name = "retweets")
    private int retweets;

    @Column(name = "mediaUrl")
    private String mediaUrl;

    public String getBody() {
        return body;
    }

    public long getUId() {
        return uId;
    }

    public User getUser() {
        return user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getFavorites() {
        return favorites;
    }

    public int getRetweets() {
        return retweets;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    // Default Constructor
    public Tweet() {
        super();
    }

    /*
         {
             "created_at": "Tue Aug 28 21:16:23 +0000 2012",
             "favorited": false,
             "text": "just another test",
             "id": 240558470661799936,
             "retweet_count": 0,
             "user": { ... }
         }
     */

   // De-serialize the json into the Java Object
   public static Tweet tweetFromJsonObj(JSONObject jsonTweet) {
       QueryCtrs queryCtrs = QueryCtrs.getInstance();
       Tweet tweet = new Tweet();

       try {
           tweet.timestamp = jsonTweet.getString("created_at");
           tweet.uId = jsonTweet.getLong("id");
           tweet.body = jsonTweet.getString("text");
           tweet.user = new User(jsonTweet.getJSONObject("user"));
           tweet.mediaUrl = "";

           /* Use this to display retweet objects differently
            * boolean count = jsonTweet.getBoolean("retweeted");
            * count = jsonTweet.getBoolean("favorited");
            */

           try {
               tweet.retweets = jsonTweet.getInt("retweet_count");
           } catch (JSONException e) {
               tweet.retweets = 0;
           }

           try {
               tweet.favorites = jsonTweet.getInt("favourites_count");
           } catch (JSONException e) {
               tweet.favorites = 0;
           }

           try {
               JSONArray media = jsonTweet.getJSONObject("extended_entities").getJSONArray("media");
               for (int i = 0; i < media.length(); i++) {
                   tweet.mediaUrl = media.getJSONObject(i).getString("media_url");
               }
           } catch (JSONException e) {
               tweet.mediaUrl = "";
           }

           queryCtrs.setSinceId(tweet.getUId());
           queryCtrs.setMaxId(tweet.getUId());

       } catch (JSONException e) {
           e.printStackTrace();
           return null;
       }
       return tweet;
   }

   // Return a set of tweets from a JSON array
   public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArr) {
       ArrayList<Tweet> arrList = new ArrayList<>();

       for (int i=0; i < jsonArr.length(); i++) {
           try {
               JSONObject jobj= jsonArr.getJSONObject(i);
               Tweet tweet = Tweet.tweetFromJsonObj(jobj);
               if (tweet != null) {

                   arrList.add(tweet);
               }
           } catch (JSONException e) {
               e.printStackTrace();
               continue; // if one display_tweets fails, continue parsing the rest!
           }
       }
       return arrList;
   }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uId);
        dest.writeString(this.body);
        dest.writeParcelable(this.user, 0);
    }

    private Tweet(Parcel in) {
        this.uId = in.readLong();
        this.body = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
