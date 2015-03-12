package com.codepath.apps.Tweeter.models;

/**
 * Created by moulib on 2/22/15.
 */
public class QueryCtrs {
    private long sinceId = Long.MIN_VALUE;
    private long maxId = Long.MIN_VALUE;

    private int count = 25;

    private static QueryCtrs instance = null;

    protected QueryCtrs() {
    }

    public long getSinceId() {
        return sinceId;
    }

    public void setSinceId(long sinceId) {
        if (sinceId > this.sinceId) {
            this.sinceId = sinceId;
        }
    }

    public long getMaxId() {
        return maxId;
    }

    public void setMaxId(long maxId) {
        if (maxId > this.maxId) {
            this.maxId = maxId - 1;
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static QueryCtrs getInstance() {
        if (instance == null) {
            instance = new QueryCtrs();
        }
        return instance;
    }
}