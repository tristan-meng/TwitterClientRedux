package com.codepath.apps.twitterclientredux;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
import android.widget.Toast;

import com.codepath.apps.twitterclientredux.fragments.MentionsFragment;
import com.codepath.apps.twitterclientredux.fragments.TimelineFragment;
import com.codepath.apps.twitterclientredux.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity implements TabListener {
	public static final int COMPOSE_REQUEST_CODE = 10; 
    public static final String TWEET_KEY = "TWEET";

    String userScreenName;
    String userProfileImageUrl;
    
    Tab tabHome;
    Tab tabMention;
    
    private boolean bComposeReturned;
    private Tweet newTweet = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_timeline);
		fetchUserProfile();  // Fetch user profile in order to fill user info for composing
        setupNavigationTabs();
    }

    public void showProgressBar() {
    	setProgressBarIndeterminateVisibility(true);
    }
    
    public void hideProgressBar() {
    	setProgressBarIndeterminateVisibility(false);
    }

    private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		tabHome = actionBar.newTab().setText("Home").setTag("TimelineFragment").setIcon(R.drawable.ic_home).setTabListener(this);
		tabMention = actionBar.newTab().setText("Mention").setTag("MentionsFragment").setIcon(R.drawable.ic_mention).setTabListener(this);
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMention);
		actionBar.selectTab(tabHome);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }
    
    public void onComposeAction(MenuItem mi)  {
    	if (!NetworkService.hasInternet(this)) {
   	    	Toast.makeText(TimelineActivity.this, "No internet connection, can't tweet now", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	if (this.userScreenName==null || this.userScreenName.length()==0)
    		fetchUserProfile();
    	Intent i = new Intent(this, ComposeActivity.class);
    	i.putExtra("screenname", this.userScreenName);
    	i.putExtra("profileimageurl", this.userProfileImageUrl);    // Pass the item text
    	startActivityForResult(i, COMPOSE_REQUEST_CODE);
    }
    
    public void onProfileAction(MenuItem mi) {
    	Intent i = new Intent(this, ProfileActivity.class);
    	startActivity(i);
    }
    
    /* Get back the new tweet */
    @SuppressWarnings("unchecked")
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
    	if (requestCode==COMPOSE_REQUEST_CODE && resultCode==Activity.RESULT_OK) {
    		bComposeReturned = true;
       		newTweet = (Tweet)(i.getSerializableExtra(TWEET_KEY));
    		newTweet.save();
   	    }
    }
    
    @Override
    protected void onPostResume() {
    	super.onPostResume();
    	if (bComposeReturned) {
    		// Parse the new tweet to home timeline
			TimelineFragment frTimeline = new TimelineFragment();
			Bundle args = new Bundle();
			args.putSerializable("new_tweet", newTweet);
			frTimeline.setArguments(args);
    		FragmentManager manager = getSupportFragmentManager();
    		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
    		fts.replace(R.id.frame_container, frTimeline);
    		fts.commit();
    		tabHome.select();
    		bComposeReturned = false;
    		newTweet = null;
    	}
    	
    }
    
    protected void fetchUserProfile() {
        TwitterClientApp.getRestClient().getUserProfile(new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(JSONObject jsonProfile) {
        		try {
					userScreenName = (String)jsonProfile.getString("screen_name");
					userProfileImageUrl = (String)jsonProfile.getString("profile_image_url");
				} catch (JSONException e) {
					e.printStackTrace();
				    hideProgressBar();
        	    }
                hideProgressBar();
        	}
        }, null);
    }

    @Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

   @Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		if (tab.getTag().equals("TimelineFragment")) {
			fts.replace(R.id.frame_container, new TimelineFragment());
		}
		else {
			fts.replace(R.id.frame_container, new MentionsFragment());
		}
		fts.commit();
		
	}



	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}   
    
    
}
