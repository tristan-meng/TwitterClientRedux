package com.codepath.apps.twitterclientredux.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclientredux.EndlessScrollListener;
import com.codepath.apps.twitterclientredux.NetworkService;
import com.codepath.apps.twitterclientredux.R;
import com.codepath.apps.twitterclientredux.TweetAdapter;
import com.codepath.apps.twitterclientredux.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetListFragment extends Fragment {
    PullToRefreshListView lvTweets;
	ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	TweetAdapter tweetAdapter;
	private String lastId = "";

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
      // Defines the xml file for the fragment
      View view = inflater.inflate(R.layout.fragment_timeline, container, false);
      lvTweets = (PullToRefreshListView) view.findViewById(R.id.lvTweets);
      // Set a listener to be invoked when the list should be refreshed.
	  tweetAdapter = new TweetAdapter(getActivity(),tweets);
	  lvTweets.setAdapter(tweetAdapter);
      fetchTweets("");
	  lvTweets.setOnScrollListener(new EndlessScrollListener() {
    	
    	   @Override
           public void onLoadMore() {
               // Triggered only when new data needs to be appended to the list
               // Add whatever code is needed to append new items to your AdapterView
               LoadMoreTweets(); 
           }
      });
      lvTweets.setOnRefreshListener(new OnRefreshListener() {
          @Override
          public void onRefresh() {
  	          if (NetworkService.hasInternet(getActivity())) {
  	        	  tweetAdapter.clear();
      	          setLastId("");
                  fetchTweets(getLastId());
  	          }
              lvTweets.onRefreshComplete();
          }
      });
      return view;
    }	
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    }
    
    protected void LoadMoreTweets () {
    	if (NetworkService.hasInternet(getActivity())) {
    		Log.d("DEBUG", "Tweet list load more: last id=" + getLastId());
    	    fetchTweets(getLastId());
    	}
    }
    
    @SuppressWarnings("unchecked")
    /*
     * A common method to populate the adapter with tweets from API. 
     * @param Tweet newTweet: the tweet that is just composed
     */
	protected void fillTweetAdapter(JSONArray jsonTweets, Tweet newTweet) {
		ArrayList<Tweet> moretweets = Tweet.fromJson(jsonTweets);
		tweetAdapter.addAll(moretweets);
		if (moretweets!=null && moretweets.size()>0) {
		    long lastlongid = moretweets.get(moretweets.size()-1).getTweetId();
		    Log.d("DEBUG", "Fetched last id=" + lastlongid);
		    setLastId(Long.valueOf(lastlongid-1L).toString());
		}
		else {
			setLastId("");
		}
		if (newTweet != null && (moretweets==null || moretweets.isEmpty() || newTweet.getTweetId() != moretweets.get(0).getTweetId())) {
			// In case we have not got the tweet we just composed.
			getTweetAdapter().insert(newTweet, 0);
    		getTweetAdapter().notifyDataSetChanged();
		}
	}
    
    protected abstract void fetchTweets(String id);
    
	
    public TweetAdapter getTweetAdapter() {
    	return this.tweetAdapter;
    }

	public String getLastId() {
		return lastId;
	}

	public void setLastId(String lastId) {
		this.lastId = lastId;
	}
    
    
}
