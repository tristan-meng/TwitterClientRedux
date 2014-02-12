package com.codepath.apps.twitterclientredux;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclientredux.models.Tweet;
import com.codepath.apps.twitterclientredux.models.Url;
import com.codepath.apps.twitterclientredux.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_tweet_detail);
		Long tweetId = (Long)(getIntent().getLongExtra("tweet_id",0));
		showProgressBar();
		TwitterClientApp.getRestClient().getTweetDetail(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				Tweet tweet = Tweet.fromJson(json);
				User user = tweet.getUser();
				TextView tvDetailUserName = (TextView) findViewById(R.id.tvDetailUserName);
				TextView tvDetailUserScreenName = (TextView)findViewById(R.id.tvDetailUserScreenName);
				TextView tvDetailTweet = (TextView)findViewById(R.id.tvDetailTweet);
				TextView tvDetailCounts = (TextView)findViewById(R.id.tvDetailCreateDate);
				TextView tvDetailCreateDate = (TextView)findViewById(R.id.tvDetailCounts);
				ImageView ivDetailUserImage = (ImageView)findViewById(R.id.ivDetailUserImage);
				tvDetailUserName.setText(user.getName());
				tvDetailUserScreenName.setText(user.getScreenName());
				tvDetailTweet.setText(Html.fromHtml(ApplyURL(tweet.getBody(), tweet.getUrls())));
				// tvDetailTweet.setMovementMethod(LinkMovementMethod.getInstance());
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy h:mm a");
				tvDetailCreateDate.setText(dateFormat.format(tweet.getCreateDate()));
				tvDetailCounts.setText(tweet.getRetweetCount() + " retweets");
				ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivDetailUserImage);
			}
			
		}, tweetId); 
		hideProgressBar();
	}

	public void showProgressBar() {
    	setProgressBarIndeterminateVisibility(true);
    }
    
    public void hideProgressBar() {
    	setProgressBarIndeterminateVisibility(false);
    }
	
    
    public String ApplyURL(String body, ArrayList<Url> urls) {
    	String convertBody = body;
    	
    	int lastIndex = 0;
    	convertBody = "";
    	for (int i=0; i<urls.size(); i++) {
    		Url l = urls.get(i);
    		
    		convertBody += body.substring(lastIndex, l.getStartIndex());
    		convertBody += "<a href=\"" + l.getUrl() + "\">" + body.substring(l.getStartIndex(),l.getEndIndex()) + "</a>";
            lastIndex = l.getEndIndex();
    	}
    	convertBody += body.substring(lastIndex);
    	return convertBody;
    	
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweet_detail, menu);
		return true;
	}

}
