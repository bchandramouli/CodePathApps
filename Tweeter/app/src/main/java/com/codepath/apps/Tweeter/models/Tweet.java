package com.codepath.apps.Tweeter.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by moulib on 2/21/15.
 */
public class Tweet {
    // display_tweets attributes
    private String body;
    private long id;
    private User user;
    private String timestamp;
    private int favorites;
    private int retweets;

    public String getBody() {
        return body;
    }

    public long getId() {
        return id;
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
           tweet.id = jsonTweet.getLong("id");
           tweet.body = jsonTweet.getString("text");
           tweet.user = new User(jsonTweet.getJSONObject("user"));

           boolean count = jsonTweet.getBoolean("retweeted");
           tweet.retweets = count ? jsonTweet.getInt("retweet_count") : 0;

           count = jsonTweet.getBoolean("favorited");
           tweet.favorites = (count) ? jsonTweet.getInt("favourites_count") : 0;

           queryCtrs.setSinceId(tweet.getId());
           queryCtrs.setMaxId(tweet.getId());

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
}
