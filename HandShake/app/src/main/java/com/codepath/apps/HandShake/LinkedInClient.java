package com.codepath.apps.HandShake;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.LinkedInApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

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
public class LinkedInClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = LinkedInApi.class;
	public static final String REST_URL = "https://api.linkedin.com/v1/";
	public static final String REST_CONSUMER_KEY = "757d26tgx8wpgs"; /* mouli.biz */
	public static final String REST_CONSUMER_SECRET = "ObPp7i7rZWCGqsIq"; /* mouli.biz */
    public static final String MOULI_REST_CONSUMER_KEY = "75hbnjwqb5ipjj";
    public static final String MOULI_REST_CONSUMER_SECRET = "UI8SQ6fdpXcIe9Di";
	public static final String REST_CALLBACK_URL = "oauth://cphandshake";



	public LinkedInClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, MOULI_REST_CONSUMER_KEY, MOULI_REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
        /*
         * GET http://api.linkedin.com/v1/people/~/connections?format=json
         */
        String apiUrl = REST_URL + "people/~/connections?format=json";

        getClient().get(apiUrl, handler);
    }

    public void getUserSettings(AsyncHttpResponseHandler handler) {
        /*
         * GET http://api.linkedin.com/v1/people/~?format=json
         */
        String apiUrl = REST_URL + "people/~?format=json";

        getClient().get(apiUrl, handler);
    }
}