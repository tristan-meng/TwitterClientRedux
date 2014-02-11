package com.codepath.apps.twitterclientredux;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclientredux.fragments.UserTimelineFragment;
import com.codepath.apps.twitterclientredux.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	private String userScreenName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_profile);
		userScreenName = (String)(getIntent().getStringExtra("user_screen_name"));
		showProgressBar();
		TwitterClientApp.getRestClient().getUserProfile(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				User user = User.fromJson(json);
				getActionBar().setTitle("@" + user.getScreenName());
				populateProfileHeader(user);
				
				UserTimelineFragment frUserTimeline = new UserTimelineFragment();
				Bundle args = new Bundle();
				args.putString("user_screen_name", userScreenName);
				frUserTimeline.setArguments(args);
				FragmentManager manager = getSupportFragmentManager();
				android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
				fts.replace(R.id.frame_usertimeline, frUserTimeline);
					
				fts.commit();
			}
			
		}, userScreenName); 
		hideProgressBar();
	}

	public void showProgressBar() {
    	setProgressBarIndeterminateVisibility(true);
    }
    
    public void hideProgressBar() {
    	setProgressBarIndeterminateVisibility(false);
    }


	protected void populateProfileHeader(User user) {

		TextView tvUserProfileName = (TextView) findViewById(R.id.tvUserProfileName);
		TextView tvUserProfileTagline = (TextView)findViewById(R.id.tvUserProfileTagline);
		TextView tvFollowers = (TextView)findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView)findViewById(R.id.tvFollowing);
		ImageView ivUserProfileImage = (ImageView)findViewById(R.id.ivUserProfileImage);
		tvUserProfileName.setText(user.getName());
		tvUserProfileTagline.setText(user.getTagline());
		tvFollowers.setText(user.getFollowers() + " followers");
		tvFollowing.setText(user.getFollowing() + " following");
		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivUserProfileImage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
