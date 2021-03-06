package com.malmstein.yahnac.model;

import android.database.Cursor;
import android.text.TextUtils;

import com.malmstein.yahnac.data.HNewsContract;

import java.io.Serializable;

public class Story implements Serializable {

    public static String COMMENT_URL_BASE = "https://news.ycombinator.com/item?id=";
    public static String NEXT_URL_BASE = "https://news.ycombinator.com/";
    public static String ASK_URL_BASE = "item?id=";
    private final Long internalId;
    private final String by;
    private final Long id;
    private final Long timeAgo;
    private final int score;
    private final String title;
    private final String url;
    private final int comments;
    private final String type;
    private final Long timestamp;
    private final int rank;
    private final int read;
    private int bookmark;

    public Story(Long internalId, String by, Long id, String type, Long timeAgo, int score, String title, String url, int comments, Long timestamp, int rank, int bookmark, int read) {
        this.internalId = internalId;
        this.by = by;
        this.id = id;
        this.timeAgo = timeAgo;
        this.type = type;
        this.score = score;
        this.title = title;
        this.url = url;
        this.comments = comments;
        this.timestamp = timestamp;
        this.rank = rank;
        this.bookmark = bookmark;
        this.read = read;
    }

    public static Story from(Cursor cursor) {
        Long internalId = cursor.getLong(HNewsContract.StoryEntry.COLUMN_ID);
        Long id = cursor.getLong(HNewsContract.StoryEntry.COLUMN_ITEM_ID);
        String type = cursor.getString(HNewsContract.StoryEntry.COLUMN_TYPE);
        String by = cursor.getString(HNewsContract.StoryEntry.COLUMN_BY);
        int comments = cursor.getInt(HNewsContract.StoryEntry.COLUMN_COMMENTS);
        String url = cursor.getString(HNewsContract.StoryEntry.COLUMN_URL);
        int score = cursor.getInt(HNewsContract.StoryEntry.COLUMN_SCORE);
        String title = cursor.getString(HNewsContract.StoryEntry.COLUMN_TITLE);
        Long time = cursor.getLong(HNewsContract.StoryEntry.COLUMN_TIME_AGO);
        Long timestamp = cursor.getLong(HNewsContract.StoryEntry.COLUMN_TIMESTAMP);
        int rank = cursor.getInt(HNewsContract.StoryEntry.COLUMN_RANK);
        int bookmark = cursor.getInt(HNewsContract.StoryEntry.COLUMN_BOOKMARK);
        int read = cursor.getInt(HNewsContract.StoryEntry.COLUMN_READ);

        return new Story(internalId, by, id, type, time, score, title, url, comments, timestamp, rank, bookmark, read);
    }

    public static Story fromBookmark(Cursor cursor) {
        Long internalId = cursor.getLong(HNewsContract.BookmarkEntry.COLUMN_ID);
        Long id = cursor.getLong(HNewsContract.BookmarkEntry.COLUMN_ITEM_ID);
        String type = cursor.getString(HNewsContract.BookmarkEntry.COLUMN_TYPE);
        String by = cursor.getString(HNewsContract.BookmarkEntry.COLUMN_BY);
        String url = cursor.getString(HNewsContract.BookmarkEntry.COLUMN_URL);
        String title = cursor.getString(HNewsContract.BookmarkEntry.COLUMN_TITLE);
        Long timestamp = cursor.getLong(HNewsContract.BookmarkEntry.COLUMN_TIMESTAMP);
        int bookmark = HNewsContract.TRUE_BOOLEAN;

        return new Story(internalId, by, id, type, (long) 0, 0, title, url, 0, timestamp, 0, bookmark, 0);
    }

    public String getSubmitter() {
        return by;
    }

    private boolean isSubmitterEmpty() {
        return TextUtils.isEmpty(by);
    }

    public boolean isStoryAJob() {
        return isSubmitterEmpty();
    }

    public Long getId() {
        return id;
    }

    public Long getTimeAgo() {
        return timeAgo;
    }

    public int getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public int getComments() {
        return comments;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getCommentsUrl() {
        return COMMENT_URL_BASE + getId();
    }

    public int getRank() {
        return rank;
    }

    public boolean isHackerNewsLocalItem() {
        boolean isLocalItem = false;
        if (getType().equals(TYPE.ask.name())) {
            isLocalItem = true;
        }

        if (url.startsWith(ASK_URL_BASE)) {
            isLocalItem = true;
        }

        return isLocalItem;
    }

    public boolean isBookmark() {
        return bookmark == HNewsContract.TRUE_BOOLEAN;
    }

    public boolean isRead() {
        return read == HNewsContract.TRUE_BOOLEAN;
    }

    public void toggleBookmark() {
        if (bookmark == HNewsContract.FALSE_BOOLEAN) {
            bookmark = HNewsContract.TRUE_BOOLEAN;
        } else {
            bookmark = HNewsContract.FALSE_BOOLEAN;
        }
    }

    public enum TYPE {
        top_story,
        new_story,
        best_story,
        show,
        ask,
        jobs
    }
}