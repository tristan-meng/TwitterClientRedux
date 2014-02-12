package com.codepath.apps.twitterclientredux.fragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.twitterclientredux.TwitterClientApp;
import com.codepath.apps.twitterclientredux.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineFragment extends TweetListFragment {
    TweetListFragment fragmentTimeline;
    Tweet newTweet;
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments()!=null)
		    this.newTweet = (Tweet)getArguments().getSerializable("new_tweet");
	}
	
    public void showProgressBar() {
    	if (getActivity()!=null)
	        getActivity().setProgressBarIndeterminateVisibility(true);
	}
	    
	public void hideProgressBar() {
		if (getActivity()!=null)
	  	    getActivity().setProgressBarIndeterminateVisibility(false);
	}
	

	/* Implement the abstract method
	 * (non-Javadoc)
	 * @see com.codepath.apps.twitterclientredux.fragments.TweetListFragment#fetchTweets(java.lang.String)
	 */
	protected void fetchTweets(String id) {
		showProgressBar();
        TwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
        	@SuppressWarnings("unchecked")
			@Override
        	public void onSuccess(JSONArray jsonTweets) {
    		    Log.d("DEBUG", "Fetched home timeline");
    		    fillTweetAdapter(jsonTweets, newTweet);
        		hideProgressBar();
        	}

        	@Override
          protected void handleFailureMessage(Throwable e, String responseBody) {
          try {
              if (responseBody != null) {
                  Object jsonResponse = parseResponse(responseBody);
                  if(jsonResponse instanceof JSONObject) {
                   	  Log.d("DEBUG", "failed1!");
                   	  
                   	                      onFailure(e, (JSONObject)jsonResponse);
                  } else if(jsonResponse instanceof JSONArray) {
                   	  Log.d("DEBUG", "failed2!");
                      onFailure(e, (JSONArray)jsonResponse);
                  } else {
                   	  Log.d("DEBUG", "failed3!");
                     onFailure(e, responseBody);
                  }
              }else {
            	  Log.d("DEBUG", "failed4!");
                  onFailure(e, "");
              }
          }catch(JSONException ex) {
              onFailure(e, responseBody);
          }
      }
        	/*
			@Override
			public void onFailure(Throwable arg0, JSONArray arg1) {
				Log.d("DEBUG", "Fetch timeline failure");
				hideProgressBar();
       	    	Toast.makeText(getActivity(), "Something is wrong", Toast.LENGTH_SHORT).show();
			}
        	*/
        }, id);
    }

	public void setNewTweet(Tweet newTweet) {
		this.newTweet = newTweet;
	}
	
}
