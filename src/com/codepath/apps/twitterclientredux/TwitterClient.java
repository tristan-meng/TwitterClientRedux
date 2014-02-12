package com.codepath.apps.twitterclientredux;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.net.Uri;

import android.util.Log;
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
 * NOTE: You may want to rename this object based on the service i.e TwitterClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "MDZc8PjPm9cZBk60pMEZ8A";
    public static final String REST_CONSUMER_SECRET = "nQc8VfdNFJ2LHtevLpBHK2vx0IFZCLxjbpM41Wzm7hU";
    public static final String REST_CALLBACK_URL = "oauth://twitterclientredux"; 
    public static final int FETCH_TWEETS_NUM = 25;
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */

    public void getHomeTimeline(AsyncHttpResponseHandler handler, String maxId) {
        String url = getApiUrl("statuses/home_timeline.json?include_rts=1&count=" + FETCH_TWEETS_NUM);	
        if (!maxId.equals(""))
        	url += "&max_id=" + maxId;
        Log.d("DEBUG", "query home timeline url: " + url);
        client.get(url, null, handler);
        
    }
    
    public void getUserProfile(AsyncHttpResponseHandler handler, String screenName) {
    	String url;
    	if (screenName == null || screenName.length()==0) // Get own profile
            url = getApiUrl("account/verify_credentials.json");
    	else  // Get other's profile
    		url = getApiUrl("users/show.json?screen_name=" + screenName);
        client.get(url, null, handler);
    }
    
    
    public void getUserTimeline(AsyncHttpResponseHandler handler, String screenName, String maxId) {
        String url = getApiUrl("statuses/user_timeline.json?include_rts=1&count=" + FETCH_TWEETS_NUM);
    	if (screenName!=null && screenName.length()>0) { // Get other's timeline
    		url += "&screen_name=" + screenName;
    	}
        if (!maxId.equals(""))
        	url += "&max_id=" + maxId;
        Log.d("DEBUG", "query user timeline url: " + url);
        client.get(url, null, handler);
    }
    

    public void getMentions(AsyncHttpResponseHandler handler, String maxId) {
        String url = getApiUrl("statuses/mentions_timeline.json?count=" + FETCH_TWEETS_NUM);	
        if (!maxId.equals(""))
        	url += "&max_id=" + maxId;
        Log.d("DEBUG", "query mentions url: " + url);
        client.get(url, null, handler);
        
    }

    public void getTweetDetail(AsyncHttpResponseHandler handler, Long tweetId) {
        String url = getApiUrl("statuses/show.json?id=" + tweetId);	
        Log.d("DEBUG", "query tweet detail url: " + url);
        client.get(url, null, handler);
        
    }
    
    public void postNewTweet(AsyncHttpResponseHandler handler, String text) {
        String url = getApiUrl("statuses/update.json?status=" + Uri.encode(text));	
        client.post(url, null, handler);
    }
}