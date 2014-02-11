package com.codepath.apps.twitterclientredux.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name="Tweets")
public class Tweet extends Model implements Serializable {


	private static final long serialVersionUID = 8185920029063073815L;
	@Column(name="TweetId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long tweetId;
	@Column(name="Body")
	private String body;
	@Column(name="Retweeted")
    private boolean retweeted;
	@Column(name="Favorited")
    private boolean favorited;
	@Column(name="CreateDate", index=true)
    private Date createDate;
	@Column(name="User")
	private User user;
    
	public Tweet() {
		super();
	}
	
	public Tweet(User user, long tweetId, String body, boolean retweeted,
			boolean favorited, Date createDate) {
		super();
		this.user = user;
		this.tweetId = tweetId;
		this.body = body;
		this.retweeted = retweeted;
		this.favorited = favorited;
		this.createDate = createDate;
	}

	public Tweet(Tweet cloneTweet) {
		super();
		this.tweetId = cloneTweet.getTweetId();
		this.body = cloneTweet.getBody();
		this.retweeted = cloneTweet.isRetweeted();
		this.favorited = cloneTweet.isFavorited();
		this.createDate = cloneTweet.getCreateDate();
	}
	
	public User getUser() {
		return user;
	}
	
	public String getBody() {
		return this.body;
	}

	public long getTweetId() {
		return this.tweetId;
	}
	
	public boolean isFavorited() {
		return this.favorited;
	}
	
	public boolean isRetweeted() {
		return this.retweeted;
	}
	
	public Date getCreateDate() {
		return this.createDate;
	}

	
	public void setUser(User user) {
		this.user = user;
	}

	public void setTweetId(long tweetId) {
		this.tweetId = tweetId;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setRetweeted(boolean retweeted) {
		this.retweeted = retweeted;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		
		try {
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
			tweet.tweetId = jsonObject.getLong("id");
			tweet.body = jsonObject.getString("text");
			tweet.favorited = jsonObject.getBoolean("favorited");
			tweet.retweeted = jsonObject.getBoolean("retweeted");
			tweet.createDate = getDate(jsonObject.getString("created_at"));
			
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tweet;
	}
	
	public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		
		for (int i=0; i<jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			}
			catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			Tweet tweet = Tweet.fromJson(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}
		return tweets;
	}

	@SuppressLint("SimpleDateFormat")
	protected static Date getDate(String dateString) {
		 try {
			 SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
			 return dateFormat.parse(dateString);
		 } catch (ParseException pe) {
			pe.printStackTrace();
			return null;
		} 
	 }
	
    public static List<Tweet> getAll() {
        return new Select()
          .from(Tweet.class)
          .orderBy("CreateDate ASC")
          .execute();
    }

}