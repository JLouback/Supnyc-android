package com.example.julianalouback.supnyc.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by moldy530 on 11/22/14.
 */
public class Event {
    public static final String[] TYPES = new String[] {
            "party","bar","dining","culture"
    };

    private static SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat("MMM dd HH:mm");

    private String mTitle;
    private String mDescription;
    private String mAddress;
    private Double mLatitude;
    private Double mLongitude;
    private String mHostUsername;
    private Long mStart;
    private Long mEnd;
    private String mType;
    private String mImageUrl;
    private Long mLikeCount;
    private Long mIntendToGoCount;
    private boolean userLiked;

    public Event(String title, String desc, String address, String mHostUsername, Long mStart, Long mEnd, String mType, String mImageUrl){
        this.mTitle = title;
        this.mDescription = desc;
        this.mAddress = address;
        this.mHostUsername = mHostUsername;
        this.mStart = mStart;
        this.mEnd = mEnd;
        this.mType = mType;
        this.mImageUrl = mImageUrl;
    }

    public void setUserLiked(boolean liked){
        this.userLiked = liked;
    }
    public boolean getUserLiked(){
        return this.userLiked;
    }

    public void setTitle(String title) { mTitle = title; }
    public String getTitle() { return mTitle; }

    public void setDescription(String description) { mDescription = description; }
    public String getDescription() { return mDescription; }

    public void setAddress(String address) { mAddress = address; }
    public String getAddress() { return mAddress; }

    public void setLatitude(Double latitude) { mLatitude = latitude; }
    public void setLatitude(String latitude) { mLatitude = Double.valueOf(latitude); }
    public Double getLatitude() { return mLatitude; }

    public void setLongitude(Double longitude) { mLongitude = longitude; }
    public void setLongitude(String longitude) { mLongitude = Double.valueOf(longitude); }
    public Double getLongitude() { return mLongitude; }

    public void setHostUsername(String hostUsername) { mHostUsername = hostUsername; }
    public String getHostUsername() { return mHostUsername; }

    public void setType(String type) { mType = type; }
    public String getType() { return mType; }

    public void setImageUrl(String imageUrl) { mImageUrl = imageUrl; }
    public String getImageUrl() { return mImageUrl; }

    public void setStart(Long start) { mStart = start; }
    public Long getStart() { return mStart; }
    public void setStart(String start) throws ParseException { mStart = sSimpleDateFormat.parse(start).getTime(); }
    public String getFormattedStart() {
        return (mStart != null ? sSimpleDateFormat.format(new Date(mStart)) : "");
    }

    public void setEnd(Long end) { mEnd = end; }
    public Long getEnd() { return mEnd; }
    public void setEnd(String end) throws ParseException { mEnd = sSimpleDateFormat.parse(end).getTime(); }
    public String getFormattedEnd() {
        return (mEnd != null ? sSimpleDateFormat.format(new Date(mEnd)) : "");
    }

    public void setLikeCount(Long likeCount) { mLikeCount = likeCount; }
    public void setLikeCount(String likeCount) { mLikeCount = Long.valueOf(likeCount); }
    public Long getLikeCount() { return mLikeCount; }

    public void setIntendToGoCount(Long intendToGoCount) { mIntendToGoCount = intendToGoCount; }
    public void setIntendToGoCount(String intendToGoCount) { mIntendToGoCount = Long.valueOf(intendToGoCount); }
    public Long getIntendToGoCount() { return mIntendToGoCount; }

}
