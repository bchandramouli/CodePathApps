package com.codepath.apps.HandShake;

import android.content.Context;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     RestClient client = RestApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class LinkedInApp extends com.activeandroid.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		LinkedInApp.context = this;
	}

	public static LinkedInClient getRestClient() {
		return (LinkedInClient) LinkedInClient.getInstance(LinkedInClient.class, LinkedInApp.context);
	}
}