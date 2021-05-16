package com.example.pocketnews_277.model;

import android.text.format.DateUtils;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleModel implements Serializable {

    private String title;
    private String author;
    private String description;
    private String publishedAt;
    private String urlToImage;
    private String url;
    private String content;
    private String category;

    private String returnEmptyIfNull(String value){
        return value == null? "": value;
    }

    private String formatDate(String date){
        return date.replaceAll("(T)|(:..Z)", " ");
    }

    public String getUrl() {
        return returnEmptyIfNull(url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return returnEmptyIfNull(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return returnEmptyIfNull(author);
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return returnEmptyIfNull(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

	@Exclude
    public String getAvgReadingTime(){
    	float totalChars = 200;
		Pattern pattern = Pattern.compile("\\[\\+([0-9]+) chars\\]");
		Matcher matcher = pattern.matcher(getContent());
		if (matcher.find())
		{
			totalChars += Integer.valueOf(matcher.group(1));
		}
		return (int) Math.ceil(totalChars/863) + " min read" ; // Average read is ~863 characters per minute
	}

    public String getPublishedAt() {
        return returnEmptyIfNull(publishedAt);
    }

	@Exclude
	public String getPublishedDate() {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy");
		try {
			return outputFormat.format(inputFormat.parse(publishedAt));
		} catch (Exception e){
			return "";
		}
	}

	@Exclude
    public String getPrettyPublishedAt(){
    	if (publishedAt == null || publishedAt.isEmpty()) return "";
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		try {
			Date date = inputFormat.parse(publishedAt);
			return (DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS).toString())
					.replace("In ", "").replace(" ago", "") + " ago";
		}
		catch (Exception e){
			return "";
		}
	}

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrlToImage() {
        return returnEmptyIfNull(urlToImage);
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getContent() {
        return returnEmptyIfNull(content);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return returnEmptyIfNull(category);
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArticleModel(String title, String author, String description, String publishedAt, String urlToImage, String url, String content) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.urlToImage = urlToImage;
        this.url = url;
        this.content = content;
		this.publishedAt =  publishedAt;
    }

	public ArticleModel(String author, String title, String publishedAt, String urlToImage, String content, String category) {
		this.author = author;
		this.title = title;
		this.publishedAt =  publishedAt;
		this.urlToImage = urlToImage;
		this.content = content;
		this.category = category;
	}

	@Override
	public String toString() {
		return "ArticleModel{" +
				"title='" + title + '\'' +
				", author='" + author + '\'' +
				", description='" + description + '\'' +
				", publishedAt='" + publishedAt + '\'' +
				", urlToImage='" + urlToImage + '\'' +
				", url='" + url + '\'' +
				", content='" + content + '\'' +
				", category='" + category + '\'' +
				'}';
	}
}
