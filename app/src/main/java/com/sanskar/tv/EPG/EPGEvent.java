package com.sanskar.tv.EPG;

/**
 * Created by Kristoffer.
 */
public class EPGEvent {

    private final long start;
    private final long end;
    private final String title;

    private final String starts;
    private final String ends;
    private final String url;
    private final String releasedDate;




    public EPGEvent(long start, long end, String starts, String ends, String title, String url, String releasedDate) {
        this.start = start;
        this.end = end;
        this.starts = starts;
        this.ends = ends;
        this.title = title;
        this.url = url;
        this.releasedDate =releasedDate;
    }

    public String getUrl() {
        return url;
    }

    public String getReleasedDate() {
        return releasedDate;
    }

    public String getStarts() {
        return starts;
    }

    public String getEnds() {
        return ends;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCurrent() {
        long now = System.currentTimeMillis();
        return now >= start && now <= end;
    }
}
