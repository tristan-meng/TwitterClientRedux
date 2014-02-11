package com.codepath.apps.twitterclientredux.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.twitterclientredux.TwitterClientApp;
import com.loopj.android.http.JsonHttpResponseHandler;


public class MentionsFragment extends TweetListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
        TwitterClientApp.getRestClient().getMentions(new JsonHttpResponseHandler() {
			@Override
        	public void onSuccess(JSONArray jsonTweets) {
    		    Log.d("DEBUG", "Fetched mentions");
    		    fillTweetAdapter(jsonTweets, null);
        		hideProgressBar();
        	}

			@Override
			public void onFailure(Throwable arg0, JSONArray arg1) {
				Log.d("DEBUG", "Fetch timeline failure");
				hideProgressBar();
       	    	Toast.makeText(getActivity(), "Something is wrong, can't get mentions", Toast.LENGTH_SHORT).show();
			}
        }, id);
    }
}
