package com.codepath.apps.twitterclientredux.models;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model implements Serializable{
	

	private static final long serialVersionUID = 842560931941511511L;
	@Column(name="UserId")
	private long userId;
	@Column(name="Name")
	private String name;
	@Column(name="ScreenName")
	private String screenName;
	@Column(name="ProfileImageUrl")
	private String profileImageUrl;
	@Column(name="NumTweets")
	private int numTweets;
	@Column(name="Tagline")
	private String tagline;
	@Column(name="Followers")
	private int followers;
	@Column(name="Following")
	private int following;
	

	public User() {
		super();
	}
	
	public User(long userId, String name, String screenName,
			String profileImageUrl, int numTweets, int followers, int following, String tagline) {
		super();
		this.userId = userId;
		this.name = name;
		this.screenName = screenName;
		this.profileImageUrl = profileImageUrl;
		this.numTweets = numTweets;
		this.followers = followers;
		this.following = following;
		this.tagline = tagline;
	}

	public User(User cloneUser) {
		super();
		this.userId = cloneUser.getUserId();
		this.name = cloneUser.getName();
		this.screenName = cloneUser.getScreenName();
		this.profileImageUrl = cloneUser.getProfileImageUrl();
		this.numTweets = cloneUser.getNumTweets();
		this.followers = cloneUser.getFollowers();
		this.following = cloneUser.getFollowing();
	}
	
	public long getUserId() {
		return this.userId;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	public String getScreenName() {
		return this.screenName;
	}
	
	public String getProfileImageUrl() {
		return this.profileImageUrl;
	}
	
	public int getNumTweets() {
		return this.numTweets;
	}

	
	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public int getFollowing() {
		return following;
	}

	public void setFollowing(int following) {
		this.following = following;
	}

	public String getTagline() {
		return tagline;
	}

	public void setTagline(String tagline) {
		this.tagline = tagline;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public void setNumTweets(int numTweets) {
		this.numTweets = numTweets;
	}

	public static User fromJson(JSONObject json) {
		User u = new User();
		
		try {
			u.userId = json.getLong("id");
			u.name = json.getString("name");
			u.screenName = json.getString("screen_name");
			u.profileImageUrl = json.getString("profile_image_url");
			u.numTweets = json.getInt("statuses_count");
			u.followers = json.getInt("followers_count");
			u.following = json.getInt("friends_count");
		    u.tagline = json.getString("description");
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return u;
	}

}
