package com.codepath.apps.twitterclientredux.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.activeandroid.Model;

public class Url extends Model implements Serializable {
	
	private static final long serialVersionUID = 287313332924613157L;
	String url;
	int startIndex;
	int endIndex;

	public Url() {
		super();
	}
	
	public Url(String url, int startIndex, int endIndex) {
		super();
		this.url = url;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}



	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public static ArrayList<Url> fromJson(JSONArray jsonArray) {
		ArrayList<Url> urls = new ArrayList<Url>(jsonArray.length());
		
		for (int i=0; i<jsonArray.length(); i++) {
			JSONObject urlJson = null;
			try {
				urlJson = jsonArray.getJSONObject(i);
			}
			catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			Url url = Url.fromJson(urlJson);
			if (url != null) {
				urls.add(url);
			}
		}
		return urls;
	}	
	
	public static Url fromJson(JSONObject json) {
		Url l = new Url();
		
		try {
			l.url = json.getString("url"); 
			JSONArray indicesArray = json.getJSONArray("indices");
			l.startIndex = (Integer)indicesArray.get(0);
			l.endIndex = (Integer)indicesArray.get(1);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return l;
	}	

}
