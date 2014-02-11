package com.codepath.apps.twitterclientredux.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.twitterclientredux.TwitterClientApp;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetListFragment {
	private String userScreenName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.userScreenName = getArguments().getString("user_screen_name");
		
	}

    public void showProgressBar() {
	    getActivity().setProgressBarIndeterminateVisibility(true);
	}
	    
	public void hideProgressBar() {
	  	getActivity().setProgressBarIndeterminateVisibility(false);
	}
	
	
	/* Implement the abstract method
	 * (non-Javadoc)
	 * @see com.codepath.apps.twitterclientredux.fragments.TweetListFragment#fetchTweets(java.lang.String)
	 */
    protected void fetchTweets(String id) {
    	showProgressBar();
        TwitterClientApp.getRestClient().getUserTimeline(new JsonHttpResponseHandler() {
			@Override
        	public void onSuccess(JSONArray jsonTweets) {
    		    Log.d("DEBUG", "Fetched user timelines");
    		    fillTweetAdapter(jsonTweets, null);
        		hideProgressBar();
        	}

			@Override
			public void onFailure(Throwable arg0, JSONArray arg1) {
				Log.d("DEBUG", "Fetch timeline failure");
				hideProgressBar();
       	    	Toast.makeText(getActivity(), "Something is wrong", Toast.LENGTH_SHORT).show();
			}
        	
        	
        }, userScreenName, id);
    }
}
