package com.codepath.apps.twitterclientredux;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.apps.twitterclientredux.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetAdapter extends ArrayAdapter {
   private Context context;
   
	@SuppressWarnings("unchecked")
	public TweetAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view ==null) {
			LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.tweet_item, null);
		}
		
		Tweet tweet = (Tweet) getItem(position);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
		imageView.setTag(tweet.getUser().getScreenName());
		ImageLoader.getInstance().displayImage(tweet.getUser().getProfileImageUrl(), imageView);
		// Set click listener to the image to display the user's profile
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getContext(), ProfileActivity.class);
				// Pass the user's screen name to intent
				i.putExtra("user_screen_name", (String)view.getTag());
				context.startActivity(i);
			}
			
		});
		
		TextView nameView = (TextView) view.findViewById(R.id.tvName);
		String formattedName = "<b>" + tweet.getUser().getName() + "</b>" + " <small><font color='#77777'>@" +
				tweet.getUser().getScreenName() + "</font></small>";
		nameView.setText(Html.fromHtml(formattedName));
		
		TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
		bodyView.setText(Html.fromHtml(tweet.getBody()));
		bodyView.setTag(tweet.getTweetId());
		bodyView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent i = new Intent(getContext(), TweetDetailActivity.class);
				i.putExtra("tweet_id", (Long)view.getTag());
			    context.startActivity(i);
			}
		});
		
		TextView dateView = (TextView) view.findViewById(R.id.tvCreateDate);
		Date createDate = tweet.getCreateDate();
		String diffDate = (String)DateUtils.getRelativeDateTimeString(getContext(), createDate.getTime(), 
				DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0);
		String showDiffDate = diffDate.substring(0, diffDate.indexOf(','));
		dateView.setText(showDiffDate);
		return view;
	}

}
