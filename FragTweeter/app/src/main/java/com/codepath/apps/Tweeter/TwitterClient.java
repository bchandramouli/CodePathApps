package com.codepath.apps.Tweeter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1/";
	public static final String REST_CONSUMER_KEY = "W8aULwkoCURBU9arBJ3OfghD6";
	public static final String REST_CONSUMER_SECRET = "MwqUmNn7H7YZxfPhymZa0sCrxu0AGXODlU8bD6bsa0XdEjNYQS";
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(int count, long max_id,
                                AsyncHttpResponseHandler handler) {
        /*
         * GET https://api.twitter.com/1.1/statuses/home_timeline.json
         *    - count = 25
         *    - max_id = 1 // Returns the tweets less than max_id!
         */

        String apiUrl = REST_URL + "statuses/home_timeline.json";

        RequestParams params = new RequestParams();
        params.put("count", count);

        if (max_id > Long.MIN_VALUE) {
            params.put("max_id", max_id);
        }

        getClient().get(apiUrl, params, handler);
    }

    public void updateHomeTimeline(int count, long since_id,
                                   AsyncHttpResponseHandler handler) {
        /*
         * GET https://api.twitter.com/1.1/statuses/home_timeline.json
         *    - count = 25
         *    - since_id = 1 // returns the most recent tweets.
         */
        String apiUrl = REST_URL + "statuses/home_timeline.json";

        RequestParams params = new RequestParams();
        params.put("count", count);


        if (since_id > Long.MIN_VALUE) {
            // If it is the first query - skip the since_id - https://dev.twitter.com/rest/public/timelines
            params.put("since_id", since_id);
        }

        getClient().get(apiUrl, params, handler);
    }

    public void getUserSettings(AsyncHttpResponseHandler handler) {
        /*
         * GET https://api.twitter.com/1.1/account/verify_credentials.json
         */
        String apiUrl = REST_URL + "account/verify_credentials.json";

        getClient().get(apiUrl, handler);
    }


    // Composing a new display_tweets
    public void postTweet(String status, long reply_status_id,
                          AsyncHttpResponseHandler handler) {
        String apiUrl = REST_URL + "statuses/update.json";

        RequestParams params = new RequestParams();

        params.put("status", status);
        if (reply_status_id != 0) {
            params.put("in_reply_to_status_id", reply_status_id);
        }

        getClient().post(apiUrl, params, handler);
    }

    public void getMentionsTimeline(int count, AsyncHttpResponseHandler handler) {
        /*
         * GET https://api.twitter.com/1.1/statuses/mentions_timeline.json
         */
        String apiUrl = REST_URL + "statuses/mentions_timeline.json";
        RequestParams params = new RequestParams();
        params.put("count", count);

        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(int count, String handle, AsyncHttpResponseHandler handler) {
        /*
         * GET https://api.twitter.com/1.1/statuses/user_timeline.json
         *    - count = 25
         */

        String apiUrl = REST_URL + "statuses/user_timeline.json";

        RequestParams params = new RequestParams();
        params.put("count", count);
        params.put("screen_name", handle);

        getClient().get(apiUrl, params, handler);
    }
}