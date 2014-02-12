package com.codepath.apps.twitterclientredux;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.twitterclientredux.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;



public class ComposeActivity extends Activity {
	EditText etNewBody;
	TextView tvCharLeft;
	TextView tvProfileName;
	ImageView ivProfileImage;
	
	Tweet newTweet = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		etNewBody = (EditText)findViewById(R.id.etNewBody);
		tvCharLeft = (TextView)findViewById(R.id.tvCharLeft);
		tvProfileName = (TextView)findViewById(R.id.tvProfileName);
		ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);

		String screenName = getIntent().getStringExtra("screenname");
		String profileImage = getIntent().getStringExtra("profileimageurl");
		
		tvProfileName.setText(screenName);
		ImageLoader.getInstance().displayImage(profileImage, ivProfileImage);
		etNewBody.addTextChangedListener(new TextWatcher() {

	           public void afterTextChanged(Editable s) {
	        	   int bodyLength = s.length();
	        	   int charLeft = 140 - bodyLength;
                   tvCharLeft.setText(new Integer(charLeft).toString() + " chars left");	        	   
	           }
	           public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	           public void onTextChanged(CharSequence s, int start, int before, int count) {}	
	       });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}

	/* When the Save button is clicked, send new tweets to API, 
	 * then close this compose activity itself  
	 */
	public void onTweet(View v) {
		String tweetText = etNewBody.getText().toString();
		if (tweetText.length()>0) {
            TwitterClientApp.getRestClient().postNewTweet(new JsonHttpResponseHandler() {
           	    @Override
        	    public void onSuccess(JSONObject jsonNewTweet) {
     		    	newTweet = Tweet.fromJson(jsonNewTweet);
                    if (newTweet!=null) {
       		            Intent data = new Intent();
       		            data.putExtra(TimelineActivity.TWEET_KEY, newTweet);
       	        	    setResult(Activity.RESULT_OK, data);
       		            // closes the activity and returns to first screen
       		            finish();
                    } 
         	    }
           	    
				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
           	    	Toast.makeText(ComposeActivity.this, "Network failure", Toast.LENGTH_SHORT).show();
				}

            }, tweetText);
        }
		else
			Toast.makeText(ComposeActivity.this, "I won't post an empty tweet",Toast.LENGTH_SHORT).show();
		
	}
	
	/* When the Cancel button is clicked, don't send anything, just close this activity window 
	 *   
	 */
	public void onCancel(View v) {
		setResult(Activity.RESULT_CANCELED);
		super.finish();
	}
	
}
