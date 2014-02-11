package com.codepath.apps.twitterclientredux.fragments;

import org.json.JSONArray;

import com.codepath.apps.twitterclientredux.TwitterClientApp;
import com.codepath.apps.twitterclientredux.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
			public void onFailure(Throwable arg0, JSONArray arg1) {
				Log.d("DEBUG", "Fetch timeline failure");
				hideProgressBar();
       	    	Toast.makeText(getActivity(), "Something is wrong", Toast.LENGTH_SHORT).show();
			}
        	
        }, id);
    }

	public void setNewTweet(Tweet newTweet) {
		this.newTweet = newTweet;
	}
	
}
